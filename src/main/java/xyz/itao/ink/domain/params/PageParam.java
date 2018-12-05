package xyz.itao.ink.domain.params;

import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
@Data
public class PageParam {
    private Integer pageNum = 1;
    private Integer PageSize = 12;
}
