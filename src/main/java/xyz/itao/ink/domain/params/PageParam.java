package xyz.itao.ink.domain.params;

import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-04
 */
@Data
public class PageParam {
    private Integer pageNum = 1;
    private Integer pageSize = 12;
    private String oderBy = "create_time desc";
}
