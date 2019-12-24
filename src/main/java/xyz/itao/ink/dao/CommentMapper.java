package xyz.itao.ink.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.entity.Comment;

import java.util.List;

/**
 * @author hetao
 */
@Component
public interface CommentMapper {

    /**
     * 将非空数据插入到数据库
     *
     * @param record 要插入的数据
     * @return 是否插入成功
     */
    boolean insertSelective(Comment record);

    /**
     * 通过主键查找数据
     *
     * @param id 主键id
     * @return 主键对应的Comment实例
     */
    Comment selectByPrimaryKey(Long id);

    /**
     * 更新数据，主键不能为空，注意，删除数据也是调用此方法，只是将deleted设置为true
     *
     * @param record 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateByPrimaryKeySelective(Comment record);

    /**
     * 多种条件查找，包括id, slug, 等
     * 这些条件全部满足才会返回
     *
     * @param record 条件
     * @return 满足条件的Content实例
     */
    List<Comment> selectByNoNulProperties(Comment record);

    /**
     * 统计当前comment数目
     *
     * @param active 是否active
     * @return 数目
     */
    Long countCommentByActive(boolean active);

    /**
     * 统计文章评论数
     * @param active 是否active
     * @param status 状态
     * @param  contentId 文章id
     * @return 评论数目
     */
    Long countCommentByActiveStatusAndContentId(@Param("active") boolean active, @Param("status") String status, @Param("contentId") Long contentId);
}