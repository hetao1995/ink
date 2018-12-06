package xyz.itao.ink.service;

import xyz.itao.ink.domain.vo.*;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public interface SiteService {
    void cleanCache(String key);

    List<MetaVo> getMetaVo(String recentMeta, String category, int maxPosts);

    List<ArchiveVo> getArchives();

    List<CommentVo> recentComments(int i);

    List<ContentVo> getContens(String recentArticle, int i);

    StatisticsVo getStatistics();
}
