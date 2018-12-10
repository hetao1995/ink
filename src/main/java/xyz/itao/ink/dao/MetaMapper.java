package xyz.itao.ink.dao;

import xyz.itao.ink.domain.entity.Meta;

import xyz.itao.ink.domain.entity.Meta;

public interface MetaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Meta record);

    int insertSelective(Meta record);

    Meta selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Meta record);

    int updateByPrimaryKey(Meta record);
}