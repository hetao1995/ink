package xyz.itao.ink.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hetao
 * @date 2018-12-05
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 是否处于激活状态
     */
    private Boolean active;

    /**
     * 是否是长期用户 true：是 false：不是
     */
    private Boolean permanent;

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

    /**
     * 加密的盐，这个用户所有需要生成摘要的地方都用这个
     */
    private String salt;

    /**
     * 最近一次登陆时间
     */
    private Date lastLogin;
}
