package xyz.itao.ink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.LinkDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.PageParam;
import xyz.itao.ink.domain.vo.LinkVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.LinkRepository;
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
public class LinkServiceImpl implements LinkService {

    @Autowired
    LinkRepository linkRepository;
    @Autowired
    DomainFactory domainFactory;

    @Override
    public PageInfo<LinkVo> loadAllActiveLinkVo(PageParam pageParam) {
        Page page = PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize(), pageParam.getOderBy());
        List<LinkDomain> linkDomains = linkRepository.loadAllActiveLinkDomain();
        List<LinkVo> linkVos = linkDomains.stream().map(LinkDomain::vo).collect(Collectors.toList());
        PageInfo<LinkVo> pageInfo =  new PageInfo<>(page);
        pageInfo.setList(linkVos);
        return pageInfo;
    }

    @Override
    public PageInfo<LinkDomain> loadAllActiveLinkDomain(PageParam pageParam) {
        Page page = PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize(), pageParam.getOderBy());
        List<LinkDomain> linkDomains = linkRepository.loadAllActiveLinkDomain();
        PageInfo<LinkDomain> pageInfo =  new PageInfo<>(page);
        pageInfo.setList(linkDomains);
        return pageInfo;
    }

    @Override
    @Transactional
    public void deleteAttachesById(Long id, UserDomain userDomain) {
        LinkDomain linkDomain = linkRepository.loadLinkDomainById(id);
        if(linkDomain==null){
            throw  new InnerException(ExceptionEnum.DELETE_NON_EXIST_ELEMENT);
        }
        linkDomain.setUpdateBy(userDomain.getId()).deleteById();
    }


    @Override
    @Transactional
    public List<LinkVo> saveFiles(MultipartFile[] multipartFiles, UserDomain userDomain)  {
        List<LinkVo> res = Lists.newArrayList();
        for (MultipartFile multipartFile : multipartFiles) {
            LinkVo linkVo = null;
            try {
                String fname = multipartFile.getOriginalFilename(), ftype = multipartFile.getContentType().contains("image") ? TypeConst.IMAGE : TypeConst.FILE;
                String fid = String.valueOf(IdUtils.nextId());
                linkVo = LinkVo
                        .builder()
                        .fileName(fname)
                        .authorId(userDomain.getId())
                        .fileType(ftype)
                        .active(true)
                        .build();
                String ext = FileUtils.fileExt(multipartFile.getInputStream(), fname);
                if (ext==null){
                    continue;
                }
                String fkey = fid+"."+ ext;
                linkVo.setFileKey(fkey);

                String filePath = WebConstant.UP_DIR + fkey;
                System.out.println("filePath:"+filePath);
                Files.write(Paths.get(filePath), multipartFile.getBytes());
                if(TypeConst.IMAGE.equals(ftype)){
                    String thumbnailFilePath = fkey.replace(fid, "thumbnail_" + fid);
                    ImageUtils.cutCenterImage(WebConstant.UP_DIR + fkey, WebConstant.UP_DIR +thumbnailFilePath, 270, 380);
                }
                LinkDomain linkDomain = domainFactory.createLinkDomain().assemble(linkVo).setCreateBy(userDomain.getId()).setUpdateBy(userDomain.getId()).save();
                res.add(linkDomain.vo());
            } catch (IOException e) {
                log.debug("上传文件失败:{}", e);
                e.printStackTrace();
                res.add(linkVo);
                continue;
            }

        }
        return res;
    }

}
