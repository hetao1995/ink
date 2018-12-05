package xyz.itao.ink.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@Builder
public class LogVo {
    /**
     * 日志的id
     */
    private Long id;

    /**
     * 发起人id
     */
    private Long userId;

    /**
     * 访问者id
     */
    private String ip;

    /**
     * 访问者代理
     */
    private String agent;

    /**
     * 操作的数据
     */
    private String data;

    /**
     * 执行的操作
     */
    private String action;
}
