package xyz.itao.ink.common;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.CommentDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.service.CommentService;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.utils.CryptoUtils;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.InkUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hetao
 * @date 2018-12-07
 * @description 主题公共函数
 */
@Component
public final class Commons {
    @Autowired
     ContentService contentService;
    @Autowired
     CommentService commentService;
    @Autowired
    Props props;

    public  static String THEME = "default";

    /**
     * 判断分页中是否有数据
     *
     * @param paginator
     * @return
     */
    public  boolean isEmpty(PageInfo paginator) {
        return paginator == null || (paginator.getList() == null) || (paginator.getList().size() == 0);
    }

    /**
     * 网站链接
     *
     * @return
     */
    public  String siteUrl() {
        return siteUrl("");
    }

    /**
     * 返回网站链接下的全址
     *
     * @param sub 后面追加的地址
     * @return
     */
    public  String siteUrl(String sub) {
        String siteUrl = siteOption(WebConstant.OPTION_SITE_URL);
        if(!siteUrl.startsWith("http")){
            siteUrl = "http://"+siteUrl;
        }
        return  siteUrl+ sub;
    }

    /**
     * 网站标题
     *
     * @return
     */
    public  String siteTitle() {
        return siteOption(WebConstant.OPTION_SITE_TITLE, "ink");
    }

    public String siteKeywords(){
        return siteOption(WebConstant.OPTION_SITE_KEYWORDS, "ink, blog");
    }

    public String siteDescription(){
        return siteOption(WebConstant.OPTION_SITE_DESCRIPTION, "a beautiful blog worth to try");
    }

    /**
     * 网站配置项
     *
     * @param key
     * @return
     */
    public  String siteOption(String key) {
        return siteOption(key, "");
    }

