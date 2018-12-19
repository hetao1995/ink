package xyz.itao.ink.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xyz.itao.ink.dao.CommentMapper;
import xyz.itao.ink.domain.BaseDomain;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.entity.Comment;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.AbstractBaseRepository;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.UserRepository;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Repository("commentRepository")
public class CommentRepositoryImpl extends AbstractBaseRepository<CommentDomain, Comment> implements CommentRepository {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserRepository userRepository;

    @Override
    protected boolean doSave(Comment entity) {
        return commentMapper.insertSelective(entity);
    }

    @Override
    protected List<Comment> doLoadByNoNullProperties(Comment entity) {
        return commentMapper.selectByNoNulProperties(entity);
    }

    @Override
    protected boolean doUpdate(Comment entity) {
        return commentMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected CommentDomain doAssemble(Comment entity) {
        return CommentDomain
                .builder()
                .id(entity.getId())
                .deleted(entity.getDeleted())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .active(entity.getActive())
                .type(entity.getType())
                .status(entity.getStatus())
                .parentId(entity.getParentId())
                .contentId(entity.getContentId())
                .authorId(entity.getAuthorId())
                .userRepository(userRepository)
                .build();
    }

    @Override
    protected Comment doExtract(CommentDomain domain) {
        return Comment
                .builder()
                .id(domain.getId())
                .deleted(domain.getDeleted())
                .createTime(domain.getCreateTime())
                .createBy(domain.getCreateBy())
                .updateTime(domain.getUpdateTime())
                .updateBy(domain.getUpdateBy())
                .active(domain.getActive())
                .type(domain.getType())
                .status(domain.getStatus())
                .parentId(domain.getParentId())
                .contentId(domain.getContentId())
                .authorId(domain.getAuthorId())
                .build();
    }

    @Override
    public CommentDomain updateCommentDomain(CommentDomain domain) {
        return update(domain);
    }



    @Override
    public List<CommentDomain> loadAllActiveRootCommentDomain() {
        return loadByNoNullPropertiesActiveAndNotDelect(CommentDomain.builder().parentId(0L).build());
    }

    @Override
    public boolean deleteCommentDomainById(Long id, Long userId) {
        CommentDomain commentDomain = loadCommentDomainById(id);
        if(commentDomain==null) {
            throw new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        commentDomain.setDeleted(true);
        return update(commentDomain)==null;
    }

    @Override
    public CommentDomain saveNewCommentDomain(CommentDomain domain) {
        return save(domain);
    }

    @Override
    public CommentDomain loadCommentDomainById(Long id) {
         List<CommentDomain> commentDomains = loadByNoNullPropertiesNotDelect(CommentDomain.builder().id(id).build());
         if(commentDomains.isEmpty()){
             return null;
         }
         return commentDomains.get(0);
    }

    @Override
    public List<CommentDomain> loadAllRootCommentDomain() {
        return loadByNoNullPropertiesNotDelect(CommentDomain.builder().parentId(0L).build());
    }

}
