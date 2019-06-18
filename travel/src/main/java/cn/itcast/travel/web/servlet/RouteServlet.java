package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService rs = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cidStr = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");
        String rname = request.getParameter("rname");

        //类别id
        int cid = 0;
        if (cidStr != null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        //当前页
        int currentPage = 0;

        if (currentPageStr != null && currentPageStr.length() >0 && !"null".equals(currentPageStr)){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }

        //每页显示条数
        int pageSize = 10;
        PageBean<Route> pb = rs.pageQuery(pageSize, currentPage, cid, rname);
        String json = writeValueAsString(pb, response);
//        System.out.println(json);
        response.getWriter().write(json);
    }

    //查找单条线路
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String rid = request.getParameter("rid");
//        System.out.println(rid);
        Route route = null;
        if (rid != null && rid.length() > 0){
            route = rs.findOne(Integer.parseInt(rid));
        }
//        System.out.println(route);

        writeValue(route,response);
    }

    //用户是否收藏
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User)request.getSession().getAttribute("user");
        ResultInfo info = new ResultInfo();

        if (user != null){
            boolean flag = favoriteService.isFavExist(user.getUid(), Integer.parseInt(rid));
            if (flag){
                info.setFlag(true);

            }else {
                info.setFlag(false);
                info.setErrorMsg("未收藏");
            }


        }else {
            info.setFlag(false);
            info.setErrorMsg("未登录");
        }


        writeValue(info,response);

    }

    //收藏与取消
    public void collect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text = request.getParameter("text");
        String rid = request.getParameter("rid");
        User user = (User)request.getSession().getAttribute("user");
        ResultInfo info = new ResultInfo();

        if (user != null){
            if ("点击收藏".equals(text)){
                int count = favoriteService.addFavorite(user.getUid(), Integer.parseInt(rid));
                if (count == -1){
                    info.setFlag(false);
                    info.setErrorMsg("收藏失败");
                }else {
                    info.setFlag(true);
                    info.setData(count);
                    info.setErrorMsg(text);
                }
            }else {
                int count = favoriteService.delFavorite(user.getUid(),Integer.parseInt(rid));
                if (count == -1){
                    info.setFlag(false);
                    info.setErrorMsg("取消收藏失败");
                }else {
                    info.setFlag(true);
                    info.setData(count);
                    info.setErrorMsg(text);
                }
            }
        }else {
            info.setFlag(false);
            info.setErrorMsg("未登录");
        }
        writeValue(info,response);

    }
}
