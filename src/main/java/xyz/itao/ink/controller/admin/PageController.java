package xyz.itao.ink.controller.admin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.vo.MetaVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.service.MetaService;
import xyz.itao.ink.service.OptionService;
import xyz.itao.ink.service.SiteService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-07
 * @description
 */
@Controller
@RequestMapping("/admin/page")
@Slf4j
public class PageController extends BaseController {

    @Autowired
    private ContentService contentService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private SiteService siteService;

    @GetMapping(value = "")
    public String index(){
        return "admin/pages";
    }

    @GetMapping("/{page}")
    public String commonPage(@PathVariable String page) {
        return "admin/" + page + ".html";
    }

    @GetMapping("/{module}/{page}")
    public String commonPage(@PathVariable String module, @PathVariable String page) {
        return "admin/" + module + "/" + page + ".html";
    }

    @GetMapping("/edit/{cid}")
    public String editPage(@PathVariable String cid) {
        return "admin/page/edit.html";
    }

    @GetMapping("/new")
    public String newPage(){
        return "admin/page/new";
    }

}
