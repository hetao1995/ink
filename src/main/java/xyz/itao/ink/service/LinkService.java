package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.domain.LinkDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LinkVo;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-06
 */
public interface LinkService {
    /**
     * 加载所有的文件元信息
     *
     * @param pageParam 翻页的param
     * @return 分页
     */
    PageInfo<LinkVo> loadAllActiveLinkVo(PageParam pageParam);

    /**
     * 获取所有的link
     *
     * @param pageParam param
     * @return link
     */
    PageInfo<LinkDomain> loadAllActiveLinkDomain(PageParam pageParam);

    /**
     * 删除文件
     *
     * @param id         文件id
     * @param userDomain 删除的人
     */
    void deleteAttachesById(Long id, UserDomain userDomain);

    /**
     * 保存上传的文件
     *
     * @param multipartFiles 上传的文件数据
     * @param userDomain     上传者
     * @return 没有上传成功的文件名称
     */
    List<LinkVo> saveFiles(MultipartFile[] multipartFiles, UserDomain userDomain);


}
