package xyz.itao.ink.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.LinkDomain;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LinkVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.LinkRepository;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.LinkService;
import xyz.itao.ink.utils.FileUtils;
import xyz.itao.ink.utils.IdUtils;
import xyz.itao.ink.utils.ImageUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
@Service("linkService")
@Slf4j
public class LinkServiceImpl extends AbstractBaseService<LinkDomain, LinkVo> implements LinkService {

    @Autowired
    LinkRepository linkRepository;

    @Override
    public PageInfo<LinkVo> loadAllActiveLinkVo(PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<LinkDomain> linkDomains = linkRepository.loadAllActiveLinkDomain();
        List<LinkVo> linkVos = linkDomains.stream().map(d->extract(d)).collect(Collectors.toList());
        return new PageInfo<>(linkVos);
    }

    @Override
    public void deleteAttachesById(Long id, UserVo userVo) {
        LinkDomain linkDomain = linkRepository.loadLinkDomainById(id);
        if(linkDomain==null){
            throw  new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        delete(extract(linkDomain), userVo.getId());
    }


    @Override
    public List<LinkVo> saveFiles(MultipartFile[] multipartFiles, UserVo userVo)  {
        List<LinkVo> res = Lists.newArrayList();
        for (MultipartFile multipartFile : multipartFiles) {
            String fname = multipartFile.getName(), ftype = multipartFile.getContentType().contains("image") ? TypeConst.IMAGE : TypeConst.FILE;
            String fid = String.valueOf(IdUtils.nextId());
            String fkey = WebConstant.UP_DIR+"/upload/"+fid+"."+ FileUtils.fileExt(fname);
            LinkVo linkVo = LinkVo
                    .builder()
                    .fileName(fname)
                    .authorId(userVo.getId())
                    .fileKey(fkey)
                    .fileType(ftype)
                    .build();

            // todo 上传到TFS
            String filePath = WebConstant.UP_DIR + fkey;
            try {
                Files.write(Paths.get(filePath), multipartFile.getBytes());
                if(TypeConst.IMAGE.equals(ftype)){
                    // todo 上传剪切文件到TFS
                    String thumbnailFilePath = fkey.replace(fid, "thumbnail_" + fid);
                    ImageUtils.cutCenterImage(WebConstant.CLASSPATH + fkey, thumbnailFilePath, 270, 380);
                }
                res.add(save(linkVo, userVo.getId()));
            } catch (IOException e) {
                log.debug("上传文件失败！", e);
                res.add(linkVo);
                continue;
            }

        }
        return res;
    }

    @Override
    protected LinkDomain doAssemble(LinkVo vo) {
        return LinkDomain
                .builder()
                .id(vo.getId())
                .active(vo.getActive())
                .authorId(vo.getAuthorId())
                .fileType(vo.getFileType())
                .fileName(vo.getFileName())
                .fileKey(vo.getFileKey())
                .build();
    }

    @Override
    protected LinkVo doExtract(LinkDomain domain) {
        return LinkVo
                .builder()
                .id(domain.getId())
                .active(domain.getActive())
                .authorId(domain.getAuthorId())
                .fileType(domain.getFileType())
                .fileName(domain.getFileName())
                .fileKey(domain.getFileKey())
                .build();
    }

    @Override
    protected LinkDomain doUpdate(LinkDomain domain) {
        return linkRepository.updateLinkDomain(domain);
    }

    @Override
    protected LinkDomain doSave(LinkDomain domain) {
        return linkRepository.saveNewLinkDomain(domain);
    }
}
