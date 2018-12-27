package xyz.itao.ink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.UserRepository;
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
public class CommentServiceImpl  implements CommentService {
    @Autowired
    UserService userService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DomainFactory domainFactory;

    @Override
    public PageInfo<CommentVo> loadAllActiveCommentVo(CommentParam commentParam) {
        Page page = PageHelper.startPage(commentParam.getPageNum(), commentParam.getPageSize(), commentParam.getOrderBy());
        List<CommentDomain> commentDomains = commentRepository.loadAllActiveCommentDomain();
        List<CommentVo> commentVos = commentDomains.stream().map((d)->d.vo()).collect(Collectors.toList());
        PageInfo<CommentVo> pageInfo = new PageInfo<>(page);
        pageInfo.setList(commentVos);
        return pageInfo;
    }


    @Override
    public void deleteCommentById(Long id, UserDomain userDomain) {
        domainFactory
                .createCommentDomain()
                .setId(id)
                .setUpdateBy(userDomain.getId())
                .deleteById();
    }

    @Override
    public void updateCommentVo(CommentVo commentVo, UserDomain userDomain) {
        domainFactory
                .createCommentDomain()
                .assemble(commentVo)
                .setUpdateBy(userDomain.getId())
                .updateById();
    }

    @Override
    public UserDomain postNewComment(CommentVo commentVo,  UserDomain userDomain) {
        if(userDomain == null){
            UserVo userVo = UserVo
                    .builder()
                    .username(null)
                    .email(commentVo.getMail())
                    .homeUrl(commentVo.getUrl())
                    .displayName(commentVo.getAuthor())
                    .lastLogin(DateUtils.getNow())
                    .build();
            userDomain = userService.registerTemporaryUser(userVo);

        }
        commentVo.setAuthorId(userDomain.getId());
        if(commentVo.getParentId()==null) {
            commentVo.setParentId(0L);
        }
        domainFactory
                .createCommentDomain()
                .assemble(commentVo)
                .setUpdateBy(userDomain.getId())
                .setCreateBy(userDomain.getId())
                .save();
        return userDomain;
    }

    @Override
    public PageInfo<CommentDomain> loadAllActiveCommentDomain(PageParam pageParam, ContentDomain contentDomain) {
        Page page = PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize(), pageParam.getOderBy());

        List<CommentDomain> commentDomains = commentRepository.loadAllActiveRootCommentDomainByContentId(contentDomain.getId());
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(commentDomains);
        return pageInfo;
    }

    @Override
    public PageInfo<CommentDomain> loadAllActiveApprovedCommentDomain(CommentParam commentParam) {
        Page page = PageHelper.startPage(commentParam.getPageNum(), commentParam.getPageSize(), commentParam.getOrderBy());
        List<CommentDomain> commentDomains = commentRepository.loadAllActiveRootCommentDomain(domainFactory.createCommentDomain().assemble(commentParam).setStatus(WebConstant.COMMENT_APPROVED));
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(page);
        pageInfo.setList(commentDomains);
        return pageInfo;
    }

}
