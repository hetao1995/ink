package xyz.itao.ink.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.entity.ContentMeta;
import xyz.itao.ink.domain.entity.Meta;
import xyz.itao.ink.domain.params.MetaParam;
import xyz.itao.ink.domain.vo.MetaVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.Date;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Data
@Accessors(chain = true)
public class MetaDomain {
    
    MetaDomain(MetaRepository metaRepository){
        this.metaRepository = metaRepository;
    }
    
    private MetaRepository metaRepository;


    /**
     * 主键
     */
    private Long id;

    /**
     * 是否被删除
     */
    private Boolean deleted;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 父级项目
     */
    private Long parentId;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目缩略名
     */
    private String slug;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 项目详情
     */
    private String detail;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 修改者
     */
    private Long updateBy;



    public Integer getCount() {
        return metaRepository.countArticlesByMetaId(id);
    }

    public List<ContentDomain> getActivePublishArticles(){
        return metaRepository.loadAllActiveContentDomainByMetaIdAndStatus(id, TypeConst.PUBLISH);
    }

    public  MetaDomain assemble(Meta entity){
        if(entity==null){
            return null;
        }
        
        this.setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setName(entity.getName())
                .setSlug(entity.getSlug())
                .setParentId(entity.getParentId())
                .setType(entity.getType())
                .setSort(entity.getSort())
                .setDetail(entity.getDetail());
        return this;
    }
    
    public MetaDomain assemble(MetaVo vo){
        if(vo==null){
            return null;
        }
        this.setId(vo.getId())
                .setActive(vo.getActive())
                .setName(vo.getName())
                .setSlug(vo.getSlug())
                .setParentId(vo.getParentId())
                .setType(vo.getType())
                .setSort(vo.getSort())
                .setDetail(vo.getDetail());
        return this;
    }

    public MetaDomain assemble(MetaParam metaParam) {
        if(metaParam==null){
            return this;
        }
        return this.setId(metaParam.getId())
                .setName(metaParam.getName())
                .setParentId(metaParam.getParentId());

    }
    
    public Meta entity(){
        return Meta
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .active(this.getActive())
                .name(this.getName())
                .slug(this.getSlug())
                .parentId(this.getParentId())
                .type(this.getType())
                .sort(this.getSort())
                .detail(this.getDetail())
                .build();
    }

    public MetaVo vo(){
        return MetaVo
                .builder()
                .id(this.getId())
                .active(this.getActive())
                .name(this.getName())
                .slug(this.getSlug())
                .parentId(this.getParentId())
                .type(this.getType())
                .sort(this.getSort())
                .detail(this.getDetail())
                .count(this.getCount())
                .build();
    }

    public MetaDomain save(){
        this.createTime = DateUtils.getNow();
        this.updateTime = DateUtils.getNow();
        this.id = IdUtils.nextId();
        this.deleted = false;
        this.active = true;
        return metaRepository.saveNewMetaDomain(this);
    }

    public MetaDomain updateById(){
        this.updateTime = DateUtils.getNow();
        return metaRepository.updateMetaDomain(this);
    }

    public boolean saveContentMeta(Long contentId, Long operator){
        ContentMeta contentMeta = ContentMeta
                .builder()
                .id(IdUtils.nextId())
                .createTime(DateUtils.getNow())
                .updateTime(DateUtils.getNow())
                .active(true)
                .deleted(false)
                .updateBy(operator)
                .createBy(operator)
                .contentId(contentId)
                .metaId(id)
                .build();
        return metaRepository.saveNewContentMeta(contentMeta);
    }



    public MetaDomain loadById() {
        if(id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        MetaDomain metaDomain = metaRepository.loadMetaDomainById(id);
        return assemble(metaDomain.entity());
    }

    public MetaDomain deleteById() {
        if(id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.setDeleted(true);
        return this.updateById();
    }
}
