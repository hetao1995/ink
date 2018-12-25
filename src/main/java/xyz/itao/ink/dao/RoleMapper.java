package xyz.itao.ink.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.Role;

import java.util.List;

@Component
public interface RoleMapper {

    /**
     * 将非空数据插入到数据库
     * @param record 要插入的数据
     * @return 是否插入成功
     */
    boolean insertSelective(Role record);

    /**
     * 通过主键查找数据
     * @param id 主键id
     * @return 主键对应的User实例
     */
    Role selectByPrimaryKey(Long id);

    /**
     * 更新数据，主键不能为空，注意，删除数据也是调用此方法，只是将deleted设置为true
     * @param record 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateByPrimaryKeySelective(Role record);

    /**
     * 多种条件查找，包括id、deleted、actived等
     * 这些条件全部满足才会返回
     * @param record 条件
     * @return 满足条件的User实例
     */
    List<Role> selectByNoNulProperties(Role record);

    /**
     * 通过userId关联userrole查询role
     * @param userId
     * @return
     */
    List<Role> selectByUserId(@Param("userId") Long userId);
}