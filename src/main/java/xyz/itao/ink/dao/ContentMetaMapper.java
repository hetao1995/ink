package xyz.itao.ink.dao;

import xyz.itao.ink.domain.entity.ContentMeta;

public interface ContentMetaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ContentMeta record);

    int insertSelective(ContentMeta record);

    ContentMeta selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContentMeta record);

    int updateByPrimaryKey(ContentMeta record);
}