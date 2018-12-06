package xyz.itao.ink.service;

import xyz.itao.ink.domain.vo.UserVo;

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
}
