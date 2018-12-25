package xyz.itao.ink.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.LogDomain;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LogVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.LogRepository;
import xyz.itao.ink.service.LogService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Service("logService")
public class LogServiceImpl  implements LogService {
    @Autowired
    LogRepository logRepository;
    @Override
    public PageInfo<LogVo> getLogs(PageParam pageParam) {
        Page page = PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize(), pageParam.getOderBy());
        List<LogDomain> logDomains = logRepository.loadAllLogs();
        List<LogVo> logVos = logDomains.stream().map(LogDomain::vo).collect(Collectors.toList());
        PageInfo<LogVo> pageInfo = new PageInfo<>(page);
        pageInfo.setList(logVos);
        return pageInfo;
    }
}
