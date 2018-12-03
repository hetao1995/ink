package xyz.itao.ink.repository;

import xyz.itao.ink.domain.BaseDomain;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.utils.DateUtils;
import xyz.itao.ink.utils.IdUtils;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
public abstract class AbstractBaseRepository<D extends BaseDomain, E> {

    /**
     * 持久化domain，在数据库中插入一条数据
     * @param domain 需要存入数据库的内容, createBy、updateBy不可缺少
     * @return 持久化之后新的Domian对象
     */
    public  D save(D domain){
        domain.setCreateTime(DateUtils.getNow());
        domain.setUpdateTime(DateUtils.getNow());
        domain.setId(IdUtils.nextId());
        E entity = extract(domain);
        boolean saved = doSave(entity);
        if(!saved){
            throw new InnerException(ExceptionEnum.PERSISTENCE_FAIL, domain);
        }
        return loadById(domain.getId());
    }

    /**
     * 更新数据，需要通过id更新，必须保证有id，updateBy
     * @param domain 需要更新的数据
     * @return 更新的结果
     */
    public D update(D domain){
        domain.setCreateTime(DateUtils.getNow());
        E entity = extract(domain);
        boolean updated = doUpdate(entity);
        if(!updated ){
            throw new InnerException(ExceptionEnum.PERSISTENCE_FAIL, domain);
        }
        //todo 缓存失效
        return loadById(domain.getId());
    }

    /**
     * 通过主键加载数据
     * @param id 主键
     * @return 加载的domain
     */
    public  D loadById(Long id){
        // todo 首先从缓存中查找
        D domain = (D) D.builder().id(id).deleted(false).build();
        E entity = extract(domain);
        entity = doLoadByNoNullProperties(entity);
        // todo 更新缓存中的entity
        return assemble(entity);
    }

    /**
     * 通过entity组装domain
     * @param entity 需要组装的entity
     * @return 组装的结果
     */
    protected D assemble(E entity) {
        if(entity == null){
            return null;
        }
        return doAssemble(entity);
    }

    /**
     * 从domain中提取entity
     * @param domain 传入的domain
     * @return 提取的结果
     */
    protected E extract(D domain) {
        if(domain == null){
            return null;
        }
        return doExtract(domain);
    }

    /**
     * 具体的持久化Entity操作
     * @param entity 需要持久化的实体
     * @return 是否持久化成功
     */
    protected abstract boolean doSave(E entity);

    /**
     * 具体的通过非空条件加载Entity方法
     * @param entity 条件
     * @return 加载的entity
     */
    protected  abstract E doLoadByNoNullProperties(E entity);

    /**
     * 具体的更新操作
     * @param entity 需要更新的实体
     * @return 更新是否成功
     */
    protected abstract boolean doUpdate(E entity);

    /**
     * 具体的组装操作
     *
     * @param entity 传入的实体对象
     * @return 组装的domain
     */
    protected abstract D doAssemble(E entity);

    /**
     * 具体的提取操作
     *
     * @param domain domain域
     * @return 提取的结果
     */
    protected abstract E doExtract(D domain);
}
