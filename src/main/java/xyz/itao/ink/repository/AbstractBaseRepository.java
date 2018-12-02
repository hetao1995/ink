package xyz.itao.ink.repository;

/**
 * @author hetao
 * @date 2018-12-02
 * @description
 */
public abstract class AbstractBaseRepository<D, E> {
    protected D assemble(E entity) {
        return doAssemble(entity);
    }

    /**
     * 具体的组装操作
     *
     * @param entity 传入的实体对象
     * @return 组装的domain
     */
    protected abstract D doAssemble(E entity);

    protected E extract(D domain) {

        return doExtract(domain);
    }

    /**
     * 具体的提取操作
     *
     * @param domain domain域
     * @return 提取的结果
     */
    protected abstract E doExtract(D domain);
}
