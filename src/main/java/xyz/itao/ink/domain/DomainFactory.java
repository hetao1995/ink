package xyz.itao.ink.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.itao.ink.common.Props;
import xyz.itao.ink.repository.*;

/**
 * @author hetao
 * @date 2018-12-21
 */
@Component
public class DomainFactory {
    /**
     * UserRepository 对象
     */
    private UserRepository userRepository;

    /**
     * commentRepository对象
     */
    private CommentRepository commentRepository;

    /**
     * metaRepository对象
     */
    private MetaRepository metaRepository;

    /**
     * contentRepository对象
     */
    private ContentRepository contentRepository;
    /**
     * LogRepository对象
     */
    private LogRepository logRepository;

    /**
     * LinkRepository对象
     */
    private LinkRepository linkRepository;
    private OptionRepository optionRepository;
    private RoleRepository roleRepository;
    private Props props;

    private CacheManager cacheManager;

    /**
     * 生成ContentDoamin
     *
     * @return domain
     */
    public ContentDomain createContentDomain() {
        return new ContentDomain(userRepository, contentRepository, commentRepository, metaRepository, this, props, cacheManager);
    }

    /**
     * 生成MetaDomain
     *
     * @return domain
     */
    public MetaDomain createMetaDomain() {
        return new MetaDomain(metaRepository);
    }

    /**
     * 生成archiveDomain对象
     *
     * @return domain
     */
    public ArchiveDomain createArchiveDomain() {
        return new ArchiveDomain(contentRepository);
    }

    /**
     * 生成CommentDomain对象
     *
     * @return domain
     */
    public CommentDomain createCommentDomain() {
        return new CommentDomain(userRepository, contentRepository, commentRepository);
    }

    /**
     * 生成LogDomain对象
     *
     * @return domian
     */
    public LogDomain createLogDomain() {
        return new LogDomain(logRepository, userRepository);
    }

    /**
     * 生成StatisticsDomain对象
     *
     * @return domain
     */
    public StatisticsDomain createStatisticsDomain() {
        return new StatisticsDomain(contentRepository, commentRepository, metaRepository, linkRepository);
    }

    public OptionDomain createOptionDomain() {
        return new OptionDomain(optionRepository);
    }

    public UserDomain createUserDomain() {
        return new UserDomain(userRepository, roleRepository, cacheManager);
    }

    public RoleDomain createRoleDomain() {
        return new RoleDomain(roleRepository);
    }

    public LinkDomain createLinkDomain() {
        return new LinkDomain(linkRepository);
    }


    @Autowired
    @Lazy
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    @Lazy
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Autowired
    @Lazy
    public void setMetaRepository(MetaRepository metaRepository) {
        this.metaRepository = metaRepository;
    }

    @Autowired
    @Lazy
    public void setContentRepository(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Autowired
    @Lazy
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Autowired
    @Lazy
    public void setLinkRepository(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Autowired
    @Lazy
    public void setOptionRepository(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Autowired
    @Lazy
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    @Lazy
    public void setProps(Props props) {
        this.props = props;
    }

    @Autowired
    @Lazy
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
