package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.StatisticsDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.*;
import xyz.itao.ink.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Controller("adminIndex")
@RequestMapping("/admin")
@Slf4j
public class IndexController {


    @Autowired
    LogService logService;
    @Autowired
    CommentService commentService;
    @Autowired
    ContentService contentService;
    @Autowired
    DomainFactory domainFactory;

    /**
     * 仪表盘
     */
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        ArticleParam articleParam = ArticleParam.builder().build();
        articleParam.setPageSize(8);
        articleParam.setPageNum(1);
        articleParam.setOrderBy("created desc");

        List<ContentVo> contentVo   = contentService.loadAllContentVo(articleParam).getList();
        StatisticsDomain statisticsDomain = domainFactory.createStatisticsDomain();

        request.setAttribute("articles", contentVo);
        request.setAttribute("statistics", statisticsDomain);
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping("/profile")
    public String profile() {
        return "/admin/profile";
    }

    @GetMapping("/comments")
    public String commentIndex(){
        return "admin/comments";
    }

    @GetMapping("/category")
    public String categoryIndex(){
        return "admin/categories";
    }

    @GetMapping("/setting")
    public String settingIndex(){
        return "admin/setting";
    }


}

