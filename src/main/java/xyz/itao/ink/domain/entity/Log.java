package xyz.itao.ink.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-02
 * @description logs表的实体类
 */
@Data
@Builder
public class Log {
    /**
     * 日志的id
     */
    private Long id;

    /**
     * 是否被删除
     */
    private Boolean deleted;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 被谁创建
     */
    private Long createBy;

    /**
     * 被谁修改
     */
    private Long updateBy;

}