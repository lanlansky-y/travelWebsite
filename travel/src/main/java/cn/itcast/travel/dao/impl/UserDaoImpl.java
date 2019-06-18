package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private static JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?) ";
        int update = template.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail(), user.getStatus(), user.getCode());

        return update;
    }

    @Override
    public User findUser(User user) {
        User u = null;
        try {
            String sql = "select * from tab_user where username = ?";
            u = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername());
        } catch (DataAccessException e) {
//            e.printStackTrace();
        }
        return u;
    }

    @Override
    public int findByCode(String code) {
        String sql = "update tab_user set status = 'Y' where code = ?";
        int update = template.update(sql, code);
        return update;
    }

    @Override
    public User findUserByUsernameAndPassword(User user) {
        User u = null;
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            u = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),
                    user.getUsername(), user.getPassword());
        } catch (DataAccessException e) {
//            e.printStackTrace();
        }
        return u;
    }

    @Override
    public User findYByUsernameAndPassword(User user) {

        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            return template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername(), user.getPassword());
        } catch (Exception e) {
            return null;
        }


    }

//    @Override
//    public String checkStatus(User user) {
//        String sql = "select status from tab_user where username = ?";
//        String status = template.queryForObject(sql, String.class, user.getUsername());
//
//        return status;
//    }
}
