package xyz.itao.ink.domain.params;

import lombok.Data;
import lombok.ToString;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@ToString(callSuper = true)
public class CommentParam extends PageParam{
    private Long excludeUID;
}
