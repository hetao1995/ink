package xyz.itao.ink.domain;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.entity.Content;
import xyz.itao.ink.domain.entity.Option;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.repository.UserRepository;

import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.EhCacheUtils;
import xyz.itao.ink.utils.IdUtils;
import xyz.itao.ink.utils.InkUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-04
 */
@Data
@Accessors(chain = true)
public class ContentDomain {

    ContentDomain(UserRepository userRepository, ContentRepository contentRepository, CommentRepository commentRepository, MetaRepository metaRepository, DomainFactory domainFactory, Props props, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
        this.commentRepository = commentRepository;
        this.metaRepository = metaRepository;
        this.domainFactory = domainFactory;
        this.props = props;
        this.cacheManager = cacheManager;
        this.cache = cacheManager.getCache(WebConstant.CONTENT_CACHE);
        this.authorCache = cacheManager.getCache(WebConstant.USER_CACHE);

    }

    /**
     * UserRepository 对象
     */
    private UserRepository userRepository;

    /**
     * commentRepository对象
     */
    private CommentRepository commentRepository;

    /**
     * metaRepository对象
     */
    private MetaRepository metaRepository;

    private DomainFactory domainFactory;

    private ContentRepository contentRepository;

    private Props props;

    private CacheManager cacheManager;

    private Cache cache;

    private Cache authorCache;
    /**
     * 内容的id
     */
    private Long id;

    /**
     * 作者的id
     */
    private Long authorId;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容缩略名
     */
    private String slug;

    /**
     * 内容类别
     */
    private String type;

    /**
     * 内容状态
     */
    private String status;

    /**
     * 点击次数
     */
    private Long hits;

    /**
     * 是否允许评论
     */
    private Boolean allowComment;

    /**
     * 内容所属评论数目
     */
    private Long commentsNum;


    /**
     * 是否允许ping
     */
    private Boolean allowPing;

    /**
     * 是否允许出现在聚合中
     */
    private Boolean allowFeed;

    /**
     * 内容文字
     */
    private String content;

    /**
     * 缩略图的地址
     */
    private String thumbImg;

    /**
     * 是那种格式的，Markdown或者html
     */
    private String fmtType;

    /**
     * 显示的创建时间戳
     */
    private Integer created;

    /**
     * 展示的修改时间戳
     */
    private Integer modified;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 被谁创建
     */
    private Long createBy;

    /**
     * 被谁修改
     */
    private Long updateBy;


    /**
     * 标签列表
     */
    private String tags;

    /**
     * 分类列表
     */
    private String categories;

    public Integer getModified() {
        if (this.getUpdateTime() == null) {
            return null;
        }
        return DateUtils.getUnixTimeByDate(this.getUpdateTime());
    }

