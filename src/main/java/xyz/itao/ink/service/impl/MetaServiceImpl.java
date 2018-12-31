package xyz.itao.ink.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.itao.ink.annotation.CacheRemove;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
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
@CacheConfig(cacheNames = WebConstant.META_CACHE)
public class MetaServiceImpl implements MetaService {

    @Autowired
    MetaRepository metaRepository;
    @Autowired
    DomainFactory domainFactory;


    @Override
    @CacheEvict(key = "'type:'+#result.type")
    @Transactional
    public MetaDomain saveMeta(String type, MetaParam metaParam, UserDomain userDomain) {
        if(StringUtils.isBlank(type)){
            throw new TipException(ExceptionEnum.META_TYPE_ILLEGAL);
        }
        if(StringUtils.isBlank(metaParam.getName())){
            throw new TipException(ExceptionEnum.META_NAME_ILLEGAL);
        }
        MetaDomain metaDomain = metaRepository.loadMetaDomainByTypeAndName(type, metaParam.getName());
        if(metaDomain != null){
            throw new TipException(ExceptionEnum.META_HAS_SAVED);
        }
        metaParam.setId(null);
        return domainFactory
                .createMetaDomain()
                .assemble(metaParam)
                .setType(type)
                .setCreateBy(userDomain.getId())
                .setUpdateBy(userDomain.getId())
                .save();

    }

    @Override
    @Caching(evict = {
            @CacheEvict(key ="'type:'+#result.type+'_'+'name:'+#result.name"),
            @CacheEvict(key = "'type:'+#result.type")
    })
    @CacheRemove(value = WebConstant.META_CACHE, key = {"#id+'*'"})
    @Transactional
    public MetaDomain deleteMetaById(Long id, UserDomain userDomain) {
        return domainFactory
                .createMetaDomain()
                .setId(id)
                .setUpdateBy(userDomain.getId())
                .deleteById();
    }


    @Override
    @Cacheable(key = "'type:'+#type+'_'+'name:'+#name")
    public MetaDomain getMetaDomainByTypeAndName(String type, String name) {
        return metaRepository.loadMetaDomainByTypeAndName(type, name);
    }

    @Override
    @Cacheable(key = "'type:'+#type")
    public List<MetaVo> getMetasByType(String type) {
        List<MetaDomain> metaDomains = metaRepository.loadMetaDomainsByType(type);
        return metaDomains.stream().map(MetaDomain::vo).collect(Collectors.toList());
    }

    @Override
    @Cacheable(key="#id+'_num_'+#pageNum+'_size_'+#pageSize")
    public PageInfo<ContentDomain> getArticlesByMetaId(Long id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> contentDomains = metaRepository.loadAllActiveContentDomainByMetaIdAndStatus(id, TypeConst.PUBLISH);
        return new PageInfo<>(contentDomains);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key ="'type:'+#result.type+'_name:'+#result.name"),
            @CacheEvict(key = "'type:'+#result.type")
    })
    @CacheRemove(value = WebConstant.META_CACHE, key = {"#id+'*'"})
    @Transactional
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

    @Override
    public Map<String, List<ContentDomain>> getMetaMapping(String type) {
        return null;
    }


}
