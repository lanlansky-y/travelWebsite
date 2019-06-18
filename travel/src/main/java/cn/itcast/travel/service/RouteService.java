package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    PageBean<Route> pageQuery(int pageSize,int currentPage,int cid,String rname);

    Route findOne(int rid);
}
