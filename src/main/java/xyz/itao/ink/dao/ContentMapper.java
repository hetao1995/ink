package xyz.itao.ink.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.entity.Content;

import java.util.List;
@Component
public interface ContentMapper {

    /**
     * 将非空数据插入到数据库
     * @param record 要插入的数据
     * @return 是否插入成功
     */
    boolean insertSelective(Content record);

    /**
     * 通过主键查找数据
     * @param id 主键id
     * @return 主键对应的Content实例
     */
    Content selectByPrimaryKey(Long id);

    /**
     * 更新数据，主键不能为空，注意，删除数据也是调用此方法，只是将deleted设置为true
     * @param record 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateByPrimaryKeySelective(Content record);

    /**
     * 多种条件查找，包括id, slug, 等
     * 这些条件全部满足才会返回
     * @param record 条件
     * @return 满足条件的Content实例
     */
    List<Content> selectByNoNulProperties(Content record);

    /**
     * 查询所有contentId在articlesid中的
     * @param articleIds
     * @param deleted
     * @param active
     * @return
     */
    List<Content> selectAllContentIn(List<Long> articleIds, boolean deleted, boolean active);

    /**
     * 查询所有文章的归档源信息
     * @param type
     * @param status
     * @return
     */
    List<Archive> selectAllContentArchive(@Param("type") String type, @Param("status") String status);

    /**
     * 查询所有crated在start和end之间的content
     * @param type
     * @param status
     * @param start
     * @param end
     * @return
     */
    List<Content> selectContentCreatedBetween(@Param("type") String type, @Param("status") String status, @Param("start") Integer start, @Param("end") Integer end);

    /**
     * 查找为type和active的content数目
     * @param type
     * @param active
     * @return
     */
    Long countContentByTypeAndActive(@Param("type") String type, @Param("active") boolean active);

    /**
     * 全文索引搜索contents
     * @param keyword
     * @param type
     * @param active
     * @return
     */
    List<Content> searchContents(@Param("keyword") String keyword, @Param("type") String type, @Param("active") boolean active);

    /**
     * 加载上一个content
     * @param created created
     * @param active active
     * @param status statuc
     * @return
     */
    Content selectNextContentDomain(@Param("created") Integer created, @Param("active") boolean active, @Param("status") String status, @Param("type") String type);

    /**
     * 加载下一个content
     * @param created created
     * @param active active
     * @param status statuc
     * @return
     */
    Content selectPrevContentDomain(@Param("created") Integer created, @Param("active") boolean active, @Param("status") String status, @Param("type") String type);
}