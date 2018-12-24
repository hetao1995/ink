package xyz.itao.ink.repository;

import xyz.itao.ink.domain.OptionDomain;

import java.util.List;

/**
 * @author hetao
 * @date 2018-12-11
 * @description
 */
public interface OptionRepository {
    List<OptionDomain> loadAllOptionDomain() ;

    OptionDomain loadOptionDomainByName(String name) ;

    void updateOptionDomain(OptionDomain optionDomain) ;

    void saveNewOptionDomain(OptionDomain optionDomain) ;

    List<OptionDomain> loadAllOptionDomainNotDeleteLike(String pattern);

    OptionDomain loadOptionDomainById(Long id);
}
