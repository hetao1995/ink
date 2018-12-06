package xyz.itao.ink.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Data
@Builder
public class MetaVo {
    /**
     * 主键id
     */
    private Long id;
    

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置值
     */
    private String value;

    /**
     * 配置详情
     */
    private String detail;
}