    /**
     * 网站配置项
     *
     * @param key
     * @param defalutValue 默认值
     * @return
     */
    public  String siteOption(String key, String defalutValue) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        return props.get(key, defalutValue);
    }

    /**
     * 截取字符串
     *
     * @param str
     * @param len
     * @return
     */
    public  String substr(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }



    /**
     * 返回github头像地址
     *
     * @param email
     * @return
     */
    public  String gravatar(String email) {
        String avatarUrl = "https://github.com/identicons/";
        if(email==null){
            email = "default";
        }
        String hash = CryptoUtils.MD5encode(email.trim().toLowerCase());
        return avatarUrl + hash + ".png";
    }

    /**
     * 返回文章链接地址
     *
     * @param contents
     * @return
     */
    public  String permalink(ContentDomain contents) {
        return permalink(contents.getId(), contents.getSlug());
    }


    /**
     * 获取随机数
     *
     * @param max
     * @param str
     * @return
     */
    public  String random(int max, String str) {
        return RandomUtils.nextInt(1, max) + str;
    }

    /**
     * 返回文章链接地址
     *
     * @param id
     * @param slug
     * @return
     */
    public  String permalink(Long id, String slug) {
        return siteUrl("/article/" + (StringUtils.isNotBlank(slug) ? slug : id.toString()));
    }

    /**
     * 格式化unix时间戳为日期
     *
     * @param unixTime
     * @return
     */
    public  String fmtDate(Integer unixTime) {
        return fmtDate(unixTime, "yyyy-MM-dd");
    }

    public  String fmtDate(Date date){
        return DateUtils.dateFormat(date,"yyyy-MM-dd" );
    }

    public String fmtDate(Date date, String format){
        return DateUtils.dateFormat(date, format);
    }
    /**
     * 格式化unix时间戳为日期
     *
     * @param unixTime
     * @param patten
     * @return
     */
    public  String fmtDate(Integer unixTime, String patten) {
        if (null != unixTime && StringUtils.isNotBlank(patten)) {
            return DateUtils.formatDateByUnixTime(unixTime, patten);
        }
        return "";
    }

    /**
     * 显示分类
     *
     * @param categories
     * @return
     */
    public  String showCategories(String categories) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(categories)) {
            String[] arr = categories.split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append("<a href=\"/category/" + URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
            }
            return sbuf.toString();
        }
        return showCategories("默认分类");
    }

    /**
     * 显示标签
     *
     * @param tags
     * @return
     */
    public  String showTags(String tags) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(tags)) {
            String[] arr = tags.split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append("<a href=\"/tag/" + URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
            }
            return sbuf.toString();
        }
        return "";
    }

    /**
     * 截取文章摘要
     *
     * @param value 文章内容
     * @param len   要截取文字的个数
     * @return
     */
    public  String intro(String value, int len) {
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return InkUtils.htmlToText(InkUtils.mdToHtml(html));
        } else {
            String text = InkUtils.htmlToText(InkUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len);
            }
            return text;
        }
    }

    /**
     * 显示文章内容，转换markdown为html
     *
     * @param value
     * @return
     */
    public  String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return InkUtils.mdToHtml(value);
        }
        return "";
    }

    /**
     * 显示文章缩略图，顺序为：文章图 -> 随机获取
     *
     * @return
     */
    public  String showThumb(ContentDomain contentDomain) {
        if(null == contentDomain){
            return "";
        }
        if(StringUtils.isNotBlank(contentDomain.getThumbImg())){
            return  "/upload/thumbnail_"+contentDomain.getThumbImg().trim();
        }
        Long cid = contentDomain.getId();
        Long size = cid % 20;
        size = size == 0 ? 1 : size;
        return "/user/img/rand/" + size + ".jpg";
    }


    /**
     * An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!
     * <p>
     * 这种格式的字符转换为emoji表情
     *
     * @param value
     * @return
     */
    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }

    private  final Pattern SRC_PATTERN = Pattern.compile("src\\s*=\\s*\'?\"?(.*?)(\'|\"|>|\\s+)");
    /**
     * 获取文章第一张图片
     *
     * @return
     */
    public  String showThumb(String content) {
        content = InkUtils.mdToHtml(content);
        if (content.contains("<img")) {
            String img = "";
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(content);
            if (m_image.find()) {
                img = img + "," + m_image.group();
                // //匹配src
                Matcher m = SRC_PATTERN.matcher(img);
                if (m.find()) {
                    return m.group(1);
                }
            }
        }
        return "";
    }

    private  final String[] ICONS = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code", "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};

    /**
     * 显示文章图标
     *
     * @param cid
     * @return
     */
    public  String showIcon(long id) {
        return ICONS[(int) (id % ICONS.length)];
    }

    /**
     * 获取社交的链接地址
     *
     * @return
     */
    public  Map<String, String> social() {
        final String prefix = "social_";
        Map<String, String> map = new HashMap<>();
        map.put("weibo", WebConstant.initConfig.get(prefix + "weibo"));
        map.put("zhihu", WebConstant.initConfig.get(prefix + "zhihu"));
        map.put("github", WebConstant.initConfig.get(prefix + "github"));
        map.put("twitter", WebConstant.initConfig.get(prefix + "twitter"));
        return map;
    }

    public  String siteTheme() {
        return siteOption("site_theme", "default");
    }

    public  String cdnUrl(){
        return siteOption(TypeConst.CDN_URL, "/admin");
    }

    public boolean allowCloudCdn(){
        return props.getBoolean(WebConstant.OPTION_ALLOW_CLOUD_CDN, false);
    }

    public  String attachUrl(){
        return attachUrl("");
    }

    public String attachUrl(String sub){
        return siteOption(TypeConst.ATTACH_URL, siteUrl()+"/upload/")+sub;
    }

    /**
     * 返回主题URL
     *
     * @return
     */
    public  String themeUrl() {
        return themeUrl("");
    }

    /**
     * 返回主题下的文件路径
     *
     * @param sub
     * @return
     */
    public  String themeUrl(String sub) {
        return siteUrl("/themes/"+Commons.THEME + sub);
    }
    /**
     * 最新文章
     * @param limit
     * @return
     */
    public  List<ContentDomain> recentArticles(int limit) {
        if (null == contentService) {
            return new ArrayList<>(0);
        }
        ArticleParam articleParam = ArticleParam.builder().orderBy("created desc").build();
        articleParam.setPageSize(limit);
        articleParam.setPageNum(1);
        return contentService.loadAllActiveContentDomain(articleParam).getList();
    }

    /**
     * 最新评论
     *
     * @param limit 查找多少个
     * @return
     */
    public  List<CommentDomain> recentComments(int limit) {
        if (null == commentService) {
            return new ArrayList<>(0);
        }
        CommentParam commentParam = CommentParam.builder().orderBy("create_time desc").build();
        commentParam.setPageNum(1);
        commentParam.setPageSize(limit);
        return commentService.loadAllActiveCommentDomain(commentParam).getList();
    }


}
