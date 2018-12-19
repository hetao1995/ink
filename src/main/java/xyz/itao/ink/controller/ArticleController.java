package xyz.itao.ink.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.itao.ink.annotation.StopRepeatSubmit;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.params.UserParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.CommentService;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.service.SiteService;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.InkUtils;
import xyz.itao.ink.utils.PatternUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Repeatable;
import java.net.URLEncoder;

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

    /**
     * 自定义页面,如关于页
     */
    @GetMapping(value = {"/{idOrSlug}", "/{idOrSlug}.html"})
    public String page(@PathVariable String idOrSlug, @RequestParam(value = "cp", defaultValue = "1") Integer pageNum, @RequestParam(value = "size", defaultValue = "6") Integer pageSize, HttpServletRequest request) {
        ContentVo contentVo = contentService.loadActiveContentVoByIdOrSlug(idOrSlug);
        if (null == contentVo || TypeConst.DRAFT.equals(contentVo.getSlug())) {
            return this.render_404();
        }
        setArticleAttribute(request, pageNum, pageSize, contentVo);
        contentService.hit(contentVo);
        return renderContent(request, contentVo);
    }

    /**
     * 文章页
     */
    @GetMapping(value = {"/article/{idOrSlug}", "/article/{idOrSlug}.html"})
    public String post(HttpServletRequest request, @PathVariable String idOrSlug, @RequestParam(value = "cp", defaultValue = "1") Integer pageNum, @RequestParam(value = "size", defaultValue = "6") Integer pageSize) {

        ContentVo contentVo = contentService.loadActiveContentVoByIdOrSlug(idOrSlug);
        if (null == contentVo || TypeConst.DRAFT.equals(contentVo.getStatus()) || !TypeConst.ARTICLE.equals(contentVo.getType())) {
            return this.render_404();
        }

        setArticleAttribute(request, pageNum, pageSize, contentVo);
        request.setAttribute("is_post", true);
        contentService.hit(contentVo);
        return this.render("post");
    }


    /**
     * 预览页
     * @param request
     * @return
     */
    @GetMapping(value = {"/preview/{idOrSlug}", "/preview/{idOrSlug}.html"})
    public String preview(HttpServletRequest request, @PathVariable String idOrSlug, @RequestAttribute(value = WebConstant.LOGIN_USER) UserVo userVo){
        ContentVo contentVo = contentService.loadDraftByIdOrSlug(idOrSlug, userVo);
        if(null == contentVo){
            return this.render_404();
        }
        request.setAttribute("article", contentVo);
        return renderContent(request, contentVo);
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

        if ((Boolean)WebConstant.OPTIONS.getOrDefault(WebConstant.OPTION_ALLOW_COMMENT_AUDIT, true)) {
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
     * @param contentVo
     * @return
     */
    private String renderContent(HttpServletRequest request, ContentVo contentVo) {
        if (TypeConst.ARTICLE.equals(contentVo.getType())) {
            request.setAttribute("is_post", true);
            return this.render("post");
        }
        if (TypeConst.PAGE.equals(contentVo.getType())) {
            return this.render("page");
        }
        return this.render_404();
    }

    /**
     * 设置article和comments到attribute
     * @param request
     * @param pageNum
     * @param pageSize
     * @param contentVo
     */
    private void setArticleAttribute(HttpServletRequest request,  Integer pageNum, Integer pageSize, ContentVo contentVo) {
        request.setAttribute("article", contentVo);
        CommentParam commentParam = CommentParam.builder().build();
        commentParam.setPageNum(pageNum);
        commentParam.setPageSize(pageSize);
        PageInfo<CommentVo> commentVoPageInfo = commentService.loadAllCommentVo(commentParam);
        request.setAttribute("cp", pageNum);
        request.setAttribute("comments", commentVoPageInfo);
    }

}
