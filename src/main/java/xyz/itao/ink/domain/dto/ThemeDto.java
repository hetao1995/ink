package xyz.itao.ink.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hetao
 * @date 2018-12-06
 */
@Data
public class ThemeDto implements Serializable {

    private static final long serialVersionUID = 7122701941161403708L;

    /**
     * 主题名称
     */
    private String name;

    /**
     * 是否有设置项
     */
    private boolean hasSetting;

    public ThemeDto(String name) {
        this.name = name;
    }

}
