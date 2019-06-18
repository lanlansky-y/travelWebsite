package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.RouteSellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.RouteSellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private PageBean pb = new PageBean<Route>();
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private RouteSellerDao routeSellerDao = new RouteSellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int pageSize, int currentPage, int cid, String rname) {
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        int totalCount = routeDao.findCount(cid,rname);
        pb.setTotalCount(totalCount);

        int start = (currentPage-1)*pageSize;
        List<Route> list = routeDao.findAllRoute(cid, start, pageSize,rname);
        pb.setList(list);

        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) +1;
        pb.setTotalPage(totalPage);

        int beginPage;
        int endPage;
        if (totalPage<10){
            beginPage = 1;
            endPage = totalPage;
        }else {
            beginPage = currentPage-5;
            endPage = currentPage+4;
            if (beginPage<1){
                beginPage = 1;
                endPage = 10;
            }
            if (endPage > totalPage){
                beginPage = totalPage-9;
                endPage = totalPage;
            }
        }
        pb.setBeginPage(beginPage);
        pb.setEndPage(endPage);

        return pb;
    }

    @Override
    public Route findOne(int rid) {

        Route route = routeDao.findOne(rid);

        //获取数据库中的线路图片
        List<RouteImg> list = routeImgDao.findImgByRid(rid);
        route.setRouteImgList(list);
        //获取数据库中的商家信息
        Seller seller = routeSellerDao.findSeller(route.getSid());
        route.setSeller(seller);
        //获取收藏次数
        int count = favoriteDao.findCountByRid(rid);
        route.setCount(count);

        return route;
    }
}
