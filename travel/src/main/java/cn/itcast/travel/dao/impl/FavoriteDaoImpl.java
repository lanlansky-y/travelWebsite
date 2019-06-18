package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findFavByUidAndRid(int uid, int rid) {

        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where uid = ? and rid = ? ";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid, rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {

        String sql = "select count(*) from tab_favorite where rid = ?";
        Integer count = template.queryForObject(sql, Integer.class, rid);
        return count;
    }

    @Override
    public int addFavorite(int uid, int rid) {
        int update = 0;
        try {
            String sql = "insert into tab_favorite values(?,?,?)";
            update = template.update(sql, rid, new Date(), uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
//        System.out.println(update);
        return update;
    }

    @Override
    public int delFavorite(int uid, int rid) {
        String sql = "delete from tab_favorite where uid = ? and rid = ?";
        int update = template.update(sql, uid, rid);
        return update;
    }
}
