package xyz.itao.ink.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-06
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 父级项目
     */
    private Long parentId;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目缩略名
     */
    private String slug;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目详情
     */
    private String detail;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 下属文章数目
     */
    private Integer count;
}
