package xyz.itao.ink.dao;

import xyz.itao.ink.domain.entity.Link;

public interface LinkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Link record);

    int insertSelective(Link record);

    Link selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Link record);

    int updateByPrimaryKey(Link record);
}