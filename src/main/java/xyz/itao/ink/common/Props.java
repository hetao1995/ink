package xyz.itao.ink.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.itao.ink.constant.TypeConst;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.*;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.params.CommentParam;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.InnerException;
import xyz.itao.ink.repository.OptionRepository;
import xyz.itao.ink.service.CommentService;
import xyz.itao.ink.service.ContentService;
import xyz.itao.ink.utils.EhCacheUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

/**
 * @author hetao
 * @date 2018-12-24
 * @description
 */
@Component
@Slf4j
public class Props {
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private DomainFactory domainFactory;
    @Autowired
    private ContentService contentService;
    @Autowired
    private CommentService commentService;



    public String  set(String name, Object value, UserDomain userDomain){
        OptionDomain optionDomain = optionRepository.loadOptionDomainByName(name);
        if(optionDomain == null){
            domainFactory
                    .createOptionDomain()
                    .setCreateBy(userDomain.getId())
                    .setUpdateBy(userDomain.getId())
                    .setActive(true)
                    .setName(name)
                    .setValue(value.toString())
                    .save();
        }else{
            optionDomain.setName(name).setValue(value.toString()).setUpdateBy(userDomain.getId()).updateById();
        }
        EhCacheUtils.remove(WebConstant.PROPS_CACHE, name);
        return optionDomain.getValue();
    }

    public void setAll(Map<String, String> map, UserDomain userDomain){
        for(Map.Entry<String, String> entry : map.entrySet()){
            this.set(entry.getKey(), entry.getValue(), userDomain);
        }
    }

    public void setAll(Properties properties, UserDomain userDomain){
        for(String name : properties.stringPropertyNames()){
            this.set(name, properties.getProperty(name), userDomain);
        }
    }

