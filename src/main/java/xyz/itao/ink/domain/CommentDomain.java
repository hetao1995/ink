package xyz.itao.ink.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import xyz.itao.ink.domain.entity.Comment;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Data
@Accessors(chain = true)
public class CommentDomain {

    CommentDomain(UserRepository userRepository, ContentRepository contentRepository, CommentRepository commentRepository){
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
        this.commentRepository = commentRepository;
    }
    /**
     * UserRepository 对象
     */
    private UserRepository userRepository;

    /**
     * ContentRepository 对象
     */
    private ContentRepository contentRepository;

    /**
     * CommentRepository对象
     */
    private CommentRepository commentRepository;
    /**
     * 评论的id
     */
    private Long id;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 品论所属内容
     */
    private Long contentId;

    /**
     * 评论的作者id
     */
    private Long authorId;

    /**
     * 父级评论id
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论类型
     */
    private String type;

    /**
     * 评论状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 被谁创建
     */
    private Long createBy;

    /**
     * 被谁修改
     */
    private Long updateBy;

    public UserDomain getAuthor() {
        return userRepository.loadActiveUserDomainById(authorId);
    }

    public ContentDomain getContentDomain(){
        return contentRepository.loadActiveContentDomainById(contentId);
    }

    public CommentDomain assemble(Comment entity){
        return this
                .setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setType(entity.getType())
                .setStatus(entity.getStatus())
                .setParentId(entity.getParentId())
                .setContentId(entity.getContentId())
                .setAuthorId(entity.getAuthorId())
                .setContent(entity.getContent());
    }

    public CommentDomain assemble(CommentVo vo){
        return this
                .setId(vo.getId())
                .setActive(vo.getActive())
                .setAuthorId(vo.getAuthorId())
                .setContentId(vo.getContentId())
                .setParentId(vo.getParentId())
                .setStatus(vo.getStatus())
                .setType(vo.getType())
                .setContent(vo.getContent());
    }

    public CommentDomain assemble(CommentParam param){
        return this
                .setContentId(param.getContentId())
                .setParentId(param.getParentId());
    }

    public Comment entity(){
        return Comment
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .active(this.getActive())
                .type(this.getType())
                .status(this.getStatus())
                .parentId(this.getParentId())
                .contentId(this.getContentId())
                .authorId(this.getAuthorId())
                .content(this.getContent())
                .build();
    }

    public CommentVo vo(){
        return CommentVo
                .builder()
                .id(this.getId())
                .active(this.getActive())
                .authorId(this.getAuthorId())
                .contentId(this.getContentId())
                .parentId(this.getParentId())
                .status(this.getStatus())
                .type(this.getType())
                .content(this.getContent())
                .author(this.getAuthor().getDisplayName())
                .mail(this.getAuthor().getEmail())
                .url(this.getAuthor().getHomeUrl())
                .createTime(this.getCreateTime())
                .build();
    }

    public CommentDomain save(){
        this.createTime = DateUtils.getNow();
        this.updateTime = DateUtils.getNow();
        this.id = IdUtils.nextId();
        this.active = true;
        this.deleted = false;
        return commentRepository.saveNewCommentDomain(this);
    }

    public CommentDomain updateById(){
        return commentRepository.updateCommentDomain(this);
    }

    public CommentDomain deleteById() {
        this.deleted = true;
        return updateById();
    }

    public List<CommentDomain> getChildren(){
        if(this.parentId != 0){
            return Lists.newArrayList();
        }
        return dfs(id);
    }
    private List<CommentDomain> dfs(Long pid){
        List<CommentDomain> children = commentRepository.loadAllActiveCommentDomainByParentId(pid);
        List<CommentDomain> res = Lists.newArrayList();
        for(CommentDomain child : children){
            res.add(child);
            res.addAll(dfs(child.id));
        }
        return res;
    }

    public CommentDomain getParent(){
        return commentRepository.loadCommentDomainById(this.parentId);
    }
}
