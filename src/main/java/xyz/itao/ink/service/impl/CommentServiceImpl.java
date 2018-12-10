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
    @Override
    protected CommentDomain doAssemble(CommentVo vo) {
        return CommentDomain
                .builder()
                .id(vo.getId())
                .active(vo.getActive())
                .authorId(vo.getId())
                .contentId(vo.getContentId())
                .parentId(vo.getParentId())
                .status(vo.getStatus())
                .type(vo.getType())
                .build();
    }

    @Override
    protected CommentVo doExtract(CommentDomain domain) {
        return CommentVo
                .builder()
                .id(domain.getId())
                .active(domain.getActive())
                .authorId(domain.getId())
                .contentId(domain.getContentId())
                .parentId(domain.getParentId())
                .status(domain.getStatus())
                .type(domain.getType())
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
        List<CommentDomain> commentDomains = commentRepository.loadAllActiveRootCommentDomain();
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
    public void postNewComment(CommentVo commentVo, UserParam userParam, UserVo userVo) {
        if(userVo == null){
            userVo = UserVo
                    .builder()
                    .username(userParam.getUsername())
                    .email(userParam.getEmail())
                    .homeUrl(userParam.getHomeUrl())
                    .displayName(userParam.getDisplayName())
                    .lastLogin(DateUtils.getNow())
                    .build();
            userVo = userService.registerTemporaryUser(userVo);
        }
        save(commentVo, userVo.getId());
    }
}
