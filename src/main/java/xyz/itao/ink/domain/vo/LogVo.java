package xyz.itao.ink.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogVo {
    /**
     * 日志的id
     */
    private Long id;


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
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date createTime;
}
