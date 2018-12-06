package xyz.itao.ink.domain.params;

import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Data
public class InstallParam {
    private String siteTitle;
    private String siteUrl;
    private String adminUser;
    private String adminEmail;
    private String adminPwd;
}
