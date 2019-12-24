package xyz.itao.ink.domain.params;

import lombok.*;

/**
 * @author hetao
 * @date 2018-12-05
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ArticleParam extends PageParam {
    private String title;
    private String categories;
    private String status;
    private String type;
    private String orderBy;
}
