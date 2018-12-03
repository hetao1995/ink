package xyz.itao.ink.controller;

import lombok.extern.slf4j.Slf4j;
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

    public static String THEME = "themes/default";


    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    public UserDomain user(HttpServletRequest request) {
        return (UserDomain) request.getAttribute(WebConstant.LOGIN_USER);
    }

    public Long getUid(HttpServletRequest request){
        return this.user(request).getId();
    }

    public String render_404() {
        return "/comm/error_404";
    }

}

