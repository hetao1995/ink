package xyz.itao.ink.utils;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.vo.ContentVo;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public class InkUtils {

    /**
     * 提取html中的文字
     *
     * @param html
     * @return
     */
    public static String htmlToText(String html) {
        if (StringUtils.isNotBlank(html)) {
            return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        return "";
    }
    /**
     * markdown转换为html
     *
     * @param markdown
     * @return
     */
    public static String mdToHtml(String markdown) {
        if (StringUtils.isBlank(markdown)) {
            return "";
        }
        java.util.List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        String content = renderer.render(document);
        content = Commons.emoji(content);
        return content;
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
    /**
     * 替换HTML脚本
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }
    /**
     * 过滤XSS注入
     *
     * @param value
     * @return
     */
    public static String filterXSS(String value) {
        String cleanValue = null;
        if (value != null) {
            cleanValue = Normalizer.normalize(value, Normalizer.Form.NFD);
            // Avoid null characters
            cleanValue = cleanValue.replaceAll("\0", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");
        }
        return cleanValue;
    }

    /**
     * 获取RSS输出
     */
    public static String getRssXml(java.util.List<ContentVo> articles, String siteUrl) throws FeedException {
        Channel channel = new Channel("rss_2.0");
        channel.setTitle((String) WebConstant.OPTIONS.get("site_title"));
        channel.setLink(siteUrl);
        channel.setDescription((String) WebConstant.OPTIONS.get("site_description"));
        channel.setLanguage("zh-CN");
        java.util.List<Item> items = new ArrayList<>();
        articles.forEach(post -> {
            Item item = new Item();
            item.setTitle(post.getTitle());
            Content content = new Content();
            String  value   = article(post.getContent());

            char[] xmlChar = value.toCharArray();
            for (int i = 0; i < xmlChar.length; ++i) {
                if (xmlChar[i] > 0xFFFD) {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                } else if (xmlChar[i] < 0x20 && xmlChar[i] != 't' & xmlChar[i] != 'n' & xmlChar[i] != 'r') {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                }
            }

            value = new String(xmlChar);

            content.setValue(value);
            item.setContent(content);
            item.setLink(permalink(post.getId(), post.getSlug(), siteUrl));
            item.setPubDate(DateUtils.toDate(post.getCreated()));
            items.add(item);
        });
        channel.setItems(items);
        WireFeedOutput out = new WireFeedOutput();
        return out.outputString(channel);
    }

    private static final String SITEMAP_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<urlset xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\" xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";

    static class Url {
        String loc;
        String lastmod;

        public Url(String loc) {
            this.loc = loc;
        }
    }

    public static String getSitemapXml(List<ContentVo> articles, String siteUrl) {
        List<Url> urls = articles.stream()
                .map(c->parse(c, siteUrl))
                .collect(Collectors.toList());
        urls.add(new Url(siteUrl + "/archives"));

        String urlBody = urls.stream()
                .map(url -> {
                    String s = "<url><loc>" + url.loc + "</loc>";
                    if (null != url.lastmod) {
                        s += "<lastmod>" + url.lastmod + "</lastmod>";
                    }
                    return s + "</url>";
                })
                .collect(Collectors.joining("\n"));

        return SITEMAP_HEAD + urlBody + "</urlset>";
    }

    private static Url parse(ContentVo contentVo, String siteUrl) {
        Url url = new Url(siteUrl + "/article/" + contentVo.getId());
        url.lastmod = DateUtils.dateFormat(DateUtils.toDate(contentVo.getModified()), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return url;
    }

    public static String buildURL(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        if (!url.startsWith("http")) {
            url = "http://".concat(url);
        }
        return url;
    }

    /**
     * 返回文章链接地址
     *
     * @param id 文章主键
     * @param slug 文章链接
     * @return 文章链接地址
     */
    public static String permalink(Long id, String slug, String siteUrl) {
        return siteUrl+"/article/" + (StringUtils.isNotBlank(slug) ? slug : id.toString());
    }

    /**
     * 显示文章内容，转换markdown为html
     *
     * @param value 文章内容
     * @return html
     */
    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return mdToHtml(value);
        }
        return "";
    }

    /**
     * 清楚cookie
     * @param request
     * @param response
     */
    public static void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return;
        }
        for(Cookie cookie : cookies){
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 添加cookie
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
    }
}
