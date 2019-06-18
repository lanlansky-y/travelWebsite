package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteSellerDao;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class RouteSellerDaoImpl implements RouteSellerDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Seller findSeller(int sid) {
        Seller seller = null;
        try {
            String sql = "select * from tab_seller where sid = ?";
            seller = template.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), sid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return seller;
    }
}
