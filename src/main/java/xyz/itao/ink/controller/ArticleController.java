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
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.service.CommentService;
import xyz.itao.ink.service.ContentService;
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
public class ArticleController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private UserService userService;

    @Autowired
    private Props props;

    /**
     * 自定义页面,如关于页
     */
    @GetMapping(value = {"/{idOrSlug}", "/{idOrSlug}.html"})
    public String page(@PathVariable String idOrSlug, @RequestParam(value = "cp", defaultValue = "1") Integer pageNum, @RequestParam(value = "size", defaultValue = "6") Integer pageSize, HttpServletRequest request) {
        ContentDomain contentDomain = contentService.loadActivePublishContentDomainByIdOrSlug(idOrSlug);
        if (null == contentDomain || TypeConst.DRAFT.equals(contentDomain.getStatus()) || !TypeConst.PAGE.equals(contentDomain.getType())) {
            return props.render404();
        }
        setArticleAttribute(request, pageNum, pageSize, contentDomain);
//        contentService.hit(contentDomain);
        return renderContent(request, contentDomain);
    }

    /**
     * 文章页
     */
    @GetMapping(value = {"/article/{idOrSlug}", "/article/{idOrSlug}.html"})
    public String post(HttpServletRequest request, HttpServletResponse response, @PathVariable String idOrSlug, @RequestParam(value = "cp", defaultValue = "1") Integer pageNum, @RequestParam(value = "size", defaultValue = "6") Integer pageSize) {

        ContentDomain contentDomain = contentService.loadActivePublishContentDomainByIdOrSlug(idOrSlug);
        if (null == contentDomain || TypeConst.DRAFT.equals(contentDomain.getStatus()) || !TypeConst.ARTICLE.equals(contentDomain.getType())) {
            return props.render404();
        }

        setArticleAttribute(request, pageNum, pageSize, contentDomain);
        request.setAttribute("is_post", true);
        // 在一段时间内访问同一文章不计算浏览
        if(!InkUtils.containsCookieName(request, contentDomain.getId().toString())){
            contentService.hit(contentDomain);
            InkUtils.setCookie(response, contentDomain.getId().toString(), null, WebConstant.PV_INTERVAL);
        }
        return props.renderTheme("post");
    }


    /**
     * 预览页
     */
    @GetMapping(value = {"/preview/{idOrSlug}", "/preview/{idOrSlug}.html"})
    public String preview(HttpServletRequest request, @PathVariable String idOrSlug, @RequestAttribute(value = WebConstant.LOGIN_USER) UserDomain userDomain){
        ContentDomain contentDomain = contentService.loadDraftByIdOrSlug(idOrSlug, userDomain);
        if(null == contentDomain){
            return props.render404();
        }
        request.setAttribute("article", contentDomain);
        return renderContent(request, contentDomain);
    }

    /**
     * 评论操作
     */
    @SysLog("发表评论")
    @StopRepeatSubmit(key = "comment",interval = 60)
    @PostMapping(value = "comment")
    @ResponseBody
    public RestResponse<?> comment(HttpServletResponse response,   CommentVo commentVo, @RequestAttribute(required = false,value = "login_user") UserDomain userDomain) {

        CommonValidator.valid(commentVo);

        if (props.getBoolean(WebConstant.OPTION_ALLOW_COMMENT_AUDIT, true)) {
            commentVo.setStatus(WebConstant.COMMENT_NO_AUDIT);
        } else {
            commentVo.setStatus(WebConstant.COMMENT_APPROVED);
        }

        UserDomain newUserDomain = commentService.postNewComment(commentVo,userDomain);
        if(userDomain==null || userDomain.getId()==null){
            // 将临时用户jwt放入cookie
            String jwt = userService.getJwtLoginToken(newUserDomain, true);
            InkUtils.setCookie(response, WebConstant.AUTHORIZATION, jwt, WebConstant.REMEMBER_ME_INTERVAL,"/");
        }
        return RestResponse.ok();
    }

    /**
     * 根据type返回视图
     */
    private String renderContent(HttpServletRequest request, ContentDomain contentDomain) {
        if (TypeConst.ARTICLE.equals(contentDomain.getType())) {
            request.setAttribute("is_post", true);
            return props.renderTheme("post");
        }
        if (TypeConst.PAGE.equals(contentDomain.getType())) {
            return props.renderTheme("page");
        }
        return props.render404();
    }

    /**
     * 设置article和comments到attribute
     */
    private void setArticleAttribute(HttpServletRequest request,  Integer pageNum, Integer pageSize, ContentDomain contentDomain) {
        request.setAttribute("article", contentDomain);
        CommentParam commentParam = CommentParam.builder().build();
        commentParam.setPageSize(pageNum);
        commentParam.setPageSize(pageSize);
        commentParam.setContentId(contentDomain.getId());
        commentParam.setOrderBy("create_time desc");

        PageInfo<CommentDomain> commentPage = commentService.loadAllActiveApprovedCommentDomain(commentParam);
        request.setAttribute("cp", pageNum);
        request.setAttribute("comments", commentPage);
    }

}
