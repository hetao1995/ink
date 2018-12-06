package xyz.itao.ink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.params.InstallParam;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.OptionService;
import xyz.itao.ink.service.SiteService;
import xyz.itao.ink.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
public class InstallController extends BaseController {
    @Autowired
    private SiteService siteService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private UserService userService;

    /**
     * 安装页
     */
    @GetMapping
    public String index(HttpServletRequest request) {
        boolean existInstall   = Files.exists(Paths.get(WebConstant.CLASSPATH + "install.lock"));
        boolean allowReinstall = WebConstant.OPTIONS.getBoolean(WebConstant.OPTION_ALLOW_INSTALL, false);
        request.setAttribute("is_install", !allowReinstall && existInstall);
        return "install";
    }


    @ResponseBody
    @PostMapping
    public RestResponse<?> doInstall(InstallParam installParam) {
        if (isRepeatInstall()) {
            return RestResponse.fail("请勿重复安装");
        }

        CommonValidator.valid(installParam);

        UserVo userVo  = siteService.installSite(installParam);



        String siteUrl = TaleUtils.buildURL(installParam.getSiteUrl());
        optionsService.saveOption("site_title", installParam.getSiteTitle());
        optionsService.saveOption("site_url", siteUrl);

        TaleConst.OPTIONS = Environment.of(optionsService.getOptions());

        return RestResponse.ok();
    }

    private boolean isRepeatInstall() {
        return Files.exists(Paths.get(CLASSPATH + "install.lock"))
                && TaleConst.OPTIONS.getInt("allow_install", 0) != 1;
    }
}
