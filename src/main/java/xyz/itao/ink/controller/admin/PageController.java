package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
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
@RequestMapping("/admin/article")
@Slf4j
public class PageController extends BaseController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private MetaService metaService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private SiteService siteService;

    @GetMapping("/{page}")
    public String commonPage(@PathVariable String page) {
        return "admin/" + page + ".html";
    }

    @GetMapping("/{module}/{page}")
    public String commonPage(@PathVariable String module, @PathVariable String page) {
        return "admin/" + module + "/" + page + ".html";
    }

    @GetMapping("/article/edit/:cid")
    public String editArticle(@PathVariable String cid) {
        return "admin/article/edit.html";
    }

    @GetMapping("/page/edit/:cid")
    public String editPage(@PathVariable String cid) {
        return "admin/page/edit.html";
    }

    @GetMapping("login")
    public String login(HttpServletResponse response, UserVo userVo) {
        if (null != userVo) {
            response.sendRedirect("/admin/index");
            return null;
        }
        return "admin/login";
    }

    @GetMapping("template")
    public String index(HttpServletRequest request) {
        String themePath = WebConstant.CLASSPATH + File.separatorChar + "templates" + File.separatorChar + "themes" + File.separatorChar + Commons.site_theme();
        try {
            List<String> files = Files.list(Paths.get(themePath))
                    .map(path -> path.getFileName().toString())
                    .filter(path -> path.endsWith(".html"))
                    .collect(Collectors.toList());

            List<String> partial = Files.list(Paths.get(themePath + File.separatorChar + "partial"))
                    .map(path -> path.getFileName().toString())
                    .filter(path -> path.endsWith(".html"))
                    .map(fileName -> "partial/" + fileName)
                    .collect(Collectors.toList());

            List<String> statics = Files.list(Paths.get(themePath + File.separatorChar + "static"))
                    .map(path -> path.getFileName().toString())
                    .filter(path -> path.endsWith(".js") || path.endsWith(".css"))
                    .map(fileName -> "static/" + fileName)
                    .collect(Collectors.toList());

            files.addAll(partial);
            files.addAll(statics);

            request.setAttribute("tpls", files);
        } catch (IOException e) {
            log.error("找不到模板路径");
        }
        return "admin/tpl_list";
    }

    @GetMapping("template/content")
    public String getContent(@RequestParam String fileName) {
        String content = null;
        try {
            String themePath = WebConstant.CLASSPATH + File.separatorChar + "templates" + File.separatorChar + "themes" + File.separatorChar + Commons.site_theme();
            String filePath  = themePath + File.separatorChar + fileName;
            content  = Files.readAllLines(Paths.get(filePath)).stream().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error("获取模板文件失败", e);
        }
        return content;
    }

    /**
     * 主题设置页面
     */
    @GetMapping("theme/setting")
    public String setting(HttpServletRequest request) {
        String currentTheme = Commons.site_theme();
        String key          = "theme_" + currentTheme + "_options";

        String              option = optionService.getOption(key);
        Map<String, Object> map    = new HashMap<>();
        try {
            if (StringUtils.isNotBlank(option)) {
                map = (Map<String, Object>) JsonKit.toAson(option);
            }
            request.setAttribute("options", map);
        } catch (Exception e) {
            log.error("解析主题设置出现异常", e);
        }
        request.setAttribute("theme_options", map);
        return this.render("setting");
    }

}