    /**
     * 反射设置pojo所有参数，必须有get方法
     * @param param
     * @param userDomain
     */
    public void setAll(Object param, UserDomain userDomain){
        Class clazz = param.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            try {
                Method method = param.getClass().getMethod("get"+this.getMethodName(field.getName()));
                this.set(field.getName(), method.invoke(param), userDomain);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("反射获取参数出错：{}",e);
                throw  new InnerException(ExceptionEnum.ILLEGAL_OPERATION);
            }
        }
    }
    /**
     * 把一个字符串的第一个字母大写
     */
    private String getMethodName(String fildeName){
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    public Optional<String> get(String name){
        Object obj = EhCacheUtils.get(WebConstant.PROPS_CACHE, name);
        if(obj != null){
            return Optional.ofNullable(obj.toString());
        }
        OptionDomain optionDomain = optionRepository.loadOptionDomainByName(name);
        if(optionDomain==null){
            return  Optional.empty();
        }
        EhCacheUtils.put(WebConstant.PROPS_CACHE, name, optionDomain.getValue());
        return Optional.ofNullable(optionDomain.getValue());
    }

    public String get(String name, String defaultValue){
        return get(name).orElse(defaultValue);
    }

    public Optional<Integer> getInt(String name){
        Optional<String> optional = this.get(name);
        return optional.map(Integer::parseInt);
    }

    public Integer getInt(String name, Integer defaultValue){
        return getInt(name).orElse(defaultValue);
    }

    public Optional<Long> getLong(String name){
        Optional<String> optional = this.get(name);
        return optional.map(Long::parseLong);
    }

    public Long getLong(String name, Long defaultValue){
        return getLong(name).orElse(defaultValue);
    }

    public Optional<Boolean> getBoolean(String name){
        return this.get(name).map(Boolean::parseBoolean);
    }

    public Boolean getBoolean(String name, Boolean defaultValue){
        return getBoolean(name).orElse(defaultValue);
    }

    public Optional<Double> getDouble(String name){
        return this.get(name).map(Double::parseDouble);
    }

    /**
     * 拼接网站url
     * @param sub
     * @return
     */
    public String getSiteUrl(String sub){
        final String prefix = "http";
        String siteUrl = this.get(WebConstant.OPTION_SITE_URL, "");
        String protocol = this.get(WebConstant.OPTION_DEFAULT_PROTOCOL, "http://");
        if(StringUtils.isNotBlank(siteUrl) && !siteUrl.startsWith(prefix)){
            siteUrl = protocol+siteUrl;
        }
        return  siteUrl+ sub;
    }

    public String getSiteUrl(){
        return this.getSiteUrl("");
    }

    /**
     * 网站标题
     */
    public  String getSiteTitle() {
        return this.get(WebConstant.OPTION_SITE_TITLE, "ink");
    }

    public String getSiteKeywords(){
        return this.get(WebConstant.OPTION_SITE_KEYWORDS, "ink, blog");
    }

    public String getSiteDescription(){
        return this.get(WebConstant.OPTION_SITE_DESCRIPTION, "a beautiful blog worth to try");
    }

    public String getCdnUrl(String sub){
        return this.get(WebConstant.OPTION_CDN_URL, "/admin") + sub;
    }

    public  String getCdnUrl(){
        return this.getCdnUrl("");
    }

    public Boolean getAllowCloudCdn(){
        return this.getBoolean(WebConstant.OPTION_ALLOW_CLOUD_CDN, false);
    }

    public String getSiteTheme(){
        return this.get(WebConstant.OPTION_SITE_THEME, "default");
    }

    public String getThemeUrl(String sub){
        return this.getSiteUrl(WebConstant.THEME_URI+"/"+this.getSiteTheme() + sub);
    }

    /**
     * 最新文章
     * @return
     */
    public List<ContentDomain> getRecentArticles() {
        int limit = this.getInt(WebConstant.OPTION_RECENT_ARTICLE_LEN, 8);
        ArticleParam articleParam = ArticleParam.builder().orderBy("created desc").build();
        articleParam.setPageSize(limit);
        articleParam.setPageNum(1);
        articleParam.setType(TypeConst.ARTICLE);
        return contentService.loadAllActivePublishContentDomain(articleParam).getList();
    }

    /**
     * 最新评论
     *
     * @return
     */
    public  List<CommentDomain> getRecentComments() {
        int limit = this.getInt(WebConstant.OPTION_RECENT_COMMENT_LEN, 8);
        CommentParam commentParam = CommentParam.builder().orderBy("create_time desc").build();
        commentParam.setPageNum(1);
        commentParam.setPageSize(limit);
        return commentService.loadAllActiveApprovedCommentDomain(commentParam).getList();
    }


    public  String getAttachUrl(){
        return this.getAttachUrl("");
    }

    public String getAttachUrl(String fileKey){
        return this.get(TypeConst.ATTACH_URL, this.getSiteUrl("/upload/"))+fileKey;
    }

    public Integer getYearNow(){
        return LocalDate.now().getYear();
    }

    public String getRandomBgUrl(){
        return this.getCdnUrl("/images/bg/"+ RandomUtils.nextInt(1, 6) + ".png");
    }

    public String renderTheme(String viewName){
        return "themes/"+ this.getSiteTheme() + "/" + viewName;
    }

    public String render404(){
        return "/comm/error_404";
    }

    public String getThemeOption(){
        return this.get(this.getThemeOptionKey(), null);
    }

    public void setThemeOption(Map<String, String> options, UserDomain userDomain){
        this.set(this.getThemeOptionKey(),JSON.toJSONString(options), userDomain);
    }

    public String getThemeOptionKey(){
        return "theme_" + this.getSiteTheme() + "_options";
    }


    private static String BLOCK_IPS_SET = "block_ips_set";
    public void setBlockIps(String blockIps, UserDomain userDomain){
        this.set(WebConstant.OPTION_BLOCK_IPS, blockIps, userDomain);
        EhCacheUtils.put(WebConstant.PROPS_CACHE, BLOCK_IPS_SET, Sets.newHashSet(blockIps.split(",")));
    }

    public Set<String> getBlockIps(){
        Object obj = EhCacheUtils.get(WebConstant.PROPS_CACHE, BLOCK_IPS_SET);
        if(obj != null && obj instanceof Set){
            return (Set<String>) obj;
        }
        Set<String> set = Sets.newHashSet(this.get(WebConstant.OPTION_BLOCK_IPS, "").split(","));
        EhCacheUtils.put(WebConstant.PROPS_CACHE, BLOCK_IPS_SET, set);
        return set;
    }

}
