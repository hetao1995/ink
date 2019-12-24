package xyz.itao.ink.domain.params;

import lombok.Data;

/**
 * @author hetao
 * @date 2018-12-06
 */
@Data
public class UserParam {
    /**
     * 用户名，由字母、数字、下划线组成，长度在30以下，不能重复
     */
    private String username;

    /**
     * 密码加盐bcrypt散列之后的值
     */
    private String password;

    /**
     * 显示的用户名
     */
    private String displayName;

    /**
     * 邮箱，不能重复
     */
    private String email;

    /**
     * 用户的主页，不能重复
     */
    private String homeUrl;

}
