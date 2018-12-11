package xyz.itao.ink.dao;

import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.OptionDomain;
import xyz.itao.ink.domain.entity.Option;

import java.util.List;

@Component
public interface OptionMapper {
    int deleteByPrimaryKey(Long id);

    /**
     * 将非空数据插入到数据库
     * @param record 要插入的数据
     * @return 是否插入成功
     */
    boolean insertSelective(Option record);

    /**
     * 通过主键查找数据
     * @param id 主键id
     * @return 主键对应的Content实例
     */
    Option selectByPrimaryKey(Long id);

    /**
     * 更新数据，主键不能为空，注意，删除数据也是调用此方法，只是将deleted设置为true
     * @param record 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateByPrimaryKeySelective(Option record);

    /**
     * 多种条件查找，包括id, slug, 等
     * 这些条件全部满足才会返回
     * @param record 条件
     * @return 满足条件的Content实例
     */
    List<Option> selectByNoNulProperties(Option record);

    /**
     * 根据通配符加载所有的Option
     * @param pattern 通配符
     * @return 满足条件的没有被删除的option
     */
    List<Option> selectNotDeleteByNamePattern(String pattern);
}
