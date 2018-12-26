package xyz.itao.ink.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.service.MetaService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Controller
public class CategoryController extends BaseController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private MetaService metaService;

    /**
     * 分类列表页
     */
    @GetMapping(value = {"/categories", "/categories.html"})
    public String categories(HttpServletRequest request) {
        Map<String, List<ContentVo>> mapping    = metaService.getMetaMapping(TypeConst.CATEGORY);
        Set<String> categories = mapping.keySet();
        request.setAttribute("categories", categories);
        request.setAttribute("mapping", mapping);
        return this.render("categories");
    }

    /**
     * 某个分类详情页
     */
    @GetMapping(value = {"/category/{keyword}", "/category/{keyword}.html"})
    public String categories(HttpServletRequest request, @PathVariable String keyword, @RequestParam(defaultValue = "12") int pageSize) {
        return this.categories(request, keyword, 1, pageSize);
    }

    /**
     * 某个分类详情页分页
     */
    @GetMapping(value = {"/category/{keyword}/{pageNum}", "/category/{keyword}/{pageNum}.html"})
    public String categories(HttpServletRequest request, @PathVariable String keyword,
                             @PathVariable int pageNum, @RequestParam(defaultValue = "12") int pageSize) {

        pageNum = pageNum < 0 || pageNum > WebConstant.MAX_PAGE ? 1 : pageNum;
        MetaDomain metaDomain = metaService.getMetaDomainByTypeAndName(TypeConst.CATEGORY, keyword);
        if (null == metaDomain) {
            return this.render_404();
        }

        PageInfo<ContentDomain> contentsPage = contentService.getPublishArticlesByMeta(metaDomain, pageNum, pageSize);

        request.setAttribute("articles", contentsPage);
        request.setAttribute("meta", metaDomain);
        request.setAttribute("type", "分类");
        request.setAttribute("keyword", keyword);
        request.setAttribute("is_category", true);
        request.setAttribute("page_prefix", "/category/" + keyword);

        return this.render("page-category");
    }

    /**
     * 标签列表页面
     * <p>
     * 渲染所有的标签和文章映射
     */
    @GetMapping(value = {"/tags", "/tags.html"})
    public String tags(HttpServletRequest request) {
        Map<String, List<ContentVo>> mapping = metaService.getMetaMapping(TypeConst.TAG);
        Set<String> tags    = mapping.keySet();
        request.setAttribute("tags", tags);
        request.setAttribute("mapping", mapping);
        return this.render("tags");
    }

    /**
     * 标签详情页
     *
     * @param name 标签名
     */
    @GetMapping(value = {"tag/{name}", "tag/{name}.html"})
    public String tagPage(HttpServletRequest request, @PathVariable String name, @RequestParam(defaultValue = "12") int limit) {
        return this.tags(request, name, 1, limit);
    }

    /**
     * 标签下文章分页
     */
    @GetMapping(value = {"tag/{name}/{pageNum}", "tag/{name}/{pageNum}.html"})
    public String tags(HttpServletRequest request, @PathVariable String name, @PathVariable int pageNum, @RequestParam(defaultValue = "12") int pageSize) {
        pageNum = pageNum < 0 || pageNum > WebConstant.MAX_PAGE ? 1 : pageNum;
        MetaDomain metaDomain = metaService.getMetaDomainByTypeAndName(TypeConst.TAG, name);
        if (null == metaDomain) {
            return this.render_404();
        }

        PageInfo<ContentDomain> contentsPage = contentService.getPublishArticlesByMeta(metaDomain, pageNum, pageSize);
        request.setAttribute("articles", contentsPage);
        request.setAttribute("meta", metaDomain);
        request.setAttribute("type", "标签");
        request.setAttribute("keyword", name);
        request.setAttribute("is_tag", true);
        request.setAttribute("page_prefix", "/tag/" + name);

        return this.render("page-category");
    }

}
