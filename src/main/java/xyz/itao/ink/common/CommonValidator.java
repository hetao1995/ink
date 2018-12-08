package xyz.itao.ink.common;

import org.apache.commons.lang3.StringUtils;
import xyz.itao.ink.constant.WebConstant;
import xyz.itao.ink.domain.params.InstallParam;
import xyz.itao.ink.domain.vo.CommentVo;
import xyz.itao.ink.domain.vo.ContentVo;
import xyz.itao.ink.domain.vo.UserVo;
import xyz.itao.ink.exception.ExceptionEnum;
import xyz.itao.ink.exception.TipException;
import xyz.itao.ink.utils.PatternUtils;

/**
 * @author hetao
 * @date 2018-12-06
 * @description
 */
public class CommonValidator {

    public static void valid(ContentVo contentVo) {
        if(stringLengthBetween(contentVo.getTitle(), WebConstant.MIN_CONTENT_TITLE_LENGTH, WebConstant.MAX_CONTENT_TITLE_LENGTH)){
            throw new TipException(ExceptionEnum.CONTENT_TITLE_ILLEGAL);
        }
        if(stringLengthBetween(contentVo.getContent(), WebConstant.MIN_CONTENT_TEXT_LENGTH, WebConstant.MAX_CONTENT_TEXT_LENGTH)){
            throw  new TipException(ExceptionEnum.CONTENT_TEXT_ILLEGAL);
        }

    }

    public static void valid(CommentVo commentVo) {
        if(stringLengthBetween(commentVo.getContent(), WebConstant.MIN_COMMENT_TEXT_LENGTH, WebConstant.MAX_COMMENT_TEXT_LENGTH)){
            throw new TipException(ExceptionEnum.COMMENT_TEXT_ILLEGAL);
        }
        if(commentVo.getContentId()==null){
            throw new TipException(ExceptionEnum.COMMENT_CONTENT_ID_ILLEGAL);
        }
    }


    public static void valid(UserVo userVo, boolean isPermanent){
        if((isPermanent || userVo.getUsername() != null) && !PatternUtils.isUsername(userVo.getUsername())){
            throw new TipException(ExceptionEnum.USERNAME_ILLEGAL);
        }
        if((isPermanent || userVo.getHomeUrl() != null) && !PatternUtils.isURL(userVo.getHomeUrl())){
            throw new TipException(ExceptionEnum.HOME_URL_ILLEGAL);
        }
        if((isPermanent || userVo.getEmail() != null) && !PatternUtils.isEmail(userVo.getEmail())){
            throw new TipException(ExceptionEnum.EMAIL_ILLEGAL);
        }
        if((isPermanent || userVo.getPassword() != null) && (userVo.getPassword().length()<6 || userVo.getPassword().length()>20)){
            throw new TipException(ExceptionEnum.PASSWORD_ILLEGAL);
        }
        if((isPermanent || userVo.getDisplayName() != null) && userVo.getDisplayName().length()<2 || userVo.getDisplayName().length()>20){
            throw new TipException(ExceptionEnum.DISPLAY_NAME_ILLEGAL);
        }
    }

    private static boolean stringLengthBetween(String str, int start, int end){
        if(StringUtils.isBlank(str) || str.length()<start || str.length()>=end){
            return false;
        }
        return true;
    }
}
