package xyz.itao.ink.controller;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.ArchiveDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.service.SiteService;
import xyz.itao.ink.utils.InkUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
@Controller
@Slf4j
public class IndexController extends BaseController{
    @Autowired
    ContentService contentService;
    @Autowired
    SiteService siteService;
    @Autowired
    Props props;

    /**
     * 首页
     * @param request
     * @param limit
     * @return
     */
    @GetMapping(value = "/")
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.index(request, 1, limit);
    }

    /**
     * 首页分页显示
     * @param request request
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @return
     */
    @GetMapping(value = {"/page/{pageNum}","/page/{pageNum}.html"})
    public String index(HttpServletRequest request, @PathVariable int pageNum, @RequestParam(defaultValue = "12") int pageSize) {

        if (pageNum > 1) {
            this.title(request, "第" + pageNum + "页");
        }
        ArticleParam articleParam = ArticleParam.builder().build();
        articleParam.setPageNum(pageNum);
        articleParam.setPageSize(pageSize);
        articleParam.setType(TypeConst.ARTICLE);
        articleParam.setStatus(TypeConst.PUBLISH);
        articleParam.setOrderBy("created desc");
        request.setAttribute("articles", contentService.loadAllActivePublishContentDomain(articleParam));
        request.setAttribute("is_home", true);
        return this.render("index");
    }
    /**
     * 搜索页
     *
     * @param keyword
     * @return
     */
    @GetMapping(value = {"/search/{keyword}", "/search/{keyword}.html"})
    public String search(HttpServletRequest request, @PathVariable String keyword, @RequestParam(defaultValue = "12") Integer pageSize) {
        return this.search(request, keyword, 1, pageSize);
    }

    @GetMapping(value = {"/search", "/search.html"})
    public String search(HttpServletRequest request, @RequestParam(defaultValue = "12") int pageSize) {
//        String keyword = request.query("s").orElse("");
        return this.search(request, "", 1, pageSize);
    }

    @GetMapping(value = {"/search/{keyword}/{pageNum}", "search/{keyword}/{pageNum}.html"})
    public String search(HttpServletRequest request,  @PathVariable String keyword,  @PathVariable int pageNum, @RequestParam(defaultValue = "12") int pageSize) {

        ArticleParam articleParam = ArticleParam.builder().type(TypeConst.ARTICLE).build();
        articleParam.setPageSize(pageSize);
        articleParam.setPageNum(pageNum);
        PageInfo<ContentDomain> contentDomainPageInfo = contentService.searchArticles(keyword, articleParam);
//        Page<Contents> articles = select().from(Contents.class)
//                .where(Contents::getType, Types.ARTICLE)
//                .and(Contents::getStatus, Types.PUBLISH)
//                .like(Contents::getTitle, "%" + keyword + "%")
//                .order(Contents::getCreated, OrderBy.DESC)
//                .page(page, limit);

        request.setAttribute("articles", contentDomainPageInfo);
        request.setAttribute("type", "搜索");
        request.setAttribute("keyword", keyword);
        request.setAttribute("page_prefix", "/search/" + keyword);
        return this.render("page-category");
    }

    /**
     * 归档页
     *
     * @return
     */
    @GetMapping(value = {"/archive", "/archive.html"})
    public String archives(HttpServletRequest request,  @RequestParam(defaultValue = "12") Integer pageSize) {
        return archives(request,0, pageSize);
    }

    @GetMapping(value = {"/archive/{pageNum}","/archive/{pageNum}.html"})
    public String archives(HttpServletRequest request, @PathVariable Integer pageNum, @RequestParam(defaultValue = "12") Integer pageSize){
        ArticleParam articleParam = ArticleParam
                .builder()
                .type(TypeConst.ARTICLE)
                .status(TypeConst.PUBLISH)
                .orderBy("date_str desc")
                .build();
        articleParam.setPageNum(pageNum);
        articleParam.setPageSize(pageSize);
        PageInfo<ArchiveDomain> archivePage = contentService.loadContentArchives(articleParam);
        request.setAttribute("archivePage", archivePage);
        request.setAttribute("is_archive", true);
        return this.render("archives");
    }

    /**
     * feed页
     *
     * @return
     */
    @GetMapping(value = {"/feed", "/feed.xml", "/atom.xml"})
    public void feed(HttpServletResponse response) throws IOException {
        List<ContentVo> articles = contentService.selectAllFeedArticles();
        try {
            String xml = InkUtils.getRssXml(articles, props.get(WebConstant.OPTION_SITE_URL,""),props.get(WebConstant.OPTION_SITE_TITLE, "ink"), props.get(WebConstant.OPTION_SITE_DESCRIPTION,""));
            response.setContentType("text/xml; charset=utf-8");
            response.getWriter().append(xml);
        } catch (Exception e) {
            log.error("生成 rss 失败:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * sitemap 站点地图
     *
     * @return
     */
    @GetMapping(value = {"/sitemap", "/sitemap.xml"})
    public void siteMap(HttpServletResponse response) {
        List<ContentVo> articles = contentService.selectAllFeedArticles();
        try {
            String xml = InkUtils.getSitemapXml(articles, props.get(WebConstant.OPTION_SITE_URL, ""));
            response.setContentType("text/xml; charset=utf-8");
            response.getWriter().append(xml);
        } catch (Exception e) {
            log.error("生成 sitemap 失败:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * 登录视图
     * @return
     */
    @GetMapping(value = "/login")
    public String login(){
        return "admin/login";
    }
}
