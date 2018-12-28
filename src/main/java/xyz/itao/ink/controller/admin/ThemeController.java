package xyz.itao.ink.controller.admin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.service.OptionService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-07
 * @description
 */
@Controller
@RequestMapping("/admin/theme")
@Slf4j
public class ThemeController  {
    @Autowired
    OptionService optionService;
    @Autowired
    Props props;

    @GetMapping("")
    public String themeIndex(){
        return "admin/themes";
    }

    /**
     * 主题设置页面
     */
    @GetMapping("/setting")
    public String setting(HttpServletRequest request) {
        String option = props.getThemeOption();
        Map<String, Object> map    = new HashMap<>();
        try {
            if (StringUtils.isNotBlank(option)) {
                map = JSON.parseObject(option);
            }
            request.setAttribute("options", map);
        } catch (Exception e) {
            log.error("解析主题设置出现异常", e);
        }
        request.setAttribute("theme_options", map);
        return props.renderTheme("setting");
    }
}
