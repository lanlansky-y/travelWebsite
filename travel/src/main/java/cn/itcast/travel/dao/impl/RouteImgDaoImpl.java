package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<RouteImg> findImgByRid(int rid) {
        List<RouteImg> list = null;
        try {
            String sql = "select * from tab_route_img where rid = ?";
            list = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }
}
