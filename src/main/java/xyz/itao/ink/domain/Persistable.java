package xyz.itao.ink.domain;

/**
 * @author hetao
 * @date 2018-11-29 23:25
 * @description 持久化支持，所有的domain都应该支持持久化
 */
public interface Persistable<T> {
    /**
     * 将当前domain中的数据持久化
     *
     * @return 持久化结果
     */
    boolean save();

    /**
     * 根据业务逻辑组装domain
     *
     * @return 组装结果
     */
    T assemble();
}
