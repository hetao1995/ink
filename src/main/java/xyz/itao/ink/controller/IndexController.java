package xyz.itao.ink.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.itao.ink.domain.entity.Content;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
@Controller
@Slf4j
public class IndexController extends BaseController{
    @GetMapping(value = "/")
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.index(request, 1, limit);
    }
    @GetMapping(value = "page/{p}")
    public String index(HttpServletRequest request, @PathVariable int p, @RequestParam(value = "limit", defaultValue = "12") int limit) {
//        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;

        if (p > 1) {
            this.title(request, "第" + p + "页");
        }
        return this.render("index");
    }
}
