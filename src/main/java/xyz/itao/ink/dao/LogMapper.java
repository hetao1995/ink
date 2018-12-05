package xyz.itao.ink.dao;

import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.Log;

import java.util.List;

@Component
public interface LogMapper {
    int deleteByPrimaryKey(Long id);

    boolean insert(Log record);

    List<Log> selectAllLogs();
}