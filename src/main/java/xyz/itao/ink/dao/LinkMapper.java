package xyz.itao.ink.dao;

import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.Link;

import java.util.List;

/**
 * @author hetao
 */
@Component
public interface LinkMapper {

    /**
     * 将非空数据插入到数据库
     *
     * @param record 要插入的数据
     * @return 是否插入成功
     */
    boolean insertSelective(Link record);

    /**
     * 通过主键查找数据
     *
     * @param id 主键id
     * @return 主键对应的Content实例
     */
    Link selectByPrimaryKey(Long id);

    /**
     * 更新数据，主键不能为空，注意，删除数据也是调用此方法，只是将deleted设置为true
     *
     * @param record 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateByPrimaryKeySelective(Link record);

    /**
     * 多种条件查找，包括id, slug, 等
     * 这些条件全部满足才会返回
     *
     * @param record 条件
     * @return 满足条件的Content实例
     */
    List<Link> selectByNoNulProperties(Link record);

    /**
     * 统计所有的link数目
     *
     * @return 结果
     */
    Long countLink();
}