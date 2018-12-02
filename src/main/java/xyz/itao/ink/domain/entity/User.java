package xyz.itao.ink.domain.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-02
 * @description users表的实体类
 */
@Data
public class User {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 是否已经被删除了
     */
    private Boolean deleted;

    /**
     * 是否处于激活状态
     */
    private Boolean actived;

    /**
     * 用户名，由字母、数字、下划线组成，长度在30以下，不能重复
     */
    private String username;

    /**
     * 密码加盐sha512散列之后的值
     */
    private String password;

    /**
     * 显示的用户名
     */
    private String displayName;

    /**
     * 邮箱，不能重复
     */
    private String email;

    /**
     * 加密的盐，这个用户所有需要生成摘要的地方都用这个
     */
    private String salt;

    /**
     * 最近一次登陆时间
     */
    private Date lastLogin;

    /**
     * 创建用户的时间戳
     */
    private Date createTime;

    /**
     * 更新的时间戳
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

    /**
     * 用户的主页，不能重复
     */
    private String hmeUrl;

}