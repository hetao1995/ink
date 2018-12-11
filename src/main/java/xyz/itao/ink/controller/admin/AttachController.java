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
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.controller.BaseController;
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



//    public static final String CLASSPATH = FileUtils.getUploadFilePath();

    @Autowired
    private LinkService linkService;

    @Autowired
    private LogService logService;

    /**
     * 附件页面
     *
     * @param request
     * @return
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request, PageParam pageParam) {
        PageInfo<LinkVo> attachPaginator = linkService.loadAllActiveLinkVo(pageParam);
        request.setAttribute("attachs", attachPaginator);
        request.setAttribute(TypeConst.ATTACH_URL, Commons.site_option(TypeConst.ATTACH_URL, Commons.site_url()));
        request.setAttribute("max_file_size", WebConstant.MAX_FILE_SIZE / 1024);
        return "admin/attach";
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
    public RestResponse<?> upload( @RequestParam("file") MultipartFile[] multipartFiles, UserVo userVo) throws IOException {
        if(multipartFiles==null){
            RestResponse.fail("请选择文件上传！");
        }
        List<LinkVo> uploadFiles = linkService.saveFiles(multipartFiles, userVo);
//        try {
//            for (MultipartFile multipartFile : multipartFiles) {
//                String fname = multipartFile.getOriginalFilename();
//                if (multipartFile.getSize() <= WebConst.MAX_FILE_SIZE) {
//                    String fkey = TaleUtils.getFileKey(fname);
//                    String ftype = TaleUtils.isImage(multipartFile.getInputStream()) ? TypeConst.IMAGE.getType() : TypeConst.FILE.getType();
//                    File file = new File(CLASSPATH + fkey);
//                    try {
//                        FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    errorFiles.add(fname);
//                }
//            }
//        } catch (Exception e) {
//            return RestRestResponse.fail();
//        }
        return RestResponse.ok(uploadFiles);
    }

    @DeleteMapping(value = "{id}")
    @ResponseBody
    @SysLog(value = "删除附件")
    public RestResponse delete(@PathVariable Long id, UserVo userVo) {
        linkService.deleteAttachesById(id, userVo);
//        try {
//            LinkVo linkVo = linkService.selectById(id);
//
//            if (null == attach) {
//                return RestResponseBo.fail("不存在该附件");
//            }
//            attachService.deleteById(id);
//            new File(CLASSPATH + attach.getFkey()).delete();
//            logService.insertLog(LogActions.DEL_ARTICLE.getAction(), attach.getFkey(), request.getRemoteAddr(), this.getUid(request));
//        } catch (Exception e) {
//            String msg = "附件删除失败";
//            LOGGER.error(msg, e);
//            return RestResponseBo.fail(msg);
//        }
        return RestResponse.ok();
    }

}
