package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface RouteSellerDao {
    Seller findSeller(int sid);
}
