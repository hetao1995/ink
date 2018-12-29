package xyz.itao.ink.constant;

import com.google.common.collect.Maps;
import xyz.itao.ink.controller.admin.AdminApiController;
import xyz.itao.ink.domain.dto.PluginMenu;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hetao
 * @date 2018-12-03
 * @description
 */
public class WebConstant {
    public static Map<String, String> initConfig = Maps.newConcurrentMap();
    /**
     * 用户domain域的key
     */
    public static final String LOGIN_USER = "login_user";
    public static  String CLASSPATH;

    static {
        try {
            CLASSPATH = new File(AdminApiController.class.getResource("/").toURI().getPath()).getPath() + File.separatorChar;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static final String      REMEMBER_IN_COOKIE = "remember_me";
    public static final String      LOGIN_ERROR_COUNT  = "login_error_count";
    public static       String      LOGIN_SESSION_KEY  = "login_user";
    public static       String      REMEMBER_TOKEN     = "";
    public static       Boolean     INSTALLED          = false;
    public static       Boolean     ENABLED_CDN        = true;
    public static       Map<String, Object> OPTIONS = Maps.newConcurrentMap();
    /**
     * 上传的目录 upload 目录
     */
    public static final String UP_DIR = CLASSPATH.substring(0, CLASSPATH.length() - 1)+File.separator+"upload"+File.separator;

    /**
     * 最大页码
     */
    public static final int MAX_PAGE = 100;

    /**
     * 最大获取文章条数
     */
    public static final int MAX_POSTS = 9999;

    /**
     * 文章内容文字数限制
     */
    public static final int MAX_CONTENT_TEXT_LENGTH = 200000;
    public static final int MIN_CONTENT_TEXT_LENGTH = 1;

    /**
     * 文章标题可以输入的文字个数限制
     */
    public static final int MAX_CONTENT_TITLE_LENGTH = 200;
    public static final int MIN_CONTENT_TITLE_LENGTH = 1;

    /**
     * 评论字数限制
     */
    public static final int MAX_COMMENT_TEXT_LENGTH = 2000;
    public static final int MIN_COMMENT_TEXT_LENGTH = 1;

    /**
     * 插件菜单
     */
    public static final List<PluginMenu> PLUGIN_MENUS = new ArrayList<>();

    /**
     * 上传文件最大20M
     */
    public static Integer MAX_FILE_SIZE = 204800;

    /**
     * 要过滤的ip列表
     */
    public static final Set<String> BLOCK_IPS = new HashSet<>(16);

    public static final String SLUG_HOME        = "/";
    public static final String SLUG_ARCHIVES    = "archives";
    public static final String SLUG_CATEGRORIES = "categories";
    public static final String SLUG_TAGS        = "tags";

    /**
     * 静态资源URI
     */
    public static final String STATIC_URI = "/static";

    /**
     * 安装页面URI
     */
    public static final String INSTALL_URI = "/install";

    /**
     * 后台URI前缀
     */
    public static final String ADMIN_URI = "/admin";

    /**
     * 后台登录地址
     */
    public static final String LOGIN_URI = "/admin/login";

    public static final String ARTICLE_URI = "/article";

    public static final String CATEGORY_URI = "/category";

    public static final String TAG_URI = "/tag";

    public static final String THEME_URI = "/themes";


    /**
     * 插件菜单 Attribute Name
     */
    public static final String PLUGINS_MENU_NAME = "plugin_menus";

    public static final String ENV_SUPPORT_163_MUSIC = "app.support_163_music";
    public static final String ENV_SUPPORT_GIST      = "app.support_gist";
    public static final String MP3_PREFIX            = "[mp3:";
    public static final String MUSIC_IFRAME          = "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=350 height=106 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=0&height=88\"></iframe>";
    public static final String MUSIC_REG_PATTERN     = "\\[mp3:(\\d+)\\]";
    public static final String GIST_PREFIX_URL       = "https://gist.github.com/";
    public static final String GIST_REG_PATTERN      = "&lt;script src=\"https://gist.github.com/(\\w+)/(\\w+)\\.js\">&lt;/script>";
    public static final String GIST_REPLATE_PATTERN  = "<script src=\"https://gist.github.com/$1/$2\\.js\"></script>";


    public static final String SQL_QUERY_METAS = "select a.*, count(b.cid) as count from t_metas a left join `t_relationships` b on a.mid = b.mid " +
            "where a.type = ? and a.name = ? group by a.mid";

    public static final String SQL_QUERY_ARTICLES = "select a.* from t_contents a left join t_relationships b on a.cid = b.cid " +
            "where b.mid = ? and a.status = 'publish' and a.type = 'post' order by a.created desc";

    public static final String COMMENT_APPROVED = "approved";
    public static final String COMMENT_NO_AUDIT = "no_audit";

    public static final String OPTION_CDN_URL             = "cdn_url";
    public static final String OPTION_SITE_THEME          = "site_theme";
    public static final String OPTION_ALLOW_INSTALL       = "allow_install";
    public static final String OPTION_ALLOW_COMMENT_AUDIT = "allow_comment_audit";
    public static final String OPTION_ALLOW_CLOUD_CDN     = "allow_cloud_CDN";
    public static final String OPTION_SAFE_REMEMBER_ME    = "safe_remember_me";
    public static final String OPTION_SITE_TITLE = "site_title";
    public static final String OPTION_SITE_URL = "site_url";
    public static final String OPTION_SITE_DESCRIPTION  = "site_description";
    public static final String OPTION_SITE_KEYWORDS = "site_keywords";

    public static final int REMEMBER_ME_INTERVAL = 60*60*24*30;

    public static final String AUTHORIZATION = "Authorization";

    public static final String FMT_MARK_DOWN = "markdown";

    public static final String FMT_HTML = "html";

    public static final String INTRODUCTION_SPLITTER = "<!-- more -->";

    public static final String OPTION_INTRO_MAX_LEN = "intro_max_len";

    public static final String OPTION_DEFAULT_PROTOCOL = "default_protocol";

    public static final String OPTION_RAND_THUMB_LEN = "rand_thumb_len";

    public static final String OPTION_ATTACH_URL = "attach_url";

    public static final String OPTION_RECENT_ARTICLE_LEN = "recent_article_len";

    public static final String OPTION_RECENT_COMMENT_LEN = "recent_comment_len";

    public static final String OPTION_BLOCK_IPS = "block_ips";

    public final static String PROPS_CACHE = "props_cache";

    public final static String CONTENT_CACHE = "content_cache";

    public final static String USER_CACHE = "user_cache";

    public final static String COMMENT_CACHE = "comment_cache";

    public final static String META_CACHE = "meta_cache";

    public final static String REPEAT_SUBMIT_CACHE = "repeat_submit_cache";

    public final static String PV_CACHE = "pv_cache";
}
