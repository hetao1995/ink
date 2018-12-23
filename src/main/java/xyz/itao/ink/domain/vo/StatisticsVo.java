package xyz.itao.ink.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hetao
 * @date 2018-12-06
 * @description 后台统计对象
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsVo implements Serializable {

    /**
     * 文章数
     */
    private Long articles;
    /**
     * 页面数
     */
    private Long pages;
    /**
     * 评论数
     */
    private Long comments;
    /**
     * 分类数
     */
    private Long categories;
    /**
     * 标签数
     */
    private Long tags;
    /**
     * 附件数
     */
    private Long attaches;

}
