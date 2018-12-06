package xyz.itao.ink.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.itao.ink.annotation.SysLog;
import xyz.itao.ink.common.RestResponse;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.controller.BaseController;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.StatisticsVo;
import xyz.itao.ink.service.SiteService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class IndexController extends BaseController {


    @Autowired
    private SiteService siteService;

    /**
     * 仪表盘
     */
    @GetMapping(value = {"/", "/index"})
    public String index(HttpServletRequest request) {
        List<CommentVo> commentVos   = siteService.recentComments(5);
        List<ContentVo> contentVo   = siteService.getContens(TypeConst.RECENT_ARTICLE, 5);
        StatisticsVo statisticsVo = siteService.getStatistics();

        request.setAttribute("comments", commentVos);
        request.setAttribute("articles", contentVo);
        request.setAttribute("statistics", statisticsVo);
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping("/profile")
    public String profile() {
        return "/admin/profile";
    }

    /**
     * 上传文件接口
     */
    @SysLog("上传附件")
    @PostMapping("api/attach/upload")
    @ResponseBody
    public RestResponse<?> upload(HttpServletRequest request) {

        log.info("UPLOAD DIR = {}", TaleUtils.UP_DIR);

        Users        users      = this.user();
        Integer      uid        = users.getUid();
        List<Attach> errorFiles = new ArrayList<>();
        List<Attach> urls       = new ArrayList<>();

        Map<String, FileItem> fileItems = request.fileItems();
        if (null == fileItems || fileItems.size() == 0) {
            return RestResponse.fail("请选择文件上传");
        }

        fileItems.forEach((fileName, fileItem) -> {
            String fname = fileItem.getFileName();
            if ((fileItem.getLength() / 1024) <= TaleConst.MAX_FILE_SIZE) {
                String fkey = TaleUtils.getFileKey(fname);

                String ftype    = fileItem.getContentType().contains("image") ? Types.IMAGE : Types.FILE;
                String filePath = TaleUtils.UP_DIR + fkey;


                try {
                    Files.write(Paths.get(filePath), fileItem.getData());
                    if (TaleUtils.isImage(new File(filePath))) {
                        String newFileName       = TaleUtils.getFileName(fkey);
                        String thumbnailFilePath = TaleUtils.UP_DIR + fkey.replace(newFileName, "thumbnail_" + newFileName);
                        ImageUtils.cutCenterImage(CLASSPATH + fkey, thumbnailFilePath, 270, 380);

                    }
                } catch (IOException e) {
                    log.error("", e);
                }

                Attach attach = new Attach();
                attach.setFname(fname);
                attach.setAuthorId(uid);
                attach.setFkey(fkey);
                attach.setFtype(ftype);
                attach.setCreated(DateKit.nowUnix());
                attach.save();

                urls.add(attach);
                siteService.cleanCache(Types.SYS_STATISTICS);
            } else {
                Attach attach = new Attach();
                attach.setFname(fname);
                errorFiles.add(attach);
            }
        });

        if (errorFiles.size() > 0) {
            return RestResponse.fail().payload(errorFiles);
        }
        return RestResponse.ok(urls);
    }


}

