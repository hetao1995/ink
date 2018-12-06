package xyz.itao.ink.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Data
@Builder
public class ArchiveVo {
    private String         dateStr;
    private Date date;
    private String         count;
    private List<ContentVo> articles;
}
