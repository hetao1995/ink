package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.itao.ink.controller.BaseController;

/**
 * @author hetao
 * @date 2018-12-04
 * @description 登陆登出操作
 */
@Controller
@Slf4j
@RequestMapping(value = "/admin")
public class AuthController  {
    @GetMapping(value = "/login")
    public String login(){
        return "admin/login";
    }
}
