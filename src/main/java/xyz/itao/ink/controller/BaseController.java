package xyz.itao.ink.controller;

import lombok.extern.slf4j.Slf4j;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.UserDomain;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hetao
 * @date 2018/11/29 0029
 * @description
 */
@Slf4j
public abstract class BaseController {

    public String render(String viewName) {
        return "themes/"+ Commons.THEME + "/" + viewName;
    }


    public String render_404() {
        return "/comm/error_404";
    }

}

