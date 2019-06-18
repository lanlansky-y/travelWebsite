package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.util.ResultInfoUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService us = new UserServiceImpl();

    //注册方法
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //校验验证码
        boolean flag = checkCode(request, response);
        if (!flag){
            return;
        }
        User user = new User();
        Map<String, String[]> map = request.getParameterMap();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        UserService us = new UserServiceImpl();
        String json = us.regist(user);

        response.getWriter().write(json);


    }

    //登陆方法
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //校验验证码
        boolean flag = checkCode(request, response);
        if (!flag){
            return;
        }

        Map<String, String[]> map = request.getParameterMap();
        String auto_login = request.getParameter("auto_login");
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        UserService us = new UserServiceImpl();
        User u = us.login(user);

        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        if (u == null){
            ResultInfo info = ResultInfoUtils.getRI(false, "用户或密码错误", null);
            try {
                json = mapper.writeValueAsString(info);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else {
            if (!("Y".equals(u.getStatus()))){
                //用户未激活
                ResultInfo info = ResultInfoUtils.getRI(false, "账号未激活", null);
                try {
                    json = mapper.writeValueAsString(info);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else {
                //用户已激活
                ResultInfo info = ResultInfoUtils.getRI(true, null, null);
                request.getSession().setAttribute("user", u);
                if (auto_login != null && auto_login.equals("on")){
                    Cookie auto = new Cookie("auto", u.getUsername() + "#" + u.getPassword());
                    auto.setMaxAge(60*60*24);

                    response.addCookie(auto);
                }
                try {
                    json = mapper.writeValueAsString(info);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }

        response.getWriter().write(json);
    }

    //查询用户
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
//        System.out.println("find"+user);
        ObjectMapper mapper = new ObjectMapper();
        if (user != null){
            String json = mapper.writeValueAsString(user);
            response.getWriter().write(json);
        }
    }

    //退出
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        Cookie auto = new Cookie("auto", "");
        auto.setMaxAge(0);

        response.addCookie(auto);
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    //激活页面
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null){

//            UserService us = new UserServiceImpl();
            int c = us.active(code);

            if (c > 0){
                //激活成功
                response.sendRedirect(request.getContextPath()+"/active_ok.html");
            }else {
                //激活失败
                response.sendRedirect(request.getContextPath()+"/active_fail.html");
            }
        }
    }

}
