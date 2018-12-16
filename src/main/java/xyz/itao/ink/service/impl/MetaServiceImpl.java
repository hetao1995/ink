package xyz.itao.ink.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.BaseDomain;
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
    @Override
    protected MetaDomain doAssemble(MetaVo vo) {
        return MetaDomain
                .builder()
                .id(vo.getId())
                .active(vo.getActive())
                .name(vo.getName())
                .slug(vo.getSlug())
                .parentId(vo.getParentId())
                .type(vo.getType())
                .sort(vo.getSort())
                .detail(vo.getDetail())
                .build();
    }

    @Override
    protected MetaVo doExtract(MetaDomain domain) {
        return MetaVo
                .builder()
                .id(domain.getId())
                .active(domain.getActive())
                .name(domain.getName())
                .slug(domain.getSlug())
                .parentId(domain.getParentId())
                .type(domain.getType())
                .sort(domain.getSort())
                .detail(domain.getDetail())
                .build();
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
    public MetaVo getMeta(String type, String keyword) {
        return null;
    }

    @Override
    public List<MetaVo> getMetasByType(String type) {
        List<MetaDomain> metaDomains = metaRepository.loadMetaDomainsByType(type);
        return metaDomains.stream().map(d->extract(d)).collect(Collectors.toList());
    }
}
