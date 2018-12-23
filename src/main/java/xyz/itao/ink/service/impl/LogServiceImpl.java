package xyz.itao.ink.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.LogDomain;
import xyz.itao.ink.domain.entity.Log;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LogVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.LogRepository;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.LogService;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Service("logService")
public class LogServiceImpl extends AbstractBaseService<LogDomain, LogVo> implements LogService {
    @Autowired
    LogRepository logRepository;
    @Override
    public PageInfo<LogVo> getLogs(PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<LogDomain> logDomainList = logRepository.loadAllLogs();
        List<LogVo> logVos = Lists.newArrayList();
        for(LogDomain logDomain : logDomainList){
            logVos.add(extract(logDomain));
        }
        PageInfo<LogVo> pageInfo = new PageInfo<>(logVos);
        return pageInfo;
    }

    @Override
    protected LogDomain doAssemble(LogVo vo) {
        return null;
    }

    @Override
    protected LogVo doExtract(LogDomain domain) {
        return domain.vo();
    }

    @Override
    protected LogDomain doUpdate(LogDomain domain) {
        throw  new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
    }

    @Override
    protected LogDomain doSave(LogDomain domain) {
        return logRepository.saveNewLogDomain(domain);
    }
}
