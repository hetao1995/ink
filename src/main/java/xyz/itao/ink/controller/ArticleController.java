package xyz.itao.ink.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.itao.ink.annotation.StopRepeatSubmit;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.params.UserParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.CommentService;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.service.SiteService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Repeatable;
import java.net.URLEncoder;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
public class ArticleController extends BaseController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SiteService siteService;

    /**
     * 自定义页面,如关于页
     */
    @GetMapping(value = {"/{id}", "/{id}.html"})
    public String page(@PathVariable Long id, HttpServletRequest request) {
        ContentVo contentVo = contentService.loadContentVoById(id);
        if (null == contentVo) {
            return this.render_404();
        }
//        if (contentVo.getAllowComment()) {
//            int cp = request.queryInt("cp", 1);
//            request.attribute("cp", cp);
//        }
        request.setAttribute("article", contentVo);
        contentService.hit(contentVo);
        if (TypeConst.ARTICLE.equals(contentVo.getType())) {
            return this.render("post");
        }
        if (TypeConst.PAGE.equals(contentVo.getType())) {
            return this.render("page");
        }
        return this.render_404();
    }

    /**
     * 文章页
     */
    @GetMapping(value = {"/article/{id}", "/article/{id}.html"})
    public String post(HttpServletRequest request, @PathVariable Long id) {
        ContentVo contentVo = contentService.loadContentVoById(id);
        if (null == contentVo) {
            return this.render_404();
        }
        if (TypeConst.DRAFT.equals(contentVo.getStatus())) {
            return this.render_404();
        }
        request.setAttribute("article", contentVo);
        request.setAttribute("is_post", true);
//        if (contentVo.getAllowComment()) {
//            int cp = request.queryInt("cp", 1);
//            request.attribute("cp", cp);
//        }
        contentService.hit(contentVo);
        return this.render("post");
    }

    /**
     * 评论操作
     */
    @SysLog("发表评论")
    @StopRepeatSubmit(key = "comment")
    @PostMapping(value = "comment")
    @ResponseBody
    public RestResponse<?> comment(HttpServletRequest request, HttpServletResponse response, CommentVo commentVo, UserVo userVo, UserParam userParam) {

//        if (StringUtils.isBlank(Referer)) {
//            return RestResponse.fail(ErrorCode.BAD_REQUEST);
//        }
//
//        if (!Referer.startsWith(Commons.site_url())) {
//            return RestResponse.fail("非法评论来源");
//        }

        CommonValidator.valid(commentVo);
//        String  val   = request.address() + ":" + comments.getCid();
//        Integer count = cache.hget(TypeConst.COMMENTS_FREQUENCY, val);
//        if (null != count && count > 0) {
//            return RestResponse.fail("您发表评论太快了，请过会再试");
//        }
//        commentVo.setIp(request.address());
//        comments.setAgent(request.userAgent());

        if ((Boolean)WebConstant.OPTIONS.getOrDefault(WebConstant.OPTION_ALLOW_COMMENT_AUDIT, true)) {
            commentVo.setStatus(WebConstant.COMMENT_NO_AUDIT);
        } else {
            commentVo.setStatus(WebConstant.COMMENT_APPROVED);
        }
        commentService.postNewComment(commentVo, userParam, userVo);
        if(userVo==null || userVo.getId()==null){
            //todo 将临时用户放入jwt
        }
        return RestResponse.ok();
    }

}
