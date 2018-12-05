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
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.params.MetaParam;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.LogVo;
import xyz.itao.ink.domain.vo.UserVo;
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
        contentService.updateArticle(contentVo, userVo.getId());
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
        contentService.publish(contentVo, userVo.getId());
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("修改页面")
    @PutMapping("/pages/{id}")
    public RestResponse<?> updatePage(@PathVariable Long id, Contents contents) {
        CommonValidator.valid(contents);

        if (null == contents.getCid()) {
            return RestResponse.fail("缺少参数，请重试");
        }
        Integer cid = contents.getCid();
        contents.setType(TypeConst.PAGE);
        contentService.updateArticle(contents);
        return RestResponse.ok(cid);
    }

    @SysLog("保存分类")
    @PostMapping("/category")
    public RestResponse<?> saveCategory(MetaParam metaParam) {
        metaService.saveMeta(TypeConst.CATEGORY, metaParam.getCname(), metaParam.getMid());
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("删除分类/标签")
    @DeleteMapping("category/{id}")
    public RestResponse<?> deleteMeta(@PathVariable Long id) {
        metaService.delete(id);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping("/comments")
    public RestResponse commentList(CommentParam commentParam) {
        Users users = this.user();
        commentParam.setExcludeUID(users.getUid());

        Page<Comments> commentsPage = commentsService.findComments(commentParam);
        return RestResponse.ok(commentsPage);
    }

    @SysLog("删除评论")
    @DeleteMapping("/comments/{id}")
    public RestResponse<?> deleteComment(@PathVariable Long id) {
        Comments comments = select().from(Comments.class).byId(coid);
        if (null == comments) {
            return RestResponse.fail("不存在该评论");
        }
        commentService.delete(coid, comments.getCid());
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("修改评论状态")
    @PutMapping("/comments")
    public RestResponse<?> updateStatus(@BodyParam Comments comments) {
        comments.update();
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @SysLog("回复评论")
    @PostMapping("/comment")
    public RestResponse<?> replyComment(@BodyParam Comments comments, HttpServletRequest request) {
        CommonValidator.validAdmin(comments);

        Comments c = select().from(Comments.class).byId(comments.getCoid());
        if (null == c) {
            return RestResponse.fail("不存在该评论");
        }
        Users users = this.user();
        comments.setAuthor(users.getUsername());
        comments.setAuthorId(users.getUid());
        comments.setCid(c.getCid());
        comments.setIp(request.address());
        comments.setUrl(users.getHomeUrl());

        if (StringUtils.isNotBlank(users.getEmail())) {
            comments.setMail(users.getEmail());
        } else {
            comments.setMail("");
        }
        comments.setStatus(TaleConst.COMMENT_APPROVED);
        comments.setParent(comments.getCoid());
        commentsService.saveComment(comments);
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        return RestResponse.ok();
    }

    @GetMapping("/attaches")
    public RestResponse attachList(PageParam pageParam) {

        Page<Attach> attachPage = select().from(Attach.class)
                .order(Attach::getCreated, OrderBy.DESC)
                .page(pageParam.getPage(), pageParam.getLimit());

        return RestResponse.ok(attachPage);
    }

    @SysLog("删除附件")
    @DeleteMapping("/attaches/{id}")
    public RestResponse<?> deleteAttach(@PathVariable Long id) throws IOException {
        Attach attach = select().from(Attach.class).byId(id);
        if (null == attach) {
            return RestResponse.fail("不存在该附件");
        }
        String key = attach.getFkey();
        siteService.cleanCache(TypeConst.SYS_STATISTICS);
        String             filePath = CLASSPATH.substring(0, CLASSPATH.length() - 1) + key;
        java.nio.file.Path path     = Paths.get(filePath);
        log.info("Delete attach: [{}]", filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Anima.deleteById(Attach.class, id);
        return RestResponse.ok();
    }

    @GetMapping("/categories")
    public RestResponse categoryList() {
        List<Metas> categories = siteService.getMetas(TypeConst.RECENT_META, TypeConst.CATEGORY, TaleConst.MAX_POSTS);
        return RestResponse.ok(categories);
    }

    @GetMapping("/tags")
    public RestResponse tagList() {
        List<Metas> tags = siteService.getMetas(TypeConst.RECENT_META, TypeConst.TAG, TaleConst.MAX_POSTS);
        return RestResponse.ok(tags);
    }

    @GetMapping("/options")
    public RestResponse options() {
        Map<String, String> options = optionService.getOptions();
        return RestResponse.ok(options);
    }

    @SysLog("保存系统配置")
    @PostMapping("/options")
    public RestResponse<?> saveOptions(HttpServletRequest request) {
        Map<String, List<String>> querys = request.parameters();
        querys.forEach((k, v) -> optionService.saveOption(k, v.get(0)));
        Environment config = Environment.of(optionService.getOptions());
        TaleConst.OPTIONS = config;
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
            TaleConst.BLOCK_IPS.addAll(Arrays.asList(advanceParam.getBlockIps().split(",")));
        } else {
            optionsService.saveOption(Types.BLOCK_IPS, "");
            TaleConst.BLOCK_IPS.clear();
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
    @PostMapping("/themes/setting")
    public RestResponse<?> saveSetting(Request request) {
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
    public RestResponse<?> saveTpl(@BodyParam TemplateParam templateParam) throws IOException {
        if (StringKit.isBlank(templateParam.getFileName())) {
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
