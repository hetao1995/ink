package xyz.itao.ink.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.itao.ink.annotation.StopRepeatSubmit;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.CommentService;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.service.SiteService;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.InkUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Controller
public class ArticleController extends BaseController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private UserService userService;

    @Autowired
    private Props props;

    /**
     * 自定义页面,如关于页
     */
    @GetMapping(value = {"/{idOrSlug}", "/{idOrSlug}.html"})
    public String page(@PathVariable String idOrSlug, @RequestParam(value = "cp", defaultValue = "1") Integer pageNum, @RequestParam(value = "size", defaultValue = "6") Integer pageSize, HttpServletRequest request) {
        ContentDomain contentDomain = contentService.loadActiveContentDomainByIdOrSlug(idOrSlug);
        if (null == contentDomain || TypeConst.DRAFT.equals(contentDomain.getStatus())) {
            return this.render_404();
        }
        setArticleAttribute(request, pageNum, pageSize, contentDomain);
        contentService.hit(contentDomain);
        return renderContent(request, contentDomain);
    }

    /**
     * 文章页
     */
    @GetMapping(value = {"/article/{idOrSlug}", "/article/{idOrSlug}.html"})
    public String post(HttpServletRequest request, @PathVariable String idOrSlug, @RequestParam(value = "cp", defaultValue = "1") Integer pageNum, @RequestParam(value = "size", defaultValue = "6") Integer pageSize) {

        ContentDomain contentDomain = contentService.loadActiveContentDomainByIdOrSlug(idOrSlug);
        if (null == contentDomain || TypeConst.DRAFT.equals(contentDomain.getStatus()) || !TypeConst.ARTICLE.equals(contentDomain.getType())) {
            return this.render_404();
        }

        setArticleAttribute(request, pageNum, pageSize, contentDomain);
        request.setAttribute("is_post", true);
        contentService.hit(contentDomain);
        return this.render("post");
    }


    /**
     * 预览页
     * @param request
     * @return
     */
    @GetMapping(value = {"/preview/{idOrSlug}", "/preview/{idOrSlug}.html"})
    public String preview(HttpServletRequest request, @PathVariable String idOrSlug, @RequestAttribute(value = WebConstant.LOGIN_USER) UserVo userVo){
        ContentDomain contentDomain = contentService.loadDraftByIdOrSlug(idOrSlug, userVo);
        if(null == contentDomain){
            return this.render_404();
        }
        request.setAttribute("article", contentDomain);
        return renderContent(request, contentDomain);
    }

    /**
     * 评论操作
     */
    @SysLog("发表评论")
    @StopRepeatSubmit(key = "comment")
    @PostMapping(value = "comment")
    @ResponseBody
    public RestResponse<?> comment(HttpServletRequest request, HttpServletResponse response,   CommentVo commentVo, @RequestAttribute(required = false,value = "login_user") UserVo userVo) {

        CommonValidator.valid(commentVo);

        if (props.getBoolean(WebConstant.OPTION_ALLOW_COMMENT_AUDIT, true)) {
            commentVo.setStatus(WebConstant.COMMENT_NO_AUDIT);
            commentVo.setActive(false);
        } else {
            commentVo.setStatus(WebConstant.COMMENT_APPROVED);
            commentVo.setActive(true);
        }

        UserVo newUserVo = commentService.postNewComment(commentVo,userVo);
        if(userVo==null || userVo.getId()==null){
            // 将临时用户jwt放入cookie
            String jwt = userService.getJwtLoginToken(newUserVo, true);
            InkUtils.setCookie(response, WebConstant.AUTHORIZATION, jwt);
        }
        return RestResponse.ok();
    }

    /**
     * 根据type返回视图
     * @param request
     * @param contentDomain
     * @return
     */
    private String renderContent(HttpServletRequest request, ContentDomain contentDomain) {
        if (TypeConst.ARTICLE.equals(contentDomain.getType())) {
            request.setAttribute("is_post", true);
            return this.render("post");
        }
        if (TypeConst.PAGE.equals(contentDomain.getType())) {
            return this.render("page");
        }
        return this.render_404();
    }

    /**
     * 设置article和comments到attribute
     * @param request
     * @param pageNum
     * @param pageSize
     * @param contentDomain
     */
    private void setArticleAttribute(HttpServletRequest request,  Integer pageNum, Integer pageSize, ContentDomain contentDomain) {
        request.setAttribute("article", contentDomain);
        ArticleParam articleParam = ArticleParam.builder().build();
        articleParam.setPageNum(pageNum);
        articleParam.setPageSize(pageSize);
        articleParam.setOrderBy("create_time desc");
        PageInfo<CommentDomain> commentPage = commentService.loadAllActiveCommentDomain(articleParam, contentDomain);
        request.setAttribute("cp", pageNum);
        request.setAttribute("comments", commentPage);
    }

}
