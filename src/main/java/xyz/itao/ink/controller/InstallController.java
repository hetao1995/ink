package xyz.itao.ink.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.itao.ink.common.CommonValidator;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.InstallParam;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.service.OptionService;
import xyz.itao.ink.service.SiteService;
import xyz.itao.ink.service.UserService;
import xyz.itao.ink.utils.InkUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Controller
@RequestMapping("/install")
public class InstallController extends BaseController {
    @Autowired
    private SiteService siteService;

    @Autowired
    private OptionService optionService;
    @Autowired
    private Props props;


    /**
     * 安装页
     */
    @GetMapping
    public String index(HttpServletRequest request) {
        boolean existInstall   = Files.exists(Paths.get(WebConstant.CLASSPATH + "install.lock"));
        boolean allowReinstall = props.getBoolean(WebConstant.OPTION_ALLOW_INSTALL, false);
        request.setAttribute("is_install", !allowReinstall && existInstall);
        return "install";
    }


    @ResponseBody
    @PostMapping
    public RestResponse<?> doInstall(InstallParam installParam) {
        if (isRepeatInstall()) {
            return RestResponse.fail("请勿重复安装");
        }
        UserDomain userDomain  = siteService.installSite(installParam);


        props.set("site_title", installParam.getSiteTitle(), userDomain);
        props.set("site_url", userDomain.getHomeUrl(), userDomain);


        return RestResponse.ok();
    }

    private boolean isRepeatInstall() {
        return Files.exists(Paths.get(WebConstant.CLASSPATH + "install.lock"))&&props.getBoolean("allow_install", true);
    }
}
