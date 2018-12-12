package xyz.itao.ink.domain.params;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@Builder
@ToString(callSuper = true)
public class ArticleParam extends PageParam {
    private String title;
    private String categories;
    private String status;
    private String type;
    private String orderBy;
}
