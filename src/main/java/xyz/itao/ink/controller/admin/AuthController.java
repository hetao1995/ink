package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.UserService;

/**
 * @author hetao
 * @date 2018-12-04
 * @description 登陆登出操作
 */
@Controller
@Slf4j
@RequestMapping(value = "/admin")
public class AuthController  {
    @Autowired
    UserService userService;
    @GetMapping(value = "/login")
    public String login(){
        return "admin/login";
    }

    @SysLog("保存个人信息")
    @PutMapping("/profile")
    public RestResponse saveProfile(String screenName, String email, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        userService.updateProfile(screenName, email, userVo);
        return RestResponse.ok();
    }

    @SysLog("修改登录密码")
    @PutMapping("/password")
    public RestResponse upPwd(String old_password, String password, @RequestAttribute(WebConstant.LOGIN_USER) UserVo userVo) {
        if (StringUtils.isBlank(old_password) || StringUtils.isBlank(password)) {
            return RestResponse.fail("请确认信息输入完整");
        }
        userService.updatePassword(old_password, password, userVo);

        return RestResponse.ok();
    }
}
