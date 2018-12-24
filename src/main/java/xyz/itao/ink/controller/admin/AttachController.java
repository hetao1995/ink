package xyz.itao.ink.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LinkVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.LinkService;
import xyz.itao.ink.service.LogService;
import xyz.itao.ink.utils.FileUtils;


import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

/**
 * @author hetao
 * @date 2018-12-07
 * @description 附件管理
 */
@Controller
@RequestMapping("admin/attach")
@Slf4j
public class AttachController extends BaseController {
    @Autowired
    Commons commons;


    @Autowired
    private LinkService linkService;


    /**
     * 附件页面
     *
     * @param request
     * @return
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request, PageParam pageParam) {
        PageInfo<LinkVo> attachPaginator = linkService.loadAllActiveLinkVo(pageParam);
        request.setAttribute("attaches", attachPaginator);
        request.setAttribute(TypeConst.ATTACH_URL, commons.site_option(TypeConst.ATTACH_URL, commons.site_url()));
        request.setAttribute("max_file_size", WebConstant.MAX_FILE_SIZE / 1024);
        return "admin/attaches";
    }

    /**
     * 上传文件接口
     *
     * @param
     * @return
     */
    @PostMapping(value = "upload")
    @ResponseBody
    @SysLog("上传文件")
    public RestResponse<?> upload( @RequestParam("file") MultipartFile[] multipartFiles, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        if(multipartFiles==null){
            RestResponse.fail("请选择文件上传！");
        }
        List<LinkVo> uploadFiles = linkService.saveFiles(multipartFiles, userDomain);
        return RestResponse.ok(uploadFiles);
    }

    @DeleteMapping(value = "{id}")
    @ResponseBody
    @SysLog(value = "删除附件")
    public RestResponse delete(@PathVariable Long id, @RequestAttribute(WebConstant.LOGIN_USER) UserDomain userDomain) {
        linkService.deleteAttachesById(id, userDomain);
        return RestResponse.ok();
    }

}
