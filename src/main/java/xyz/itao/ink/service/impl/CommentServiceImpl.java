package xyz.itao.ink.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.params.UserParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.CommentService;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Service("commentService")
public class CommentServiceImpl extends AbstractBaseService<CommentDomain, CommentVo> implements CommentService {
    @Autowired
    UserService userService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    protected CommentDomain doAssemble(CommentVo vo) {
        return CommentDomain
                .builder()
                .id(vo.getId())
                .active(vo.getActive())
                .authorId(vo.getAuthorId())
                .contentId(vo.getContentId())
                .parentId(vo.getParentId())
                .status(vo.getStatus())
                .type(vo.getType())
                .userRepository(userRepository)
                .content(vo.getContent())
                .build();
    }

    @Override
    protected CommentVo doExtract(CommentDomain domain) {
        return CommentVo
                .builder()
                .id(domain.getId())
                .active(domain.getActive())
                .authorId(domain.getAuthorId())
                .contentId(domain.getContentId())
                .parentId(domain.getParentId())
                .status(domain.getStatus())
                .type(domain.getType())
                .author(domain.getAuthor())
                .mail(domain.getMail())
                .url(domain.getUrl())
                .content(domain.getContent())
                .createTime(domain.getCreateTime())
                .build();
    }

    @Override
    protected CommentDomain doUpdate(CommentDomain domain) {
        return commentRepository.updateCommentDomain(domain);
    }

    @Override
    protected CommentDomain doSave(CommentDomain domain) {
        return commentRepository.saveNewCommentDomain(domain);
    }

    @Override
    public PageInfo<CommentVo> loadAllCommentVo(CommentParam commentParam) {
        PageHelper.startPage(commentParam.getPageNum(), commentParam.getPageSize());
        List<CommentDomain> commentDomains = commentRepository.loadAllRootCommentDomain();
        List<CommentVo> commentVos = commentDomains.stream().map((d)->extract(d)).collect(Collectors.toList());
        return new PageInfo<>(commentVos);
    }

    @Override
    public PageInfo<CommentVo> loadAllActiveCommentVo(CommentParam commentParam) {
        PageHelper.startPage(commentParam.getPageNum(), commentParam.getPageSize());
        List<CommentDomain> commentDomains = commentRepository.loadAllActiveRootCommentDomain(CommentDomain.builder().contentId(commentParam.getContentId()).build());
        List<CommentVo> commentVos = commentDomains.stream().map((d)->extract(d)).collect(Collectors.toList());
        return new PageInfo<>(commentVos);
    }

    @Override
    public boolean deleteCommentById(Long id, UserVo userVo) {
        return commentRepository.deleteCommentDomainById(id, userVo.getId());
    }

    @Override
    public void updateCommentVo(CommentVo commentVo, UserVo userVo) {
        update(commentVo, userVo.getId());
    }

    @Override
    public UserVo postNewComment(CommentVo commentVo,  UserVo userVo) {
        if(userVo == null){
            userVo = UserVo
                    .builder()
                    .username(commentVo.getAuthor())
                    .email(commentVo.getMail())
                    .homeUrl(commentVo.getUrl())
                    .displayName(commentVo.getAuthor())
                    .lastLogin(DateUtils.getNow())
                    .build();
            userVo = userService.registerTemporaryUser(userVo);
        }

        commentVo.setAuthorId(userVo.getId());
        commentVo.setParentId(0L);
        save(commentVo, userVo.getId());
        return userVo;
    }
}
