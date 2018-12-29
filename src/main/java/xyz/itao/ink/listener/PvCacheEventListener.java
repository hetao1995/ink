package xyz.itao.ink.listener;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import xyz.itao.ink.domain.DomainFactory;

/**
 * @author hetao
 * @date 2018-12-29
 * @description
 */
public class PvCacheEventListener extends CacheEventListenerAdapter {

    DomainFactory domainFactory;

    PvCacheEventListener(DomainFactory domainFactory){
        this.domainFactory = domainFactory;
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {

        Long id = (Long) element.getObjectKey(), hit = (Long) element.getObjectValue();
        domainFactory
                .createContentDomain()
                .setId(id)
                .setHits(hit)
                .updateById();
    }
}
