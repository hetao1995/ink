package xyz.itao.ink.domain;

/**
 * @author hetao
 * @date 2018-11-29 23:45
 * @description user域
 */
public class UserDomain implements Persistable<UserDomain>{

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public UserDomain assemble() {
        return null;
    }
}
