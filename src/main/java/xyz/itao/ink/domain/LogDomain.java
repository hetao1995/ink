package xyz.itao.ink.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.itao.ink.domain.entity.Log;
import xyz.itao.ink.domain.vo.LogVo;
import xyz.itao.ink.repository.LogRepository;
import xyz.itao.ink.repository.UserRepository;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@Accessors(chain = true)
public class LogDomain extends BaseDomain {
    
    LogDomain(LogRepository logRepository, UserRepository userRepository){
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }
    LogRepository logRepository;
    UserRepository userRepository;
    
    /**
     * 日志的id
     */
    private Long id;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

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

    public  UserDomain getUserDomain(){
        return userRepository.loadActiveUserDomainById(userId);
    }
    
    public LogDomain assemble(Log entity){
        return this
                .setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setIp(entity.getIp())
                .setAgent(entity.getAgent())
                .setData(entity.getData())
                .setAction(entity.getAction())
                .setUserId(entity.getUserId());
    }
    
    public Log entity(){
        return Log
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .active(this.getActive())
                .ip(this.getIp())
                .agent(this.getAgent())
                .data(this.getData())
                .action(this.getAction())
                .userId(this.getUserId())
                .build();
    }
    
    public LogVo vo(){
        UserDomain userDomain = this.getUserDomain();
        return LogVo
                .builder()
                .id(this.getId())
                .ip(this.getIp())
                .agent(this.getAgent())
                .data(this.getData())
                .action(this.getAction())
                .operator(userDomain==null ? this.getIp() : userDomain.getDisplayName())
                .createTime(this.createTime)
                .build();
    }

    public LogDomain save(){
        return logRepository.saveNewLogDomain(this);
    }
}
