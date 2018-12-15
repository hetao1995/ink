package xyz.itao.ink.domain.vo;

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
public class StatisticsVo implements Serializable {

    private static final long serialVersionUID = 2329863829741481287L;
    // 文章数
    private long articles;
    // 页面数
    private long pages;
    // 评论数
    private long comments;
    // 分类数
    private long categories;
    // 标签数
    private long tags;
    // 附件数
    private long attachs;
    // 友链数
    private long links;

}
