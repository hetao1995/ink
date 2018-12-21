package xyz.itao.ink.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.ContentMeta;

import java.util.List;

@Component
public interface ContentMetaMapper {
    int deleteByPrimaryKey(Long id);

    boolean insertSelective(ContentMeta record);

    ContentMeta selectByPrimaryKey(Long id);

    boolean updateByPrimaryKeySelective(ContentMeta record);

    /**
     * 通过metaId统计文章数目
     * @param metaId
     * @return
     */
    Integer countContentsByMetaId(Long metaId);

    /**
     * 通过id查找所有contentId
     * @param id
     * @return
     */
    List<Long> selectAllContentIdByMetaId(Long id);

    /**
     *
     * @param contentId
     * @param metaId
     * @return
     */
    ContentMeta selectByContentIdAndMetaId(@Param("contentId") Long contentId, @Param("metaId") Long metaId);
}