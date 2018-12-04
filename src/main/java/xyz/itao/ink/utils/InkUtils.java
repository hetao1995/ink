package xyz.itao.ink.utils;

import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public class InkUtils {
    /**
     * markdown转换为html
     */
    public static String mdToHtml(String markdown) {
        if (StringUtils.isBlank(markdown)) {
            return "";
        }

        List<Extension> extensions = Collections.singletonList(TablesExtension.create());
        Parser parser     = Parser.builder().extensions(extensions).build();
        Node document   = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .attributeProviderFactory(context -> new LinkAttributeProvider())
                .extensions(extensions).build();

        String content = renderer.render(document);
        content = emoji(content);

        // 支持网易云音乐输出
        if (TaleConst.BCONF.getBoolean(ENV_SUPPORT_163_MUSIC, true) && content.contains(MP3_PREFIX)) {
            content = content.replaceAll(MUSIC_REG_PATTERN, MUSIC_IFRAME);
        }
        // 支持gist代码输出
        if (TaleConst.BCONF.getBoolean(ENV_SUPPORT_GIST, true) && content.contains(GIST_PREFIX_URL)) {
            content = content.replaceAll(GIST_REG_PATTERN, GIST_REPLATE_PATTERN);
        }
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
}
