package xyz.itao.ink.utils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 简单的Id生成工具，long型，1位不使用，距离START_STMP时间43位，序列号20位
 *
 * @author hetao
 * @date 2018-12-01 23:33
 */
public class IdUtils {
    /**
     * 生成id的参数
     */
    static class IdGenArgs {
        //序列号
        long sequence = 0L;
        //上一次时间戳
        long stamp = 0L;
    }

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * 序列号占用的位数
     */
    private final static long SEQUENCE_BIT = 20;

    /**
     * 序列号最大值
     */
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 时间戳向左的位移
     */
    private final static long TIMESTMP_LEFT = SEQUENCE_BIT;

    /**
     * 原子引用
     */
    private static AtomicReference<IdGenArgs> argsAtomicReference = new AtomicReference<>(new IdGenArgs());

    /**
     * 产生下一个ID
     *
     * @return long型的id
     */
    public static long nextId() {
        IdGenArgs lastArgs, currArgs;
        long stamp, sequence;
        while (true) {
            lastArgs = argsAtomicReference.get();
            currArgs = new IdGenArgs();
            currArgs.stamp = System.currentTimeMillis();
            while (lastArgs.stamp > currArgs.stamp) {
                lastArgs = argsAtomicReference.get();
                currArgs.stamp = System.currentTimeMillis();
            }
            if (lastArgs.stamp == currArgs.stamp) {
                if (lastArgs.sequence == MAX_SEQUENCE) {
                    continue;
                }
                currArgs.sequence = lastArgs.sequence + 1;
            } else {
                currArgs.sequence = 0L;
            }
            stamp = currArgs.stamp;
            sequence = currArgs.sequence;
            if (argsAtomicReference.compareAndSet(lastArgs, currArgs)) {
                break;
            }
        }
        return ((stamp - START_STMP) << TIMESTMP_LEFT | sequence) & 0xEFFFFFFFFFFFFFFFL;
    }

}
