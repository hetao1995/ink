package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.vo.*;
import xyz.itao.ink.service.LogService;
import xyz.itao.ink.service.SiteService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Controller("adminIndex")
@RequestMapping("/admin")
@Slf4j
public class IndexController extends BaseController {


    @Autowired
    private SiteService siteService;
    @Autowired
    LogService logService;

    /**
     * 仪表盘
     */
    @GetMapping(value = {"/", "/index"})
    public String index(HttpServletRequest request) {
        List<CommentVo> commentVos   = siteService.recentComments(5);
        List<ContentVo> contentVo   = siteService.getContens(TypeConst.RECENT_ARTICLE, 5);
        StatisticsVo statisticsVo = siteService.getStatistics();

        request.setAttribute("comments", commentVos);
        request.setAttribute("articles", contentVo);
        request.setAttribute("statistics", statisticsVo);
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping("/profile")
    public String profile() {
        return "/admin/profile";
    }


}

