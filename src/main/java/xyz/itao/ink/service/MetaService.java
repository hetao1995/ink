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
     * @param type 类型
     * @param name
     * @param mid
     * @param userVo 操作人
     */
    void saveMeta(String type, String name, Long mid, UserVo userVo);

    /**
     * 通过id删除meta
     * @param id
     * @param userVo
     */
    void deleteMetaById(Long id, UserVo userVo);

    /**
     * 获取meta数据的映射
     * @param type
     * @return
     */
    Map<String, List<ContentVo>> getMetaMapping(String type);

    /**
     * 通过type和keyword获取meta数据
     * @param type
     * @param keyword
     * @return
     */
    MetaVo getMeta(String type, String keyword);
}
