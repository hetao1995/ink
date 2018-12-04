package xyz.itao.ink.exception;

/**
 * @author hetao
 * @date 2018-12-03
 * @description 错误枚举 code<0 为内部错误， code>0 为外部错误
 */
public enum ExceptionEnum {
    PERSISTENCE_FAIL("持久化失败!",-1),
    USERNAME_ILLEGAL("用户名无效，用户名必须是2-20位，字母开头，数字、字母、下划线组成！", 1),
    EMAIL_ILLEGAL("不是有效的邮箱地址，请再次确认！", 2),
    HOME_URL_ILLEGAL("不是有效的主页地址，请再次确认！",3),
    DISPLAY_NAME_ILLEGAL("昵称无效，昵称必须小于20位！",4),
    PASSWORD_ILLEGAL("密码无效，密码必须在6-20位之间", 5),
    USERNAME_USED("该用户名已经被使用！",11),
    EMAIL_USED("该邮箱已经被使用！",12),
    HOME_URL_USED("该主页地址已经被使用！", 13),
    USERNAME_EMPTY("username不能为空！", 21),
    EMAIL_EMPTY("email不能为空！", 22),
    HOME_URL_EMPTY("主页不能为空！", 23),
    PASSWORD_EMPTY("密码不能为空！",24),
    USER_NOT_FIND("没有找到该用户！",31),
    ROLE_NOT_FOUND("没有找到该角色！",32);

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
