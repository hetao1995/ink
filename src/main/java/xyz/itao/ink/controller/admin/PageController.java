package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author hetao
 * @date 2018-12-07
 * @description
 */
@Controller
@RequestMapping("/admin/page")
@Slf4j
public class PageController {


    @GetMapping(value = "")
    public String index(){
        return "admin/pages";
    }

    @GetMapping("/{page}")
    public String commonPage(@PathVariable String page) {
        return "admin/" + page + ".html";
    }

    @GetMapping("/{module}/{page}")
    public String commonPage(@PathVariable String module, @PathVariable String page) {
        return "admin/" + module + "/" + page + ".html";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable String id) {
        return "admin/page/edit.html";
    }

    @GetMapping("/new")
    public String newPage(){
        return "admin/page/new";
    }

}
