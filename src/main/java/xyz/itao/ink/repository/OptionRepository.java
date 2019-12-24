package xyz.itao.ink.repository;

import xyz.itao.ink.domain.OptionDomain;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-11
 */
public interface OptionRepository {
    /**
     * 获取所有option
     *
     * @return 所有option
     */
    List<OptionDomain> loadAllOptionDomain();

    /**
     * 通过name获取
     *
     * @param name name
     * @return 结果
     */
    OptionDomain loadOptionDomainByName(String name);

    /**
     * update
     *
     * @param optionDomain target
     */
    void updateOptionDomain(OptionDomain optionDomain);

    /**
     * save
     *
     * @param optionDomain save的对象
     */
    void saveNewOptionDomain(OptionDomain optionDomain);

    /**
     * 模糊查询
     *
     * @param pattern 关键字
     * @return 结果
     */
    List<OptionDomain> loadAllOptionDomainNotDeleteLike(String pattern);

    /**
     * 通过id获取
     *
     * @param id id
     * @return 结果
     */
    OptionDomain loadOptionDomainById(Long id);
}
