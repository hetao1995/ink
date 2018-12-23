package xyz.itao.ink.domain.params;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
@Data
public class PageParam {
    private Integer pageNum = 1;
    private Integer pageSize = 12;
    private String oderBy = "create_time desc";
}
