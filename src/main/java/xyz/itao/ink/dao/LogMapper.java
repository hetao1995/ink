package xyz.itao.ink.dao;

import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.Log;

import java.util.List;

/**
 * @author hetao
 */
@Component
public interface LogMapper {
    /**
     * 通过主键删除
     * @param id 主键
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入
     * @param record 记录
     * @return 是否成功
     */
    boolean insert(Log record);

    /**
     * 找到所有的log
     * @return 查找结果
     */
    List<Log> selectAllLogs();
}