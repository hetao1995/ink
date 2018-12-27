package xyz.itao.ink.service;

import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.params.InstallParam;
import xyz.itao.ink.domain.vo.*;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public interface SiteService {
    /**
     * 安装
     * @param installParam param
     * @return admin
     */
    UserDomain installSite(InstallParam installParam);
}
