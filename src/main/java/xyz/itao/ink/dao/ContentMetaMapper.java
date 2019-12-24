package xyz.itao.ink.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.ContentMeta;

import java.util.List;

/**
 * @author hetao
 */
@Component
public interface ContentMetaMapper {
    /**
     * 删除
     *
     * @param id 主键
     * @return 行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert
     *
     * @param record 内容
     * @return 是否成功
     */
    boolean insertSelective(ContentMeta record);

    /**
     * 通过主键查找
     *
     * @param id 主键
     * @return 结果
     */
    ContentMeta selectByPrimaryKey(Long id);

    /**
     * 通过主键更新
     *
     * @param record 内容
     * @return 更新成功
     */
    boolean updateByPrimaryKeySelective(ContentMeta record);

    /**
     * 通过metaId统计文章数目
     *
     * @param metaId metaId
     * @return 数目
     */
    Integer countContentsByMetaId(Long metaId);

    /**
     * 通过id查找所有contentId
     *
     * @param id id
     * @return contentId
     */
    List<Long> selectAllContentIdByMetaId(Long id);

    /**
     * 通过contentId和metaId查找
     *
     * @param contentId 内容id
     * @param metaId    metaId
     * @return ContentMeta
     */
    ContentMeta selectByContentIdAndMetaId(@Param("contentId") Long contentId, @Param("metaId") Long metaId);
}