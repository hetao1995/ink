package xyz.itao.ink.controller.admin;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.Commons;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LinkVo;
import xyz.itao.ink.service.LinkService;


import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-07
 * @description 附件管理
 */
@Controller
@RequestMapping("admin/attach")
@Slf4j
public class AttachController {
    @Autowired
    Commons commons;


    @Autowired
    private LinkService linkService;


    /**
     * 附件页面
     *
     * @param request request
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request, PageParam pageParam) {
        PageInfo<LinkVo> attachPaginator = linkService.loadAllActiveLinkVo(pageParam);
        request.setAttribute("attaches", attachPaginator);
        request.setAttribute(TypeConst.ATTACH_URL, commons.siteOption(TypeConst.ATTACH_URL, commons.siteUrl()));
        request.setAttribute("max_file_size", WebConstant.MAX_FILE_SIZE / 1024);
        return "admin/attaches";
    }

    /**
     * 上传文件接口
     *
     * @param multipartFiles 上传的文件
     * @return 所有linkvo，包括上传成功和不成功的
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
