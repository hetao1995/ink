package xyz.itao.ink.dao;

import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.UserRole;

import java.util.List;

@Component
public interface UserRoleMapper {
    /**
     * 插入
     * @param record 插入的对象
     * @return 是否插入成功
     */
    boolean insertSelective(UserRole record);

    /**
     * 通过主键查找
     * @param id 主键
     * @return 主键对应的UserRole对象
     */
    UserRole selectByPrimaryKey(Long id);

    /**
     * 通过主键更新
     * @param record 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateByPrimaryKeySelective(UserRole record);

    /**
     * 多种条件查找，包括id、deleted、roleId、userId等
     * 这些条件全部满足才会返回
     * @param record 条件
     * @return 满足条件的UserRole实例集合
     */
    List<UserRole> selectByNoNulProperties(UserRole record);

    /**
     * 通过userid和roleid查找
     * @param userId
     * @param roleId
     * @return
     */
    UserRole selectByUserIdAndRoleId(Long userId, Long roleId);
}