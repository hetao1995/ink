package xyz.itao.ink.dao;

import xyz.itao.ink.domain.entity.Option;

public interface OptionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Option record);

    int insertSelective(Option record);

    Option selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Option record);

    int updateByPrimaryKey(Option record);
}
