package xyz.itao.ink.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.BaseDomain;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.MetaVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.service.AbstractBaseService;
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
public class MetaServiceImpl extends AbstractBaseService<MetaDomain, MetaVo> implements MetaService {

    @Autowired
    MetaRepository metaRepository;
    @Autowired
    DomainFactory domainFactory;
    @Override
    protected MetaDomain doAssemble(MetaVo vo) {
        return domainFactory.createMetaDomain().assemble(vo);
    }

    @Override
    protected MetaVo doExtract(MetaDomain domain) {
        return domain.vo();
    }

    @Override
    protected MetaDomain doUpdate(MetaDomain domain) {
        return metaRepository.updateMetaDomain(domain);
    }

    @Override
    protected MetaDomain doSave(MetaDomain domain) {
        return metaRepository.saveNewMetaDomain(domain);
    }

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
        save(metaVo, userVo.getId());

    }

    @Override
    public void deleteMetaById(Long id, UserVo userVo) {
        delete(MetaVo.builder().id(id).build(), userVo.getId());
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
        return metaDomains.stream().map(d->extract(d)).collect(Collectors.toList());
    }

    @Override
    public PageInfo<ContentDomain> getArticlesByMetaId(Long id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> contentDomains = metaRepository.loadAllActiveContentDomainByMetaId(id);
        return new PageInfo<>(contentDomains);
    }


}
