package xyz.itao.ink.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.BaseDomain;
import xyz.itao.ink.domain.params.InstallParam;
import xyz.itao.ink.domain.vo.*;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.RoleService;
import xyz.itao.ink.service.SiteService;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.InkUtils;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-08
 * @description
 */
@Service("siteService")
public class SiteServiceImpl implements SiteService {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Override
    public void cleanCache(String key) {

    }

    @Override
    public List<MetaVo> getMetaVo(String recentMeta, String category, int maxPosts) {
        return null;
    }

    @Override
    public List<ArchiveVo> getArchives() {
        return null;
    }

    @Override
    public List<CommentVo> recentComments(int i) {
        return null;
    }

    @Override
    public List<ContentVo> getContens(String recentArticle, int i) {
        return null;
    }

    @Override
    public StatisticsVo getStatistics() {
        return null;
    }

    @Override
    public UserVo installSite(InstallParam installParam) {
        if(StringUtils.isBlank(installParam.getSiteTitle())){
            throw  new TipException(ExceptionEnum.SITE_TITLE_ILLEGAL);
        }
        installParam.setSiteUrl(InkUtils.buildURL(installParam.getSiteUrl()));
        UserVo userVo = UserVo
                .builder()
                .displayName(installParam.getAdminUser())
                .username(installParam.getAdminUser())
                .password(installParam.getAdminPwd())
                .homeUrl(installParam.getSiteUrl())
                .email(installParam.getAdminEmail())
                .build();
        userVo = userService.registerPermanentUser(userVo);
        roleService.addRole("ADMIN", "管理员", 0L);
        roleService.addRoleToUser("ADMIN", userVo.getId(), 0L);
        return userVo;
    }
}
