package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LinkVo;
import xyz.itao.ink.domain.vo.UserVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
public interface LinkService {
    /**
     * 加载所有的文件元信息
     * @param pageParam 翻页的param
     * @return
     */
    PageInfo<LinkVo> loadAllActiveLinkVo(PageParam pageParam);

    /**
     * 删除文件
     * @param id 文件id
     * @param userVo 删除的人
     */
    void deleteAttachesById(Long id, UserVo userVo);

    PageInfo<LinkVo> getAttachs(int page, int limit);

    List<String> saveFiles(MultipartFile[] multipartFiles, UserVo userVo);
}