    /**
     * 获取所有激活状态的comment
     *
     * @return comment
     */
    @SuppressWarnings("unchecked")
    public List<CommentDomain> getActiveComments() {
        String key = this.id + "active_comments";
        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper != null) {
            return (List<CommentDomain>) wrapper.get();
        }
        List<CommentDomain> contentDomains = commentRepository.loadAllActiveRootCommentDomainByContentId(id);
        cache.put(key, contentDomains);
        return contentDomains;
    }

    /**
     * 获取文章所有的comment
     *
     * @return comment
     */
    public List<CommentDomain> getComments() {
        return commentRepository.loadAllActiveRootCommentDomainByContentId(id);
    }

    /**
     * 获取文章作者对象
     *
     * @return user
     */
    public UserDomain getAuthor() {
        Cache.ValueWrapper wrapper = authorCache.get(this.authorId);
        if (wrapper != null) {
            return (UserDomain) wrapper.get();
        }
        UserDomain userDomain = userRepository.loadActiveUserDomainById(authorId);
        authorCache.put(this.authorId, userDomain);
        return userDomain;
    }

    /**
     * 获取标签Meta
     *
     * @return tag
     */
    public String getTags() {
        return getMetas(TypeConst.TAG);
    }

    /**
     * 存储tag
     *
     * @param tags tags
     */
    private void saveTags(String tags) {
        saveMetas(tags, TypeConst.TAG);
    }

    /**
     * 获取分类meta
     *
     * @return category
     */
    public String getCategories() {
        return getMetas(TypeConst.CATEGORY);
    }

    /**
     * 存储category
     *
     * @param categories category
     */
    private void saveCategories(String categories) {
        saveMetas(categories, TypeConst.CATEGORY);
    }

    private void saveMetas(String metas, String type) {
        if (metas == null) {
            return;
        }
        Set<String> metaSet = Sets.newHashSet(StringUtils.split(metas, ","));
        List<MetaDomain> metaDomains = metaRepository.loadAllMetaDomainByContentIdAndType(id, type);
        for (MetaDomain metaDomain : metaDomains) {
            if (!metaSet.contains(metaDomain.getName())) {
                metaRepository.deleteContentMetaRelationshipByContentIdAndMetaId(id, metaDomain.getId());
            } else {
                metaSet.remove(metaDomain.getName());
            }
        }
        for (String name : metaSet) {
            MetaDomain metaDomain = metaRepository.loadMetaDomainByTypeAndName(type, name);
            if (metaDomain == null) {
                metaDomain = domainFactory
                        .createMetaDomain()
                        .setName(name)
                        .setActive(true)
                        .setDeleted(false)
                        .setType(type)
                        .setCreateBy(this.updateBy)
                        .setUpdateBy(this.updateBy)
                        .setParentId(0L);
                metaDomain = metaDomain.save();
            }
            metaDomain.saveContentMeta(id, this.updateBy);
        }
        cache.put("metas:" + type + this.id, metas);
    }

    private String getMetas(String type) {
        if (id == null) {
            return null;
        }
        String key = "metas:" + type + this.id;
        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper != null) {
            return (String) wrapper.get();
        }
        List<MetaDomain> metaDomains = metaRepository.loadAllMetaDomainByContentIdAndType(id, type);
        String metas = StringUtils.join(metaDomains.stream().map(MetaDomain::getName).collect(Collectors.toList()), ",");
        cache.put(key, metas);
        return metas;
    }

    public ContentDomain save() {
        this.createTime = DateUtils.getNow();
        this.updateTime = DateUtils.getNow();
        this.id = IdUtils.nextId();
        this.deleted = false;
        this.active = true;
        this.hits = 0L;
        this.commentsNum = 0L;
        this.saveTags(this.tags);
        this.saveCategories(this.categories);
        contentRepository.saveNewContentDomain(this);

        return this;
    }

    public ContentDomain updateById() {
        if (id == null) {
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.updateTime = DateUtils.getNow();
        this.saveTags(this.tags);
        this.saveCategories(this.categories);
        this.setCommentsNum(this.getTotalComments());
        // 根据status修改commentzhuangt
        if (TypeConst.DRAFT.equals(this.getStatus())) {
            for (CommentDomain commentDomain : commentRepository.loadAllCommentDomainByContentId(this.id)) {
                commentDomain.setActive(false).updateById();
            }
        } else if (TypeConst.PUBLISH.equals(this.getStatus())) {
            for (CommentDomain commentDomain : commentRepository.loadAllCommentDomainByContentId(this.id)) {
                commentDomain.setActive(true).updateById();
            }
        }
        contentRepository.updateContentDomain(this);
        this.cacheEvict();
        return this;
    }


    public ContentDomain deleteById() {
        if (id == null) {
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.setDeleted(true);
        // 将删除的文章所有comment都设置为false
        for (CommentDomain commentDomain : commentRepository.loadAllCommentDomainByContentId(this.id)) {
            commentDomain.setActive(false).updateById();
        }
        this.cacheEvict();
        return this.updateById();
    }


    public ContentDomain loadById() {
        if (id == null) {
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        Cache.ValueWrapper wrapper = cache.get(this.id);
        ContentDomain contentDomain;
        if (wrapper != null) {
            contentDomain = (ContentDomain) wrapper.get();
        } else {
            contentDomain = contentRepository.loadContentDomainById(id);
            cache.put(this.id, contentDomain);
        }
        return assemble(Objects.requireNonNull(contentDomain).entity());
    }

    public ContentDomain assemble(Content entity) {
        if (entity == null) {
            return this;
        }

        this.setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setAllowFeed(entity.getAllowFeed())
                .setAllowPing(entity.getAllowPing())
                .setAllowComment(entity.getAllowComment())
                .setCommentsNum(entity.getCommentsNum())
                .setType(entity.getType())
                .setHits(entity.getHits())
                .setStatus(entity.getStatus())
                .setAuthorId(entity.getAuthorId())
                .setSlug(entity.getSlug())
                .setTitle(entity.getTitle())
                .setContent(entity.getContent())
                .setCreated(entity.getCreated())
                .setModified(entity.getModified())
                .setThumbImg(entity.getThumbImg())
                .setFmtType(entity.getFmtType());
        return this;
    }

    public ContentDomain assemble(ContentVo vo) {
        if (vo == null) {
            return this;
        }

        this.setId(vo.getId())
                .setActive(vo.getActive())
                .setAllowFeed(vo.getAllowFeed())
                .setAllowPing(vo.getAllowPing())
                .setAllowComment(vo.getAllowComment())
                .setCommentsNum(vo.getCommentsNum())
                .setType(vo.getType())
                .setHits(vo.getHits())
                .setStatus(vo.getStatus())
                .setAuthorId(vo.getAuthorId())
                .setSlug(vo.getSlug())
                .setTitle(vo.getTitle())
                .setContent(vo.getContent())
                .setCreated(vo.getCreated())
                .setModified(vo.getModified())
                .setCategories(vo.getCategories())
                .setTags(vo.getTags())
                .setThumbImg(vo.getThumbImg())
                .setFmtType(vo.getFmtType());
        return this;
    }


    public ContentDomain assemble(ArticleParam articleParam) {
        if (articleParam == null) {
            return this;
        }
        this.setStatus(articleParam.getStatus())
                .setType(articleParam.getType())
                .setCategories(articleParam.getCategories())
                .setTitle(articleParam.getTitle());
        return this;
    }

    public Content entity() {
        return Content
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .active(this.getActive())
                .allowFeed(this.getAllowFeed())
                .allowPing(this.getAllowPing())
                .allowComment(this.getAllowComment())
                .commentsNum(this.getCommentsNum())
                .type(this.getType())
                .hits(this.getHits())
                .status(this.getStatus())
                .authorId(this.getAuthorId())
                .slug(this.getSlug())
                .title(this.getTitle())
                .content(this.getContent())
                .created(this.getCreated())
                .modified(this.getModified())
                .thumbImg(this.getThumbImg())
                .fmtType(this.getFmtType())
                .categories(this.categories)
                .tags(this.tags)
                .build();
    }

    public ContentVo vo() {
        return ContentVo
                .builder()
                .id(this.getId())
                .active(this.getActive())
                .allowFeed(this.getAllowFeed())
                .allowPing(this.getAllowPing())
                .allowComment(this.getAllowComment())
                .commentsNum(this.getCommentsNum())
                .type(this.getType())
                .hits(this.getHits())
                .status(this.getStatus())
                .authorId(this.getAuthorId())
                .slug(this.getSlug())
                .title(this.getTitle())
                .content(this.getContent())
                .created(this.getCreated())
                .modified(this.getModified())
                .thumbImg(this.getThumbImg())
                .fmtType(this.getFmtType())
                .tags(this.getTags())
                .categories(this.getCategories())
                .build();
    }

    /**
     * 获取content转化后的html
     *
     * @return html文本
     */
    public String getContentHtml() {
        String key = "content_html:" + this.getId();
        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper != null) {
            return (String) wrapper.get();
        }
        String content = StringUtils.replaceFirst(this.getContent(), WebConstant.INTRODUCTION_SPLITTER, "\n");
        String html = getHtml(content);
        cache.put(key, html);
        return html;
    }

    private String getHtml(String content) {
        if (WebConstant.FMT_HTML.equals(this.getFmtType())) {
            return content;
        } else if (WebConstant.FMT_MARK_DOWN.equals(this.getFmtType())) {
            return InkUtils.mdToHtml(content);
        }
        return content;
    }

    /**
     * 获取摘要html
     *
     * @return introduction
     */
    public String getIntroHtml() {
        String intro = StringUtils.substring(this.getContent(), 0, props.getInt(WebConstant.OPTION_INTRO_MAX_LEN, 75));
        int pos = StringUtils.indexOf(intro, WebConstant.INTRODUCTION_SPLITTER);
        intro = pos < 0 ? intro : StringUtils.substring(intro, 0, pos);
        return getHtml(intro);
    }

    /**
     * @return 返回文章永久链接
     */
    public String getPermalink() {
        return props.getSiteUrl(WebConstant.ARTICLE_URI + "/" + (StringUtils.isNotBlank(slug) ? slug : id.toString()));
    }


    private final Pattern SRC_PATTERN = Pattern.compile("src\\s*=\\s*\'?\"?(.*?)(\'|\"|>|\\s+)");

    /**
     * @return 显示文章缩略图，顺序为：thumbImag -> 文章图 -> 随机获取
     */
    public String getThumbUrl() {
        final String imgPrefix = "<img";
        if (StringUtils.isNotBlank(this.getThumbImg())) {
            return "/upload/thumbnail_" + this.getThumbImg().trim();
        } else if (this.getContentHtml().contains(imgPrefix)) {
            String img = "";
            final String regExImg = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern pImage = Pattern.compile(regExImg, Pattern.CASE_INSENSITIVE);
            Matcher mImage = pImage.matcher(this.getContentHtml());
            if (mImage.find()) {
                img = img + "," + mImage.group();
                // 匹配src
                Matcher m = SRC_PATTERN.matcher(img);
                if (m.find()) {
                    return m.group(1);
                }
            }
        }
        long size = this.getId() % props.getInt(WebConstant.OPTION_RAND_THUMB_LEN, 20) + 1;
        return "/themes/" + props.get(WebConstant.OPTION_SITE_THEME, "default") + "/static/img/rand/" + size + ".jpg";
    }

    private final static String[] ICONS = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code", "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};

    /**
     * @return 文章图标
     */
    public String getIcon() {
        return ICONS[(int) (this.id % ICONS.length)];
    }

    /**
     * @return 显示分类
     */
    public String getCategoriesHtml() throws UnsupportedEncodingException {
        String categories = this.getCategories();
        if (StringUtils.isBlank(categories)) {
            categories = "默认分类";
        }
        String[] arr = categories.split(",");
        StringBuffer sbuf = new StringBuffer();
        for (String c : arr) {
            sbuf.append("<a href=\"" + WebConstant.CATEGORY_URI + "/" + URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
        }
        return sbuf.toString();
    }

    /**
     * @return 显示tags
     */
    public String getTagsHtml() throws UnsupportedEncodingException {
        String tags = this.getTags();
        if (StringUtils.isBlank(tags)) {
            return "";
        }
        String[] arr = tags.split(",");
        StringBuilder sbuf = new StringBuilder();
        for (String c : arr) {
            sbuf.append("<a href=\"" + WebConstant.TAG_URI + "/").append(URLEncoder.encode(c, "UTF-8")).append("\">").append(c).append("</a>");
        }
        return sbuf.toString();
    }

    public String getCreatedFmt() {
        return DateUtils.formatDateByUnixTime(this.getCreated(), "yyyy-MM-dd");
    }

    public String getModifiedFmt() {
        return DateUtils.dateFormat(this.getUpdateTime(), "yyyy/MM/dd HH:mm");
    }

    public ContentDomain getPrev() {
        String key = "prev:" + this.id;
        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper != null) {
            return (ContentDomain) wrapper.get();
        }
        ContentDomain contentDomain = contentRepository.loadPrevActivePublishContentDomain(this.getCreated(), this.type);
        cache.put(key, contentDomain);
        return contentDomain;
    }

    public ContentDomain getNext() {
        String key = "next:" + this.id;
        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper != null) {
            return (ContentDomain) wrapper.get();
        }
        ContentDomain contentDomain = contentRepository.loadNextActivePublishContentDomain(this.getCreated(), this.type);
        cache.put(key, contentDomain);
        return contentDomain;
    }

    public void cacheEvict() {
        String prev = "prev:" + this.id, next = "next:" + this.id;
        cache.evict(prev);
        cache.evict(next);
        ContentDomain p = getPrev(), n = getNext();
        if (p != null) {
            cache.evict("next:" + p.getId());
        }
        if (n != null) {
            cache.evict("prev:" + n.getId());
        }
        cache.evict(this.id);
        cache.evict("content_html:" + this.getId());
    }

    public ContentDomain hit() {
        Long hit = this.getPv();
        EhCacheUtils.put(WebConstant.PV_CACHE, this.getId(), hit + 1);
        return this;
    }

    public Long getPv() {
        Long hit = (Long) EhCacheUtils.get(WebConstant.PV_CACHE, this.getId());
        if (hit != null) {
            return hit;
        }
        hit = contentRepository.getHit(this.getId());
        if (hit == null) {
            hit = 0L;
        }
        EhCacheUtils.put(WebConstant.PV_CACHE, this.getId(), hit);
        return hit;
    }

    /**
     * 获取commentNums
     *
     * @return 评论数
     */
    public Long getTotalComments() {
        Long cn = (Long) EhCacheUtils.get(WebConstant.COMMENT_CACHE, this.getId() + "commentsNum");
        if (cn != null) {
            return cn;
        }
        cn = commentRepository.countActiveApprovedCommentByContentId(this.id);
        EhCacheUtils.put(WebConstant.COMMENT_CACHE, this.getId() + "commentsNum", cn);
        return cn;
    }
}
