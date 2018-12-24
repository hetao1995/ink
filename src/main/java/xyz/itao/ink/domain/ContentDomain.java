package xyz.itao.ink.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.util.set.Sets;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.entity.Comment;
import xyz.itao.ink.domain.entity.Content;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.repository.UserRepository;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
@Data
@Accessors(chain = true)
public class ContentDomain extends BaseDomain{

    ContentDomain(UserRepository userRepository, ContentRepository contentRepository, CommentRepository commentRepository, MetaRepository metaRepository, DomainFactory domainFactory){
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
        this.commentRepository = commentRepository;
        this.metaRepository =  metaRepository;
        this.domainFactory = domainFactory;
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
        if(this.getUpdateTime()==null){
            return null;
        }
        return DateUtils.getUnixTimeByDate(this.getUpdateTime());
    }

    /**
     * 获取所有激活状态的comment
     * @return
     */
    public List<CommentDomain> getActiveComments() {
        return commentRepository.loadAllActiveRootCommentDomainByContentId(id);
    }

    /**
     * 获取文章所有的comment
     * @return
     */
    public List<CommentDomain> getComments(){
        return commentRepository.loadAllActiveRootCommentDomainByContentId(id);
    }

    /**
     * 获取文章作者对象
     * @return
     */
    public UserDomain getAuthor() {
        return userRepository.loadActiveUserDomainById(authorId);
    }

    /**
     * 获取标签Meta
     * @return
     */
    public String getTags() {
        return getMetas(TypeConst.TAG);
    }

    /**
     * 存储tag
     * @param tags
     */
    private void saveTags(String tags){
        saveMetas(tags, TypeConst.TAG);
    }

    /**
     * 获取分类meta
     * @return
     */
    public String getCategories() {
        return getMetas(TypeConst.CATEGORY);
    }

    /**
     * 存储category
     * @param categories
     */
    private void saveCategories(String categories){
        saveMetas(categories, TypeConst.CATEGORY);
    }

    private void saveMetas(String metas, String type) {
        if(metas == null){
            return ;
        }
        Set<String> metaSet = Sets.newHashSet(StringUtils.split(metas, ","));
        List<MetaDomain> metaDomains = metaRepository.loadAllMetaDomainByContentIdAndType(id, type);
        for(MetaDomain metaDomain : metaDomains){
            if(!metaSet.contains(metaDomain.getName())){
                metaRepository.deleteContentMetaRelationshipByContentIdAndMetaId(id, metaDomain.getId());
            }else{
                metaSet.remove(metaDomain.getName());
            }
        }
        for(String name : metaSet){
            MetaDomain metaDomain = metaRepository.loadMetaDomainByTypeAndName(type, name);
            if(metaDomain == null){
                metaDomain = domainFactory
                        .createMetaDomain()
                        .setName(name)
                        .setActive(true)
                        .setDeleted(false)
                        .setType(type)
                        .setCreateBy(authorId)
                        .setUpdateBy(authorId)
                        .setParentId(0L);
                metaDomain = metaDomain.save();
            }
            metaDomain.saveContentMeta(id, authorId);
        }
    }

    private String getMetas(String type){
        List<MetaDomain> metaDomains = metaRepository.loadAllMetaDomainByContentIdAndType(id, type);
        return StringUtils.join(metaDomains.stream().map(MetaDomain::getName).collect(Collectors.toList()), ",");
    }

    public ContentDomain save(){
        this.createTime = DateUtils.getNow();
        this.updateTime = DateUtils.getNow();
        this.id = IdUtils.nextId();
        this.deleted = false;
        ContentDomain contentDomain = contentRepository.saveNewContentDomain(this);
        contentDomain.saveTags(this.tags);
        contentDomain.saveCategories(this.categories);
        return contentDomain;
    }

    public ContentDomain updateById(){
        if(id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.updateTime = DateUtils.getNow();
        ContentDomain contentDomain = contentRepository.updateContentDomain(this);
        contentDomain.saveTags(this.tags);
        contentDomain.saveCategories(this.categories);
        return contentDomain;
    }

    public ContentDomain deleteById(){
        if(id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.setDeleted(true);
        return this.updateById();
    }

    public ContentDomain loadById(){
        if(id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        ContentDomain contentDomain = contentRepository.loadContentDomainById(id);
        return assemble(contentDomain.entity());
    }

    public  ContentDomain assemble(Content entity){
        if(entity==null){
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

    public  ContentDomain assemble(ContentVo vo){
        if(vo==null){
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



    public ContentDomain assemble(ArticleParam articleParam){
        if(articleParam==null){
            return this;
        }
        this.setStatus(articleParam.getStatus())
                .setType(articleParam.getType())
                .setCategories(articleParam.getCategories())
                .setTitle(articleParam.getTitle());
        return this;
    }

    public  Content entity(){
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
                .build();
    }

    public ContentVo vo(){
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
}
