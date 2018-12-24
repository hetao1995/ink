package xyz.itao.ink.domain.params;

import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
public class MetaParam {
    private Long id;
    private String name;
    private Long parentId = 0L;
}
