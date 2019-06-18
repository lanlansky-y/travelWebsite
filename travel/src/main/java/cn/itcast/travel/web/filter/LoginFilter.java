package cn.itcast.travel.web.filter;

import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();
        if (requestURI.contains("login")  || requestURI.contains(".html") || requestURI.contains(".js") || requestURI.contains("/exitServlet") || requestURI.contains("/css") || requestURI.contains("/fonts") || requestURI.contains("/js") || requestURI.contains("/error") || requestURI.contains("/images") || requestURI.contains("/img") || requestURI.contains("/checkCode")) {

            chain.doFilter(req, resp);

        } else {
            Cookie[] cookies = request.getCookies();
            Cookie auto = getCookie(cookies, "auto");
            if (auto != null) {
                String value = auto.getValue();
                String username = value.split("#")[0];
                String password = value.split("#")[1];
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                UserService us = new UserServiceImpl();
                User u = us.findY(user);
                if (u == null) {
                    chain.doFilter(req, resp);
                } else {
                    session.setAttribute("user", u);
                    chain.doFilter(req, resp);
                }


            } else {
                chain.doFilter(req, resp);
            }
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

    public Cookie getCookie(Cookie[] cookies, String cookie_key) {
        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookie_key)) {
                    return cookie;
                }
            }
        }

        return null;
    }
}
