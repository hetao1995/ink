package xyz.itao.ink.domain;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.MetaRepository;

import java.util.Date;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Data
@Builder
public class MetaDomain extends BaseDomain{
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

    private MetaRepository metaRepository;

    private ContentRepository contentRepository;

    /**
     * 此meta下的文章数
     */
    private Integer count;

    public List<Long> getContentIds() {
        if(contentIds == null){
            contentIds = metaRepository.loadAllContentIdByMetaId(id);
            count = contentIds.size();
        }

        return contentIds;
    }

    private List<Long> contentIds;

    public Integer getCount() {
        if(count!=null || id==null){
            return count;
        }
        return metaRepository.countArticlesByMetaId(id);
    }

}
