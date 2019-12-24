package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.service.UserService;

/**
 * 登陆登出、修改密码操作
 *
 * @author hetao
 * @date 2018-12-04
 */
@Controller
@Slf4j
@RequestMapping(value = "/admin")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String login() {
        return "admin/login";
    }

    @SysLog("保存个人信息")
    @PutMapping("/profile")
    @ResponseBody
    public RestResponse saveProfile(String screenName, String email, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        userService.updateProfile(screenName, email, userDomain);
        return RestResponse.ok();
    }

    @SysLog("修改登录密码")
    @PutMapping("/password")
    @ResponseBody
    public RestResponse upPwd(@RequestParam(value = "old_password") String oldPassword, @RequestParam(value = "password") String password, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return RestResponse.fail("请确认信息输入完整");
        }
        userService.updatePassword(oldPassword, password, userDomain);

        return RestResponse.ok();
    }
}
