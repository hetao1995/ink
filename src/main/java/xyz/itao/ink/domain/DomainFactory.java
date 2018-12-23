package xyz.itao.ink.domain;

import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.itao.ink.repository.*;

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
     * LogRepository对象
     */
    @Autowired
    @Lazy
    private LogRepository logRepository;

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

    /**
     * 生成archiveDomain对象
     * @return
     */
    public ArchiveDomain createArchiveDomain(){
        return new ArchiveDomain(contentRepository);
    }

    /**
     * 生成CommentDomain对象
     * @return
     */
    public CommentDomain createCommentDomain(){
        return new CommentDomain(userRepository, contentRepository, commentRepository);
    }

    public LogDomain createLogDomain(){
        return new LogDomain(logRepository);
    }
}
