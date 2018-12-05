package xyz.itao.ink.repository;

import xyz.itao.ink.domain.LogDomain;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
public interface LogRepository {
    /**
     * 加载所有的log
     * @return 所有的log
     */
    List<LogDomain> loadAllLogs();

    /**
     * 存储logdomain
     * @param domain 需要存储的domain
     * @return 存储后的结果
     */
    LogDomain saveNewLogDomain(LogDomain domain);
}
