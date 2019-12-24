package xyz.itao.ink.service;

import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.InstallParam;


/**
 * @author hetao
 * @date 2018-12-04
 */
public interface SiteService {
    /**
     * 安装
     *
     * @param installParam param
     * @return admin
     */
    UserDomain installSite(InstallParam installParam);
}
