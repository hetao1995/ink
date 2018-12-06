package xyz.itao.ink.controller.admin;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.dto.ThemeDto;
import xyz.itao.ink.domain.entity.Link;
import xyz.itao.ink.domain.params.*;
import xyz.itao.ink.domain.vo.*;
import xyz.itao.ink.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static xyz.itao.ink.constant.WebConstant.CLASSPATH;

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
    public RestResponse<?> deletePage(@PathVariable Long id, UserVo userVo) {
        contentService.deleteById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping(value = "/articles/{id}")
    public RestResponse article(@PathVariable Long id) {
        ContentVo contentVo = contentService.loadContentVoById(id);
        contentVo.setContent("");
        return RestResponse.ok(contentVo);
    }

    @GetMapping(value = "/articles/content/{id}")
    public String articleContent(@PathVariable Long id) {
        ContentVo contentVo = contentService.loadContentVoById(id);
        return contentVo.getContent();
    }

    @PostMapping(value = "/articles")
    public RestResponse newArticle(ContentVo contentVo, UserVo userVo) {
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
        Long cid = contentService.publishNewContent(contentVo).getId();
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok(cid);
    }

    @DeleteMapping(value = "/articles/{id}")
    public RestResponse<?> deleteArticle(@PathVariable Long id, UserVo userVo) {
        contentService.deleteById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @PutMapping(value = "/articles/{id}")
    public RestResponse updateArticle(@PathVariable Long id, ContentVo contentVo, UserVo userVo) {
        if (null == contentVo || null == id) {
            return RestResponse.fail("缺少参数，请重试");
        }
        contentVo.setId(id);
        CommonValidator.valid(contentVo);
        contentService.updateArticle(contentVo, userVo);
        return RestResponse.ok(id);
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
    public RestResponse<?> newPage( ContentVo contentVo, UserVo userVo) {

        CommonValidator.valid(contentVo);

        contentVo.setType(TypeConst.PAGE);
        contentVo.setAllowPing(true);
        contentVo.setAuthorId(userVo.getId());
        contentService.publish(contentVo, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("修改页面")
    @PutMapping("/pages/{id}")
    public RestResponse<?> updatePage(@PathVariable Long id, ContentVo contentVo, UserVo userVo) {
        CommonValidator.valid(contentVo);

        if (null == id) {
            return RestResponse.fail("缺少参数，请重试");
        }
        contentVo.setId(id);
        contentVo.setType(TypeConst.PAGE);
        contentService.updateArticle(contentVo);
        return RestResponse.ok(id);
    }

    @SysLog("保存分类")
    @PostMapping("/category")
    public RestResponse<?> saveCategory(MetaParam metaParam, UserVo userVo) {
        metaService.saveMeta(TypeConst.CATEGORY, metaParam.getCname(), metaParam.getMid(), userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("删除分类/标签")
    @DeleteMapping("category/{id}")
    public RestResponse<?> deleteMeta(@PathVariable Long id, UserVo userVo) {
        metaService.deleteById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping("/comments")
    public RestResponse commentList(CommentParam commentParam) {

        PageInfo<CommentVo> commentsPage = commentService.findComments(commentParam);
        return RestResponse.ok(commentsPage);
    }

    @SysLog("删除评论")
    @DeleteMapping("/comments/{id}")
    public RestResponse<?> deleteComment(@PathVariable Long id, UserVo userVo) {

        CommentVo commentVo = commentService.deleteById(id, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("修改评论状态")
    @PutMapping("/comments")
    public RestResponse<?> updateStatus( CommentVo commentVo, UserVo userVo) {
        commentService.updateCommentVo(commentVo, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("回复评论")
    @PostMapping("/comment")
    public RestResponse<?> postComment(CommentVo commentVo, UserParam userParam, UserVo userVo) {
        CommonValidator.validAdmin(commentVo);
        commentService.postNewComment(commentVo, userParam, userVo);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping("/attaches")
    public RestResponse attachList(PageParam pageParam) {

        PageInfo<LinkVo> linkVoPageInfo = linkService.loadAllActiveLinkVo(pageParam);

        return RestResponse.ok(linkVoPageInfo);
    }

    @SysLog("删除附件")
    @DeleteMapping("/attaches/{id}")
    public RestResponse<?> deleteAttach(@PathVariable Long id, UserVo userVo) throws IOException {
        Attach attach = select().from(Attach.class).byId(id);
        linkService.deleteById(id, userVo);
        return RestResponse.ok();
    }

    @GetMapping("/categories")
    public RestResponse categoryList() {
        List<MetaVo> categories = siteService.getMetaVo(TypeConst.RECENT_META, TypeConst.CATEGORY, WebConstant.MAX_POSTS);
        return RestResponse.ok(categories);
    }

    @GetMapping("/tags")
    public RestResponse tagList() {
        List<MetaVo> tags = siteService.getMetaVo(TypeConst.RECENT_META, TypeConst.TAG, WebConst.MAX_POSTS);
        return RestResponse.ok(tags);
    }

    @GetMapping("/options")
    public RestResponse options() {
        Map<String, String> options = optionService.getAllOption();
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
    public RestResponse<?> saveAdvance(AdvanceParam advanceParam) {
        // 清除缓存
        if (StringUtils.isNotBlank(advanceParam.getCacheKey())) {
            if ("*".equals(advanceParam.getCacheKey())) {
                cache.clean();
            } else {
                cache.del(advanceParam.getCacheKey());
            }
        }
        // 要过过滤的黑名单列表
        if (StringUtils.isNotBlank(advanceParam.getBlockIps())) {
            optionService.saveOption(TypeConst.BLOCK_IPS, advanceParam.getBlockIps());
            WebConst.BLOCK_IPS.addAll(Arrays.asList(advanceParam.getBlockIps().split(",")));
        } else {
            optionsService.saveOption(TypeConst.BLOCK_IPS, "");
            WebConst.BLOCK_IPS.clear();
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
            optionService.deleteOption(key);
        }

        if (StringUtils.isNotBlank(advanceParam.getCdnURL())) {
            optionsService.saveOption(OPTION_CDN_URL, advanceParam.getCdnURL());
            TaleConst.OPTIONS.set(OPTION_CDN_URL, advanceParam.getCdnURL());
        }

        // 是否允许重新安装
        if (StringUtils.isNotBlank(advanceParam.getAllowInstall())) {
            optionService.saveOption(OPTION_ALLOW_INSTALL, advanceParam.getAllowInstall());
            TaleConst.OPTIONS.set(OPTION_ALLOW_INSTALL, advanceParam.getAllowInstall());
        }

        // 评论是否需要审核
        if (StringUtils.isNotBlank(advanceParam.getAllowCommentAudit())) {
            optionService.saveOption(OPTION_ALLOW_COMMENT_AUDIT, advanceParam.getAllowCommentAudit());
            TaleConst.OPTIONS.set(OPTION_ALLOW_COMMENT_AUDIT, advanceParam.getAllowCommentAudit());
        }

        // 是否允许公共资源CDN
        if (StringUtils.isNotBlank(advanceParam.getAllowCloudCDN())) {
            optionService.saveOption(OPTION_ALLOW_CLOUD_CDN, advanceParam.getAllowCloudCDN());
            TaleConst.OPTIONS.set(OPTION_ALLOW_CLOUD_CDN, advanceParam.getAllowCloudCDN());
        }
        return RestResponse.ok();
    }

    @GetMapping("themes")
    public RestResponse getThemes() {
        // 读取主题
        String         themesDir  = CLASSPATH + "templates/themes";
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
                    WebContext.blade().addStatics("/templates/themes/" + f.getName() + "/screenshot.png");
                } catch (Exception e) {
                }
            }
        }
        return RestResponse.ok(themes);
    }

    @SysLog("保存主题设置")
    @PostMapping("/themes")
    public RestResponse<?> saveSetting() {
        Map<String, List<String>> query = request.parameters();

        // theme_milk_options => {  }
        String currentTheme = Commons.site_theme();
        String key          = "theme_" + currentTheme + "_options";

        Map<String, String> options = new HashMap<>();
        query.forEach((k, v) -> options.put(k, v.get(0)));

        optionService.saveOption(key, JsonKit.toString(options));

        TaleConst.OPTIONS = Environment.of(optionsService.getOptions());
        return RestResponse.ok();
    }

    @SysLog("激活主题")
    @PostMapping("/themes/active")
    public RestResponse<?> activeTheme( ThemeParam themeParam) {
        optionsService.saveOption(OPTION_SITE_THEME, themeParam.getSiteTheme());
        delete().from(Options.class).where(Options::getName).like("theme_option_%").execute();

        TaleConst.OPTIONS.set(OPTION_SITE_THEME, themeParam.getSiteTheme());
        BaseController.THEME = "themes/" + themeParam.getSiteTheme();

        String themePath = "/templates/themes/" + themeParam.getSiteTheme();
        try {
            TaleLoader.loadTheme(themePath);
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
        String themePath = Const.CLASSPATH + File.separatorChar + "templates" + File.separatorChar + "themes" + File.separatorChar + Commons.site_theme();
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
