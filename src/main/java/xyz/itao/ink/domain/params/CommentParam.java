package xyz.itao.ink.domain.params;

import lombok.*;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentParam extends PageParam{
    private Long contentId;
    private String orderBy;
    private Long parentId = 0L;
}
