package xyz.itao.ink.domain;

import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.itao.ink.repository.CommentRepository;
import xyz.itao.ink.repository.ContentRepository;
import xyz.itao.ink.repository.MetaRepository;
import xyz.itao.ink.repository.UserRepository;

/**
 * @author hetao
 * @date 2018-12-21
 * @description
 */
@Component
public class DomainFactory {
    /**
     * UserRepository 对象
     */
    @Autowired
    @Lazy
    private UserRepository userRepository;

    /**
     * commentRepository对象
     */
    @Autowired
    @Lazy
    private CommentRepository commentRepository;

    /**
     * metaRepository对象
     */
    @Autowired
    @Lazy
    private MetaRepository metaRepository;

    /**
     * contentRepository对象
     */
    @Autowired
    @Lazy
    private ContentRepository contentRepository;

    /**
     * 生成ContentDoamin
     * @return
     */
    public ContentDomain createContentDomain(){
        return new ContentDomain(userRepository, contentRepository, commentRepository, metaRepository, this);
    }

    /**
     * 生成MetaDomain
     * @return
     */
    public MetaDomain createMetaDomain(){
        return new MetaDomain(metaRepository);
    }

}
