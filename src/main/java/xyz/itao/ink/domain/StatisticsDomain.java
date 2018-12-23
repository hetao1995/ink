package xyz.itao.ink.domain;

import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.vo.StatisticsVo;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.LinkRepository;
import xyz.itao.ink.repository.MetaRepository;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
public class StatisticsDomain {
    StatisticsDomain(ContentRepository contentRepository,
                     CommentRepository commentRepository,
                     MetaRepository metaRepository,
                     LinkRepository linkRepository){
        this.contentRepository = contentRepository;
        this.commentRepository = commentRepository;
        this.metaRepository = metaRepository;
        this.linkRepository = linkRepository;
    }
    ContentRepository contentRepository;
    CommentRepository commentRepository;
    MetaRepository metaRepository;
    LinkRepository linkRepository;

    /**
     * 文章数
     */
    public Long getArticles() {
        return contentRepository.countContentNum(TypeConst.ARTICLE, true);
    }

    /**
     * 页面数
     */
    public Long getPages() {
        return contentRepository.countContentNum(TypeConst.PAGE, true);
    }

    /**
     * 评论数
     */
    public Long getComments() {
        return commentRepository.countCommentNum(true);
    }

    /**
     * 分类数
     */
    public Long getCategories() {
        return metaRepository.countMetaNum(TypeConst.CATEGORY);
    }

    /**
     * 标签数
     */
    public Long getTags() {
        return metaRepository.countMetaNum(TypeConst.TAG);
    }

    /**
     * 附件数
     */
    public Long getAttaches() {
        return linkRepository.countLinkNum();
    }

    public StatisticsVo vo(){
        return StatisticsVo
                .builder()
                .articles(this.getArticles())
                .attaches(this.getAttaches())
                .comments(this.getComments())
                .categories(this.getCategories())
                .pages(this.getPages())
                .tags(this.getTags())
                .build();
    }
}
