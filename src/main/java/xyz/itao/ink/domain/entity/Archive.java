package xyz.itao.ink.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import xyz.itao.ink.domain.entity.Content;

import java.util.Date;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Archive {
    private String         dateStr;
    private Date           date;
    private String         count;
    private List<Content> articles;
}
