package xyz.itao.ink.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.UserDomain;
import xyz.itao.ink.domain.params.MetaParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.MetaVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.service.MetaService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Service("metaService")
//todo
public class MetaServiceImpl implements MetaService {

    @Autowired
    MetaRepository metaRepository;
    @Autowired
    DomainFactory domainFactory;

    @Override
    public void saveMeta(String type, String name, Long mid, UserVo userVo) {
        if(StringUtils.isBlank(type)){
            throw new TipException(ExceptionEnum.META_TYPE_ILLEGAL);
        }
        if(StringUtils.isBlank(name)){
            throw new TipException(ExceptionEnum.META_NAME_ILLEGAL);
        }
        MetaDomain metaDomain = metaRepository.loadMetaDomainByTypeAndName(type, name);
        if(metaDomain != null){
            throw new TipException(ExceptionEnum.META_HAS_SAVED);
        }
        MetaVo metaVo = MetaVo
                .builder()
                .name(name)
                .id(mid)
                .type(type)
                .active(true)
                .parentId(0L)
                .build();
        domainFactory.createMetaDomain().assemble(metaVo).setCreateBy(userVo.getId()).setUpdateBy(userVo.getId()).save();
    }

    @Override
    public void saveMeta(String type, MetaParam metaParam, UserDomain userDomain) {
        metaParam.setId(null);
        domainFactory
                .createMetaDomain()
                .assemble(metaParam)
                .setType(type)
                .setCreateBy(userDomain.getId())
                .setUpdateBy(userDomain.getId())
                .save();

    }

    @Override
    public void deleteMetaById(Long id, UserDomain userDomain) {
        domainFactory
                .createMetaDomain()
                .setId(id)
                .setUpdateBy(userDomain.getId())
                .deleteById();
    }

    @Override
    public Map<String, List<ContentVo>> getMetaMapping(String type) {
        return null;
    }

    @Override
    public MetaDomain getMetaDomainByTypeAndName(String type, String name) {
        return metaRepository.loadMetaDomainByTypeAndName(type, name);
    }

    @Override
    public List<MetaVo> getMetasByType(String type) {
        List<MetaDomain> metaDomains = metaRepository.loadMetaDomainsByType(type);
        return metaDomains.stream().map(MetaDomain::vo).collect(Collectors.toList());
    }

    @Override
    public PageInfo<ContentDomain> getArticlesByMetaId(Long id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> contentDomains = metaRepository.loadAllActiveContentDomainByMetaId(id);
        return new PageInfo<>(contentDomains);
    }

    @Override
    public MetaDomain updateCategory(Long id, MetaParam metaParam, UserDomain userDomain) {
        MetaDomain metaDomain = domainFactory
                .createMetaDomain()
                .assemble(metaParam)
                .setId(id)
                .loadById();
        if(metaDomain==null){
            throw new TipException(ExceptionEnum.HAS_NOT_FIND_DATA);
        }
        if(!TypeConst.CATEGORY.equals(metaDomain.getType())){
            throw  new TipException(ExceptionEnum.FORBIDDEN_OPERATION);
        }
        metaDomain.setName(metaParam.getName()).setUpdateBy(userDomain.getId());
        return metaDomain.updateById();
    }


}
