package xyz.itao.ink.exception;

/**
 * @author hetao
 * @date 2018-12-03
 * @description 错误枚举 code<0 为内部错误， code>0 为外部错误
 */
public enum ExceptionEnum {
    ILLEGAL_OPERATION("非法操作，不支持此方法！", -1),
    PERSISTENCE_FAIL("持久化失败!",-2),
    DELETE_NON_EXIST_ELEMENT("删除不存在的元素！",-3),

    USERNAME_ILLEGAL("用户名无效，用户名必须是2-20位，字母开头，数字、字母、下划线组成！", 1),
    EMAIL_ILLEGAL("不是有效的邮箱地址，请再次确认！", 2),
    HOME_URL_ILLEGAL("不是有效的主页地址，请再次确认！",3),
    DISPLAY_NAME_ILLEGAL("昵称无效，昵称必须小于20位！",4),
    PASSWORD_ILLEGAL("密码无效，密码必须在6-20位之间", 5),
    CONTENT_TITLE_ILLEGAL("文章的标题无效，标题长度必须在1-200之间！",6),
    CONTENT_TEXT_ILLEGAL("文章内容无效，内容必须在1-200000之间！",7),
    CONTENT_SLUG_ILLEGAL("输入的路径无效，路径必须大于2并且是一个合法的路径",8),
    COMMENT_TEXT_ILLEGAL("评论无效内容，评论必须大于1并且小于2000！",9),
    COMMENT_CONTENT_ID_ILLEGAL("必须有评论的文章！",10),
    SITE_TITLE_ILLEGAL("网站名称不能为空！", 11),
    META_TYPE_ILLEGAL("meta的type不能为空", 12),
    META_NAME_ILLEGAL("meta的name不能为空！",13),
    USERNAME_USED("该用户名已经被使用！",11),
    EMAIL_USED("该邮箱已经被使用！",12),
    HOME_URL_USED("该主页地址已经被使用！", 13),
    META_HAS_SAVED("该meta已经保存过了！", 14),
    USERNAME_EMPTY("username不能为空！", 21),
    EMAIL_EMPTY("email不能为空！", 22),
    HOME_URL_EMPTY("主页不能为空！", 23),
    PASSWORD_EMPTY("密码不能为空！",24),
    USER_NOT_FIND("没有找到该用户！",31),
    ROLE_NOT_FOUND("没有找到该角色！",32),
    WRONG_OLD_PASSWORD("旧密码不正确，请核对！",41);

    private String message;
    private Integer code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    ExceptionEnum(String message, Integer code){
        this.message = message;
        this.code = code;
    }
}
