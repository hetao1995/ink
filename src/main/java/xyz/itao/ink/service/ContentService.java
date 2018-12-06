package xyz.itao.ink.service;

import com.github.pagehelper.PageInfo;
import xyz.itao.ink.domain.ContentDomain;
import xyz.itao.ink.domain.params.ArticleParam;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;

/**
 * @author hetao
 * @date 2018-12-04
 * @description
 */
public interface ContentService {
    /**
     * 通过id删除文章
     * @param id content的id
     * @return 是否删除成功
     */
    boolean deleteById(Long id, UserVo userVo);

    /**
     * 通过id加载ContentVo
     * @param id content的主键
     * @return 加载的结果
     */
    ContentVo loadContentVoById(Long id);

    /**
     * 发布一个新的内容
     * @param contentVo 发布的内容
     * @return  发布后的vo
     */
    ContentVo publishNewContent(ContentVo contentVo, UserVo userVo);

    /**
     * 分页加载所有的contentvo
     * @return
     */
    PageInfo<ContentVo> loadAllActiveContentVo(ArticleParam articleParam);

    /**
     * 修改文章
     * @param contentVo
     * @param userVo
     */
    void updateArticle(ContentVo contentVo, UserVo userVo);

}
