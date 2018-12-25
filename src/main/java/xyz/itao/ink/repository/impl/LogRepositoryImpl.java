package xyz.itao.ink.repository.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.LogMapper;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.LogDomain;
import xyz.itao.ink.domain.entity.Log;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.LogRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Repository("logRepository")
public class LogRepositoryImpl  implements LogRepository {
    @Autowired
    LogMapper logMapper;
    @Autowired
    DomainFactory domainFactory;

    @Override
    public List<LogDomain> loadAllLogs() {
        List<Log> logs = logMapper.selectAllLogs();
        return logs.stream().map(e->domainFactory.createLogDomain().assemble(e)).collect(Collectors.toList());
    }

    @Override
    public LogDomain saveNewLogDomain(LogDomain domain) {
        logMapper.insert(domain.entity());
        return domain;
    }
}
