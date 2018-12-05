package xyz.itao.ink.service;

import xyz.itao.ink.domain.BaseDomain;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
public abstract class AbstractBaseService<D extends BaseDomain, V> {
    /**
     * vo组装domain
     * @param vo 传入的vo
     * @return 组装的结果
     */
    protected D assemble(V vo){
        if(vo == null){
            return null;
        }
        return doAssemble(vo);
    }

    /**
     * 具体的组装操作
     * @param vo 传入的vo
     * @return 组装的结果
     */
    protected abstract D doAssemble(V vo);

    /**
     * 从domain中提取vo
     * @param domain 需要提取的domain
     * @return 提取的结果
     */
    protected V extract(D domain){
        if(domain == null){
            return null;
        }
        return doExtract(domain);
    }

    /**
     * 具体的提取操作
     * @param domain 提取的domain
     * @return 提取结果
     */
    protected abstract V doExtract(D domain);

    /**
     * 更新操作
     * @param vo 需要更新的vo
     * @param userId 是谁更新的
     * @return 更新之后的结果
     */
    protected V update(V vo, Long userId){
        D domain = assemble(vo);
        domain.setUpdateBy(userId);
        domain = doUpdate(domain);
        return extract(domain);
    }

    /**
     * 具体的更新操作
     * @param domain 需要更新的domain
     * @return 更新之后的domain
     */
    protected abstract D doUpdate(D domain);

    /**
     * 删除操作，注意删除其实是将deleted字段更新
     * @param vo 需要删除的vo
     * @param userId
     * @return
     */
    protected boolean delete(V vo, Long userId){
        D domain = assemble(vo);
        domain.setUpdateBy(userId);
        domain.setDeleted(true);
        return doUpdate(domain) == null;
    }

    /**
     * 插入操作
     * @param vo 插入的vo
     * @param userId 操作者
     * @return 插入的结果
     */
    protected V save(V vo, Long userId){
        D domain = assemble(vo);
        domain.setUpdateBy(userId);
        domain.setCreateBy(userId);
        return extract(doSave(domain));
    }

    /**
     * 具体的插入操作
     * @param domain 插入的domain
     * @return 插入的结果
     */
    protected abstract D doSave(D domain);


}
