package xyz.itao.ink.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import xyz.itao.ink.domain.entity.Link;
import xyz.itao.ink.domain.vo.LinkVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.LinkRepository;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
@Data
@Accessors(chain = true)
public class LinkDomain {
    LinkDomain(LinkRepository linkRepository){
        this.linkRepository = linkRepository;
    }
    LinkRepository linkRepository;
    /**
     * 连接的id
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
     * 文件上传者的id
     */
    private Long authorId;

    /**
     * 文件的原名
     */
    private String fileName;

    /**
     * 文件的种类
     */
    private String fileType;

    /**
     * 文件在文件服务器上的名称
     */
    private String fileKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建者id
     */
    private Long createBy;

    /**
     * 修改者id
     */
    private Long updateBy;
    
    public LinkDomain assemble(LinkVo vo){
        return this
                .setId(vo.getId())
                .setActive(vo.getActive())
                .setAuthorId(vo.getAuthorId())
                .setFileType(vo.getFileType())
                .setFileName(vo.getFileName())
                .setFileKey(vo.getFileKey());
    }
    
    public LinkDomain assemble(Link entity){
        return this
                .setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setAuthorId(entity.getAuthorId())
                .setFileType(entity.getFileType())
                .setFileName(entity.getFileName())
                .setFileKey(entity.getFileKey());
    }
    
    public Link entity(){
        return Link
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .active(this.getActive())
                .authorId(this.getAuthorId())
                .fileType(this.getFileType())
                .fileName(this.getFileName())
                .fileKey(this.getFileKey())
                .build();
    }
    
    public LinkVo vo(){
        return LinkVo
                .builder()
                .id(this.getId())
                .active(this.getActive())
                .authorId(this.getAuthorId())
                .fileType(this.getFileType())
                .fileName(this.getFileName())
                .fileKey(this.getFileKey())
                .build();
    }
    public LinkDomain save(){
        this
                .setId(IdUtils.nextId())
                .setActive(true)
                .setDeleted(false)
                .setCreateTime(DateUtils.getNow())
                .setUpdateTime(DateUtils.getNow());
        linkRepository.saveNewLinkDomain(this);
        return this;
    }
    public LinkDomain updateById(){
        if(id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.updateTime = DateUtils.getNow();
        linkRepository.updateLinkDomain(this);
        return this;
    }

    public LinkDomain deleteById() {
        if (id == null) {
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        this.setDeleted(true);
        this.updateById();
        return this;
    }

    public String getSubstrFileName(){
        return StringUtils.substring(this.getFileName(), 0, 12);
    }
}
