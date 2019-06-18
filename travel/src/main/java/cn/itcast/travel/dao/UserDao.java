package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    int save(User user);

    User findUser(User user);

    int findByCode(String code);

    User findUserByUsernameAndPassword(User user);

    User findYByUsernameAndPassword(User user);

//    String checkStatus(User user);
}
