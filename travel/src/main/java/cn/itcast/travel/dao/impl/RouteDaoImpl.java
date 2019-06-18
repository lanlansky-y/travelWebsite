package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public int findCount(int cid, String rname) {
//        String sql = "select count(*) from tab_route where cid = ?";
        //定义模板
        String sql = "select count(*) from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();
        if (cid != 0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if (rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sql = sb.toString();


        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> findAllRoute(int cid, int start, int pageSize, String rname) {
//        String sql = "select * from tab_route where cid = ? limit ?,?";
        //定义模板
        String sql = "select * from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();
        if (cid != 0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if (rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ") ;
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();

        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        Route route = null;
        try {
            String sql = "select * from tab_route where rid = ?";
            System.out.println(sql);
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return route;
    }


}
