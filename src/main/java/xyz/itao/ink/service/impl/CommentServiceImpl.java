package xyz.itao.ink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.entity.Comment;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.params.PageParam;
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
    @Autowired
    DomainFactory domainFactory;
    @Override
    protected CommentDomain doAssemble(CommentVo vo) {
        return domainFactory.createCommentDomain().assemble(vo);
    }

    @Override
    protected CommentVo doExtract(CommentDomain domain) {
        return domain.vo();
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
        Page page = PageHelper.startPage(commentParam.getPageNum(), commentParam.getPageSize(), commentParam.getOrderBy());
        List<CommentDomain> commentDomains = commentRepository.loadAllRootCommentDomain();
        List<CommentVo> commentVos = commentDomains.stream().map((d)->extract(d)).collect(Collectors.toList());
        PageInfo<CommentVo> pageInfo = new PageInfo<>(page);
        pageInfo.setList(commentVos);
        return pageInfo;
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
        if(commentVo.getParentId()==null) {
            commentVo.setParentId(0L);
        }
        save(commentVo, userVo.getId());
        return userVo;
    }

    @Override
    public PageInfo<CommentDomain> loadAllActiveCommentDomain(PageParam pageParam, ContentDomain contentDomain) {
        Page page = PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<CommentDomain> commentDomains = contentDomain.getComments();
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(commentDomains);
        return pageInfo;
    }

    @Override
    public PageInfo<CommentDomain> loadAllActiveCommentDomain(CommentParam commentParam) {
        Page page = PageHelper.startPage(commentParam.getPageNum(), commentParam.getPageSize(), commentParam.getOrderBy());
        List<CommentDomain> commentDomains = commentRepository.loadAllActiveRootCommentDomain(domainFactory.createCommentDomain().assemble(commentParam));
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(commentDomains);
        return pageInfo;
    }
}
