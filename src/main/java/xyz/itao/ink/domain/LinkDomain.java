package xyz.itao.ink.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
@Data
@Builder
@Accessors(chain = true)
public class LinkDomain extends BaseDomain{
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
}
