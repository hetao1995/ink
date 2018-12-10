package xyz.itao.ink.service.impl;

import org.springframework.stereotype.Service;
import xyz.itao.ink.domain.BaseDomain;
import xyz.itao.ink.domain.MetaDomain;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.MetaVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.service.AbstractBaseService;
import xyz.itao.ink.service.MetaService;

import java.util.List;
import java.util.Map;

/**
 * @author hetao
 * @date 2018-12-10
 * @description
 */
@Service("metaService")
//todo 
public class MetaServiceImpl extends AbstractBaseService<MetaDomain, MetaVo> implements MetaService {

    @Override
    protected MetaDomain doAssemble(MetaVo vo) {
        return MetaDomain
                .builder()
                .id(vo.getId())
                .active(vo.getActive())
                .name(vo.getName())
                .detail(vo.getDetail())
                .value(vo.getValue())
                .build();
    }

    @Override
    protected MetaVo doExtract(MetaDomain domain) {
        return MetaVo
                .builder()
                .id(domain.getId())
                .active(domain.getActive())
                .name(domain.getName())
                .detail(domain.getDetail())
                .value(domain.getValue())
                .build();
    }

    @Override
    protected MetaDomain doUpdate(MetaDomain domain) {
        return null;
    }

    @Override
    protected MetaDomain doSave(MetaDomain domain) {
        return null;
    }

    @Override
    public void saveMeta(String category, String cname, Integer mid, UserVo userVo) {

    }

    @Override
    public void deleteMetaById(Long id, UserVo userVo) {

    }

    @Override
    public Map<String, List<ContentVo>> getMetaMapping(String category) {
        return null;
    }

    @Override
    public MetaVo getMeta(String category, String keyword) {
        return null;
    }
}
