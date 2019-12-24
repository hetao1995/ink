package xyz.itao.ink.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-06
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkVo {
    /**
     * 连接的id
     */
    private Long id;

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
}
