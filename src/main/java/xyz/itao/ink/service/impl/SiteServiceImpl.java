package xyz.itao.ink.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.RoleDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.entity.Archive;
import xyz.itao.ink.domain.params.InstallParam;
import xyz.itao.ink.domain.vo.*;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.service.*;
import xyz.itao.ink.utils.InkUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-08
 * @description
 */
@Service("siteService")
@Slf4j
public class SiteServiceImpl implements SiteService {
    @Autowired
    UserService userService;
    @Autowired
    ContentService contentService;
    @Autowired
    MetaService metaService;
    @Autowired
    DomainFactory domainFactory;


    @Override
    public UserDomain installSite(InstallParam installParam) {
        if(StringUtils.isBlank(installParam.getSiteTitle())){
            throw  new TipException(ExceptionEnum.SITE_TITLE_ILLEGAL);
        }
        installParam.setSiteUrl(InkUtils.buildURL(installParam.getSiteUrl()));
        UserDomain admin = initAdmin(installParam);
        File lock = new File(WebConstant.CLASSPATH+"install.lock");
        try {
            boolean res = lock.createNewFile();
            if(!res){
                log.error("Ink初始化站点失败, file创建结果为false");
                throw  new InnerException(ExceptionEnum.SITE_INSTALL_FAILE);
            }
            log.debug("Ink初始化了站点");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Ink初始化站点失败！{}",e);
            throw  new InnerException(ExceptionEnum.SITE_INSTALL_FAILE);
        }

        return admin;
    }

    private UserDomain initAdmin(InstallParam installParam){
        UserVo userVo = UserVo
                .builder()
                .displayName(installParam.getAdminUser())
                .username(installParam.getAdminUser())
                .password(installParam.getAdminPwd())
                .homeUrl(installParam.getSiteUrl())
                .email(installParam.getAdminEmail())
                .build();
        UserDomain userDomain = userService.registerPermanentUser(userVo);
        RoleDomain roleDomain = domainFactory.createRoleDomain().setRole("ADMIN").setDetail("管理员").setCreateBy(0L).setUpdateBy(0L).save();
        List<RoleDomain> roleDomains = userDomain.getRoles();
        roleDomains.add(roleDomain);
        userDomain.setRoles(roleDomains).setUpdateBy(0L).updateById();
        return userDomain;
    }
}
