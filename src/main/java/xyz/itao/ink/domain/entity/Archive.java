package xyz.itao.ink.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.itao.ink.domain.entity.Content;

import java.util.Date;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Archive {
    private String dateStr;
    private Integer count;
}
