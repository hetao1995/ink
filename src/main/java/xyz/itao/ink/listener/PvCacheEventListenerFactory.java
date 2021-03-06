package xyz.itao.ink.listener;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.itao.ink.domain.DomainFactory;
import xyz.itao.ink.utils.SpringContextHolder;

import java.util.Properties;

/**
 * @author hetao
 * @date 2018-12-30
 * @description
 */
public class PvCacheEventListenerFactory extends CacheEventListenerFactory {


    private static DomainFactory domainFactory = SpringContextHolder.getBean("domainFactory");

    @Override
    public CacheEventListener createCacheEventListener(Properties properties) {
        return new PvCacheEventListener(domainFactory);
    }
}
