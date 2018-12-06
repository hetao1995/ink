package xyz.itao.ink.service;

import xyz.itao.ink.domain.vo.MetaVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public interface SiteService {
    void cleanCache(String key);

    List<MetaVo> getMetaVo(String recentMeta, String category, int maxPosts);
}
