package xyz.itao.ink.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import xyz.itao.ink.domain.entity.Option;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.OptionRepository;
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
public class OptionDomain extends BaseDomain{

    OptionDomain(OptionRepository optionRepository){
        this.optionRepository = optionRepository;
    }
    private OptionRepository optionRepository;
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
     * 配置名称
     */
    private String name;

    /**
     * 配置值
     */
    private String value;

    /**
     * 配置详情
     */
    private String detail;

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

    public OptionDomain assemble(Option entity){
        return this
                .setId(entity.getId())
                .setDeleted(entity.getDeleted())
                .setCreateTime(entity.getCreateTime())
                .setCreateBy(entity.getCreateBy())
                .setUpdateTime(entity.getUpdateTime())
                .setUpdateBy(entity.getUpdateBy())
                .setActive(entity.getActive())
                .setName(entity.getName())
                .setDetail(entity.getDetail())
                .setValue(entity.getValue());
    }

    public Option entity(){
        return Option
                .builder()
                .id(this.getId())
                .deleted(this.getDeleted())
                .createTime(this.getCreateTime())
                .createBy(this.getCreateBy())
                .updateTime(this.getUpdateTime())
                .updateBy(this.getUpdateBy())
                .active(this.getActive())
                .name(this.getName())
                .detail(this.getDetail())
                .value(this.getValue())
                .build();
    }

    public OptionDomain save(){
        this.createTime = DateUtils.getNow();
        this.updateTime = DateUtils.getNow();
        this.deleted = false;
        this.id = IdUtils.nextId();
        optionRepository.saveNewOptionDomain(this);
        return this;
    }

    public OptionDomain updateById(){
        if(this.id==null){
            throw new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
        }
        optionRepository.updateOptionDomain(this);
        OptionDomain optionDomain = optionRepository.loadOptionDomainById(this.id);
        assemble(optionDomain.entity());
        return this;
    }

}
