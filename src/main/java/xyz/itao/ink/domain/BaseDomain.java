package xyz.itao.ink.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-03
 * @description
 */
@Data
public abstract class BaseDomain {
    /**
     * id
     */
    protected Long id;

    /**
     * 是否已经被删除了
     */
    protected Boolean deleted;
    /**
     * 创建用户的时间戳
     */
    protected Date createTime;

    /**
     * 更新的时间戳
     */
    protected Date updateTime;

    /**
     * 被谁创建
     */
    protected Long createBy;

    /**
     * 被谁修改
     */
    protected Long updateBy;
}
