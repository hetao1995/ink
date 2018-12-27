package xyz.itao.ink.controller.admin;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.bootstrap.InkLoader;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.dto.ThemeDto;
import xyz.itao.ink.domain.params.*;
import xyz.itao.ink.domain.vo.*;
import xyz.itao.ink.service.*;
import xyz.itao.ink.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    MetaService metaService;
    @Autowired
    OptionService optionService;
    @Autowired
    LogService logService;
    @Autowired
    CommentService commentService;
    @Autowired
    LinkService linkService;
    @Autowired
    Props props;
    @Autowired
    Commons commons;
    @GetMapping(value = "/logs")
    public RestResponse sysLogs( PageParam pageParam) {
        pageParam.setOderBy("create_time desc");
        PageInfo<LogVo> logVoPageInfo = logService.getLogs(pageParam);
        return RestResponse.ok(logVoPageInfo);
    }

    @SysLog("删除页面")
    @DeleteMapping(value = "/page/{id}")
    public RestResponse<?> deletePage(@PathVariable Long id,@RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        contentService.deleteById(id, userDomain);
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
    public RestResponse newArticle(@RequestBody ContentVo contentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        CommonValidator.valid(contentVo);
        contentVo.setType(TypeConst.ARTICLE);
        contentVo.setAuthorId(userDomain.getId());
        contentVo.setModified(DateUtils.getUnixTimeByDate(DateUtils.getNow()));
        //将点击数设初始化为0
        contentVo.setHits(0L);
        //将评论数设初始化为0
        contentVo.setCommentsNum(0L);
        if (StringUtils.isBlank(contentVo.getCategories())) {
            contentVo.setCategories("默认分类");
        }
        Long id = contentService.publishNewContent(contentVo, userDomain).getId();
        return RestResponse.ok(id);
    }

    @DeleteMapping(value = "/article/{id}")
    public RestResponse<?> deleteArticle(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        contentService.deleteById(id, userDomain);
        return RestResponse.ok();
    }

    @PutMapping(value = "/article/{id}")
    public RestResponse updateArticle(@RequestBody ContentVo contentVo, @PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        if (null == contentVo || id == null) {
            return RestResponse.fail("缺少参数，请重试");
        }

        contentVo.setId(id);
        contentVo.setModified(DateUtils.getUnixTimeByDate(DateUtils.getNow()));
        CommonValidator.valid(contentVo);
        contentService.updateContentVo(contentVo, userDomain);
        return RestResponse.ok(contentVo.getId());
    }

    @GetMapping("/articles")
    public RestResponse articleList(ArticleParam articleParam) {
        articleParam.setType(TypeConst.ARTICLE);
        articleParam.setOrderBy("create_time desc");
        System.out.println("articleParam"+articleParam);
        PageInfo<ContentVo> contentVoPageInfo = contentService.loadAllContentVo(articleParam);
        return RestResponse.ok(contentVoPageInfo);
    }

    @GetMapping("/pages")
    public RestResponse pageList(ArticleParam articleParam) {
        articleParam.setType(TypeConst.PAGE);
        articleParam.setOrderBy("create_time desc");
        PageInfo<ContentVo> contentVoPageInfo = contentService.loadAllContentVo(articleParam);
        return RestResponse.ok(contentVoPageInfo);
    }

    @SysLog("发布页面")
    @PostMapping("/page")
    public RestResponse<?> newPage( @RequestBody ContentVo contentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {

        CommonValidator.valid(contentVo);
        contentVo.setType(TypeConst.PAGE);
        contentVo.setAllowPing(true);
        contentVo.setAuthorId(userDomain.getId());
        contentVo.setModified(DateUtils.getUnixTimeByDate(DateUtils.getNow()));
        Long id = contentService.publishNewContent(contentVo, userDomain).getId();
        return RestResponse.ok(id);
    }

    @SysLog("修改页面")
    @PutMapping("/page/{id}")
    public RestResponse<?> updatePage(@PathVariable Long id, @RequestBody ContentVo contentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        CommonValidator.valid(contentVo);

        if (null == id) {
            return RestResponse.fail("缺少参数，请重试");
        }
        contentVo.setId(id);
        contentVo.setType(TypeConst.PAGE);
        contentVo.setModified(DateUtils.getUnixTimeByDate(DateUtils.getNow()));
        contentService.updateContentVo(contentVo, userDomain);
        return RestResponse.ok(id);
    }

    @SysLog("保存分类")
    @PostMapping("/category")
    public RestResponse<?> saveCategory(@RequestBody MetaParam metaParam, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        metaService.saveMeta(TypeConst.CATEGORY, metaParam, userDomain);
        return RestResponse.ok();
    }

    @SysLog("修改分类")
    @PutMapping("/category/{id}")
    public RestResponse<?> putCategory(@PathVariable Long id, @RequestBody MetaParam metaParam, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain){
        MetaDomain metaDomain = metaService.updateCategory(id, metaParam, userDomain);
        return RestResponse.ok(metaDomain.vo());
    }

    @SysLog("删除分类/标签")
    @DeleteMapping("/meta/{id}")
    public RestResponse<?> deleteMeta(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        metaService.deleteMetaById(id, userDomain);
        return RestResponse.ok();
    }



    @GetMapping("/comments")
    public RestResponse commentList(CommentParam commentParam) {
        commentParam.setOrderBy("create_time desc");
        PageInfo<CommentVo> commentsPage = commentService.loadAllActiveCommentVo(commentParam);
        return RestResponse.ok(commentsPage);
    }

    @SysLog("删除评论")
    @DeleteMapping("/comment/{id}")
    public RestResponse<?> deleteComment(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {

        commentService.deleteCommentById(id, userDomain);
        return RestResponse.ok();
    }

    @SysLog("修改评论状态")
    @PutMapping("/comment/{id}")
    public RestResponse<?> updateStatus( @RequestBody CommentVo commentVo, @PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        commentVo.setId(id);
        commentService.updateCommentVo(commentVo, userDomain);
        return RestResponse.ok();
    }

    @SysLog("回复评论")
    @PostMapping("/comment")
    public RestResponse<?> postComment(CommentVo commentVo, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        CommonValidator.valid(commentVo);
        commentService.postNewComment(commentVo,userDomain);
        return RestResponse.ok();
    }

    @GetMapping("/attach")
    public RestResponse attachList(PageParam pageParam) {

        PageInfo<LinkVo> linkVoPageInfo = linkService.loadAllActiveLinkVo(pageParam);

        return RestResponse.ok(linkVoPageInfo);
    }

    @SysLog("删除附件")
    @DeleteMapping("/attach/{id}")
    public RestResponse<?> deleteAttach(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        linkService.deleteAttachesById(id, userDomain);
        return RestResponse.ok();
    }

    @PostMapping(value = "/attach")
    @ResponseBody
    @SysLog("上传文件")
    public RestResponse<?> uploadAttach(@RequestParam("file") MultipartFile[] multipartFiles, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        if(multipartFiles==null){
            RestResponse.fail("请选择文件上传！");
        }
        List<LinkVo> uploadFiles = linkService.saveFiles(multipartFiles, userDomain);
        return RestResponse.ok(uploadFiles);
    }

    @GetMapping("/categories")
    public RestResponse categoryList() {
        List<MetaVo> categories = metaService.getMetasByType(TypeConst.CATEGORY);
        return RestResponse.ok(categories);
    }

    @GetMapping("/tags")
    public RestResponse tagList() {
        List<MetaVo> tags = metaService.getMetasByType(TypeConst.TAG);
        return RestResponse.ok(tags);
    }

    @GetMapping("/options")
    public RestResponse options() {
        Map<String, String> options = optionService.loadAllOptions();
        return RestResponse.ok(options);
    }

    @SysLog("保存系统配置")
    @PostMapping("/options")
    public RestResponse<?> saveOptions(@RequestParam Map<String, String> options, @RequestAttribute(WebConstant.LOGIN_USER)UserDomain userDomain) {
        options.forEach((k, v) -> props.set(k, v, userDomain));
        return RestResponse.ok();
    }

    @SysLog("保存高级选项设置")
    @PostMapping("/advanced")
    public RestResponse<?> saveAdvance(AdvanceParam advanceParam, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {

        // 要过过滤的黑名单列表
        if (StringUtils.isNotBlank(advanceParam.getBlockIps())) {
            props.set(TypeConst.BLOCK_IPS, advanceParam.getBlockIps(), userDomain);
            WebConstant.BLOCK_IPS.addAll(Arrays.asList(advanceParam.getBlockIps().split(",")));
        } else {
            props.set(TypeConst.BLOCK_IPS, "", userDomain);
            WebConstant.BLOCK_IPS.clear();
        }
        // 处理卸载插件
        if (StringUtils.isNotBlank(advanceParam.getPluginName())) {
            String key = "plugin_";
            // 卸载所有插件
            if (!"*".equals(advanceParam.getPluginName())) {
                key = "plugin_" + advanceParam.getPluginName();
            } else {
                props.set(TypeConst.ATTACH_URL, props.get(WebConstant.OPTION_SITE_URL, ""), userDomain);
            }
            optionService.deleteOption(key, userDomain);
        }

        if (StringUtils.isNotBlank(advanceParam.getCdnURL())) {
            props.set(WebConstant.OPTION_CDN_URL, advanceParam.getCdnURL(), userDomain);
        }

        // 是否允许重新安装
        if (StringUtils.isNotBlank(advanceParam.getAllowInstall())) {
            props.set(WebConstant.OPTION_ALLOW_INSTALL, advanceParam.getAllowInstall(), userDomain);
        }

        // 评论是否需要审核
        if (StringUtils.isNotBlank(advanceParam.getAllowCommentAudit())) {
            props.set(WebConstant.OPTION_ALLOW_COMMENT_AUDIT, advanceParam.getAllowCommentAudit(), userDomain);
        }

        // 是否允许公共资源CDN
        if (StringUtils.isNotBlank(advanceParam.getAllowCloudCDN())) {
            props.set(WebConstant.OPTION_ALLOW_CLOUD_CDN, advanceParam.getAllowCloudCDN(), userDomain);
        }
        return RestResponse.ok();
    }

    @GetMapping("themes")
    public RestResponse getThemes() {
        // 读取主题
        String         themesDir  = WebConstant.CLASSPATH + "templates"+File.separator+"themes";
        File[]         themesFile = new File(themesDir).listFiles();
        List<ThemeDto> themes     = Lists.newArrayList();
        assert themesFile != null;
        for (File f : themesFile) {
            if (f.isDirectory()) {
                ThemeDto themeDto = new ThemeDto(f.getName());
                if (Files.exists(Paths.get(f.getPath() + "/setting.html"))) {
                    themeDto.setHasSetting(true);
                }
                themes.add(themeDto);
            }
        }
        return RestResponse.ok(themes);
    }

    @SysLog("保存主题设置")
    @PostMapping("/themes")
    public RestResponse<?> saveSetting(@RequestParam Map<String, String> query, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {

        String currentTheme = props.get(WebConstant.OPTION_SITE_THEME, "default");
        String key          = "theme_" + currentTheme + "_options";

        Map<String, String> options = Maps.newHashMap();
        query.forEach(options::put);

        props.set(key, JSON.toJSONString(options), userDomain);

        return RestResponse.ok();
    }

    @SysLog("激活主题")
    @PostMapping("/themes/active")
    public RestResponse<?> activeTheme( ThemeParam themeParam, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        props.set(WebConstant.OPTION_SITE_THEME, themeParam.getSiteTheme(), userDomain);
        optionService.deleteAllThemes(userDomain);
        Commons.THEME = themeParam.getSiteTheme();

        String themePath = "/templates/themes/" + themeParam.getSiteTheme();
        try {
            InkLoader.loadTheme(themePath);
        } catch (Exception e) {
            log.error("加载主题失败！{}",e);
        }
        return RestResponse.ok();
    }

    @SysLog("保存模板")
    @PostMapping("/template")
    public RestResponse<?> saveTpl(@RequestBody TemplateParam templateParam) throws IOException {
        if (StringUtils.isBlank(templateParam.getFileName())) {
            return RestResponse.fail("缺少参数，请重试");
        }
        String content   = templateParam.getContent();
        String themePath = WebConstant.CLASSPATH + File.separatorChar + "templates" + File.separatorChar + "themes" + File.separatorChar + props.get(WebConstant.OPTION_SITE_THEME, "default");
        String filePath  = themePath + File.separatorChar + templateParam.getFileName();
        if (Files.exists(Paths.get(filePath))) {
            byte[] rf_wiki_byte = content.getBytes(StandardCharsets.UTF_8);
            Files.write(Paths.get(filePath), rf_wiki_byte);
        } else {
            Files.createFile(Paths.get(filePath));
            byte[] rf_wiki_byte = content.getBytes(StandardCharsets.UTF_8);
            Files.write(Paths.get(filePath), rf_wiki_byte);
        }
        return RestResponse.ok();
    }
}
