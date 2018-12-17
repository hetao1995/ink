package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.MetaVo;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.service.MetaService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-07
 * @description
 */
@Controller("adminArticle")
@RequestMapping("/admin/article")
@Slf4j
public class ArticleController {
    @Autowired
    private MetaService metaService;
    @Autowired
    private ContentService contentService;

    @GetMapping(value = "")
    public String index() {
        return "admin/articles";
    }

    @GetMapping("/edit/{cid}")
    public String editArticle(@PathVariable String cid) {
        return "admin/article/edit.html";
    }

    @GetMapping("/publish")
    public String newArticle(HttpServletRequest request){
        List<MetaVo> categories = metaService.getMetasByType(TypeConst.CATEGORY);
        request.setAttribute("categories", categories);
        return "admin/article/new";
    }
}
