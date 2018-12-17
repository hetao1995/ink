package xyz.itao.ink.controller.admin;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.sun.deploy.config.WinPlatform;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.bootstrap.InkLoader;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.dto.ThemeDto;
import xyz.itao.ink.domain.params.*;
import xyz.itao.ink.domain.vo.*;
import xyz.itao.ink.service.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
@RestController
@Slf4j
@RequestMapping(value = "/admin/api")
public class AdminApiController {
    @Autowired
    ContentService contentService;
    @Autowired
    SiteService siteService;
    @Autowired
    MetaService metaService;
    @Autowired
    OptionService optionService;
    @Autowired
    LogService logService;
    @Autowired
    CommentService commentService;
    @Autowired
    LinkService linkService;
    @GetMapping(value = "/logs")
    public RestResponse sysLogs(@RequestParam PageParam pageParam) {
        PageInfo<LogVo> logVoPageInfo = logService.getLogs(pageParam);
        return RestResponse.ok(logVoPageInfo);
    }

    @SysLog("删除页面")
    @DeleteMapping(value = "/page/{id}")
    public RestResponse<?> deletePage(@PathVariable Long id,@RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        contentService.deleteById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping(value = "/article/{id}")
    public RestResponse article(@PathVariable Long id) {
        ContentVo contentVo = contentService.loadContentVoById(id);
        contentVo.setContent("");
        return RestResponse.ok(contentVo);
    }

    @GetMapping(value = "/article/content/{id}")
    public String articleContent(@PathVariable Long id) {
        ContentVo contentVo = contentService.loadContentVoById(id);
        return contentVo.getContent();
    }

    @PostMapping(value = "/article")
    public RestResponse newArticle(@RequestBody ContentVo contentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        CommonValidator.valid(contentVo);

        contentVo.setType(TypeConst.ARTICLE);
        contentVo.setAuthorId(userVo.getId());
        //将点击数设初始化为0
        contentVo.setHits(0L);
        //将评论数设初始化为0
        contentVo.setCommentsNum(0L);
        if (StringUtils.isBlank(contentVo.getCategories())) {
            contentVo.setCategories("默认分类");
        }
        Long cid = contentService.publishNewContent(contentVo, userVo).getId();
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok(cid);
    }

    @DeleteMapping(value = "/article/{id}")
    public RestResponse<?> deleteArticle(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        contentService.deleteById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @PutMapping(value = "/article")
    public RestResponse updateArticle(ContentVo contentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        if (null == contentVo || contentVo.getId() == null) {
            return RestResponse.fail("缺少参数，请重试");
        }
        CommonValidator.valid(contentVo);
        contentService.updateArticle(contentVo, userVo);
        return RestResponse.ok(contentVo.getId());
    }

    @GetMapping("/articles")
    public RestResponse articleList(ArticleParam articleParam) {
        articleParam.setType(TypeConst.ARTICLE);
        articleParam.setOrderBy("create_time desc");
        PageInfo<ContentVo> contentVoPageInfo = contentService.loadAllActiveContentVo(articleParam);
        return RestResponse.ok(contentVoPageInfo);
    }

    @GetMapping("/pages")
    public RestResponse pageList(ArticleParam articleParam) {
        articleParam.setType(TypeConst.PAGE);
        articleParam.setOrderBy("create_time desc");
        PageInfo<ContentVo> contentVoPageInfo = contentService.loadAllActiveContentVo(articleParam);
        return RestResponse.ok(contentVoPageInfo);
    }

    @SysLog("发布页面")
    @PostMapping("/pages")
    public RestResponse<?> newPage( @RequestBody ContentVo contentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {

        CommonValidator.valid(contentVo);
        System.out.println("contentVo:"+contentVo);

        contentVo.setType(TypeConst.PAGE);
        contentVo.setAllowPing(true);
        contentVo.setAuthorId(userVo.getId());
        contentService.publishNewContent(contentVo, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("修改页面")
    @PutMapping("/pages/{id}")
    public RestResponse<?> updatePage(@PathVariable Long id, ContentVo contentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        CommonValidator.valid(contentVo);

        if (null == id) {
            return RestResponse.fail("缺少参数，请重试");
        }
        contentVo.setId(id);
        contentVo.setType(TypeConst.PAGE);
        contentService.updateArticle(contentVo, userVo);
        return RestResponse.ok(id);
    }

    @SysLog("保存分类")
    @PostMapping("/category")
    public RestResponse<?> saveCategory(@RequestBody MetaParam metaParam, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        metaService.saveMeta(TypeConst.CATEGORY, metaParam.getCname(), metaParam.getMid(), userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("删除分类/标签")
    @DeleteMapping("category/{id}")
    public RestResponse<?> deleteMeta(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        metaService.deleteMetaById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping("/comments")
    public RestResponse commentList(CommentParam commentParam) {

        PageInfo<CommentVo> commentsPage = commentService.loadAllCommentVo(commentParam);
        return RestResponse.ok(commentsPage);
    }

    @SysLog("删除评论")
    @DeleteMapping("/comments/{id}")
    public RestResponse<?> deleteComment(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {

        boolean commentVo = commentService.deleteCommentById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("修改评论状态")
    @PutMapping("/comments")
    public RestResponse<?> updateStatus( CommentVo commentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        commentService.updateCommentVo(commentVo, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("回复评论")
    @PostMapping("/comment")
    public RestResponse<?> postComment(CommentVo commentVo, UserParam userParam, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        CommonValidator.valid(commentVo);
        commentService.postNewComment(commentVo, userParam, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping("/attach")
    public RestResponse attachList(PageParam pageParam) {

        PageInfo<LinkVo> linkVoPageInfo = linkService.loadAllActiveLinkVo(pageParam);

        return RestResponse.ok(linkVoPageInfo);
    }

    @SysLog("删除附件")
    @DeleteMapping("/attach/{id}")
    public RestResponse<?> deleteAttach(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        linkService.deleteAttachesById(id, userVo);
        return RestResponse.ok();
    }

    @PostMapping(value = "/attach")
    @ResponseBody
    @SysLog("上传文件")
    public RestResponse<?> uploadAttach(@RequestParam("file") MultipartFile[] multipartFiles, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        if(multipartFiles==null){
            RestResponse.fail("请选择文件上传！");
        }
        List<LinkVo> uploadFiles = linkService.saveFiles(multipartFiles, userVo);
        return RestResponse.ok(uploadFiles);
    }

    @GetMapping("/categories")
    public RestResponse categoryList() {
        List<MetaVo> categories = siteService.getMetaVo(TypeConst.RECENT_META, TypeConst.CATEGORY, WebConstant.MAX_POSTS);
        return RestResponse.ok(categories);
    }

    @GetMapping("/tags")
    public RestResponse tagList() {
        List<MetaVo> tags = siteService.getMetaVo(TypeConst.RECENT_META, TypeConst.TAG, WebConstant.MAX_POSTS);
        return RestResponse.ok(tags);
    }

    @GetMapping("/options")
    public RestResponse options() {
        Map<String, String> options = optionService.loadAllOptions();
        return RestResponse.ok(options);
    }

    @SysLog("保存系统配置")
    @PostMapping("/options")
    public RestResponse<?> saveOptions(Map<String, List<String>> options) {
        options.forEach((k, v) -> optionService.saveOption(k, v.get(0)));
        return RestResponse.ok();
    }

    @SysLog("保存高级选项设置")
    @PostMapping("/advanced")
    public RestResponse<?> saveAdvance(AdvanceParam advanceParam, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        // 清除缓存
        if (StringUtils.isNotBlank(advanceParam.getCacheKey())) {
            if ("*".equals(advanceParam.getCacheKey())) {
//                cache.clean();
            } else {
//                cache.del(advanceParam.getCacheKey());
            }
        }
        // 要过过滤的黑名单列表
        if (StringUtils.isNotBlank(advanceParam.getBlockIps())) {
            optionService.saveOption(TypeConst.BLOCK_IPS, advanceParam.getBlockIps());
            WebConstant.BLOCK_IPS.addAll(Arrays.asList(advanceParam.getBlockIps().split(",")));
        } else {
            optionService.saveOption(TypeConst.BLOCK_IPS, "");
            WebConstant.BLOCK_IPS.clear();
        }
        // 处理卸载插件
        if (StringUtils.isNotBlank(advanceParam.getPluginName())) {
            String key = "plugin_";
            // 卸载所有插件
            if (!"*".equals(advanceParam.getPluginName())) {
                key = "plugin_" + advanceParam.getPluginName();
            } else {
                optionService.saveOption(TypeConst.ATTACH_URL, Commons.site_url());
            }
            optionService.deleteOption(key, userVo);
        }

        if (StringUtils.isNotBlank(advanceParam.getCdnURL())) {
            optionService.saveOption(WebConstant.OPTION_CDN_URL, advanceParam.getCdnURL());
            WebConstant.OPTIONS.put(WebConstant.OPTION_CDN_URL, advanceParam.getCdnURL());
        }

        // 是否允许重新安装
        if (StringUtils.isNotBlank(advanceParam.getAllowInstall())) {
            optionService.saveOption(WebConstant.OPTION_ALLOW_INSTALL, advanceParam.getAllowInstall());
            WebConstant.OPTIONS.put(WebConstant.OPTION_ALLOW_INSTALL, advanceParam.getAllowInstall());
        }

        // 评论是否需要审核
        if (StringUtils.isNotBlank(advanceParam.getAllowCommentAudit())) {
            optionService.saveOption(WebConstant.OPTION_ALLOW_COMMENT_AUDIT, advanceParam.getAllowCommentAudit());
            WebConstant.OPTIONS.put(WebConstant.OPTION_ALLOW_COMMENT_AUDIT, advanceParam.getAllowCommentAudit());
        }

        // 是否允许公共资源CDN
        if (StringUtils.isNotBlank(advanceParam.getAllowCloudCDN())) {
            optionService.saveOption(WebConstant.OPTION_ALLOW_CLOUD_CDN, advanceParam.getAllowCloudCDN());
            WebConstant.OPTIONS.put(WebConstant.OPTION_ALLOW_CLOUD_CDN, advanceParam.getAllowCloudCDN());
        }
        return RestResponse.ok();
    }

    @GetMapping("themes")
    public RestResponse getThemes() {
        // 读取主题
        String         themesDir  = WebConstant.CLASSPATH + "templates"+File.separator+"themes";
        File[]         themesFile = new File(themesDir).listFiles();
        List<ThemeDto> themes     = new ArrayList<>(themesFile.length);
        for (File f : themesFile) {
            if (f.isDirectory()) {
                ThemeDto themeDto = new ThemeDto(f.getName());
                if (Files.exists(Paths.get(f.getPath() + "/setting.html"))) {
                    themeDto.setHasSetting(true);
                }
                themes.add(themeDto);
                try {
//                    WebContext.blade().addStatics("/templates/themes/" + f.getName() + "/screenshot.png");
                } catch (Exception e) {
                }
            }
        }
        return RestResponse.ok(themes);
    }

    @SysLog("保存主题设置")
    @PostMapping("/themes")
    public RestResponse<?> saveSetting(Map<String, String> query) {
//        Map<String, List<String>> query = request.parameters();

        // theme_milk_options => {  }
        String currentTheme = Commons.site_theme();
        String key          = "theme_" + currentTheme + "_options";

        Map<String, String> options = new HashMap<>();
        query.forEach(options::put);

        optionService.saveOption(key, JSON.toJSONString(options));

        WebConstant.OPTIONS = optionService.loadOptions();
        return RestResponse.ok();
    }

    @SysLog("激活主题")
    @PostMapping("/themes/active")
    public RestResponse<?> activeTheme( ThemeParam themeParam, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        optionService.saveOption(WebConstant.OPTION_SITE_THEME, themeParam.getSiteTheme());
//        delete().from(Options.class).where(Options::getName).like("theme_option_%").execute();
        optionService.deleteAllThemes(userVo);
        WebConstant.OPTIONS.put(WebConstant.OPTION_SITE_THEME, themeParam.getSiteTheme());
        BaseController.THEME = "themes/" + themeParam.getSiteTheme();

        String themePath = "/templates/themes/" + themeParam.getSiteTheme();
        try {
            InkLoader.loadTheme(themePath);
        } catch (Exception e) {
        }
        return RestResponse.ok();
    }

    @SysLog("保存模板")
    @PostMapping("/template")
    public RestResponse<?> saveTpl(TemplateParam templateParam) throws IOException {
        if (StringUtils.isBlank(templateParam.getFileName())) {
            return RestResponse.fail("缺少参数，请重试");
        }
        String content   = templateParam.getContent();
        String themePath = WebConstant.CLASSPATH + File.separatorChar + "templates" + File.separatorChar + "themes" + File.separatorChar + Commons.site_theme();
        String filePath  = themePath + File.separatorChar + templateParam.getFileName();
        if (Files.exists(Paths.get(filePath))) {
            byte[] rf_wiki_byte = content.getBytes("UTF-8");
            Files.write(Paths.get(filePath), rf_wiki_byte);
        } else {
            Files.createFile(Paths.get(filePath));
            byte[] rf_wiki_byte = content.getBytes("UTF-8");
            Files.write(Paths.get(filePath), rf_wiki_byte);
        }
        return RestResponse.ok();
    }
}
