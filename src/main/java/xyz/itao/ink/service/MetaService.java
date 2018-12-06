package xyz.itao.ink.service;

import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.MetaVo;
import xyz.itao.ink.domain.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
public interface MetaService {
    /**
     * 设置meta数据
     * @param category 类型
     * @param cname
     * @param mid
     * @param userVo 操作人
     */
    void saveMeta(String category, String cname, Integer mid, UserVo userVo);

    /**
     * 通过id删除meta
     * @param id
     * @param userVo
     */
    void deleteMetaById(Long id, UserVo userVo);

    Map<String, List<ContentVo>> getMetaMapping(String category);

    MetaVo getMeta(String category, String keyword);
}
