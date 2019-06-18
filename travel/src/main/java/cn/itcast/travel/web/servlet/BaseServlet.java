package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.util.ResultInfoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取路径
        String uri = request.getRequestURI();
        //获取方法名
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
//        System.out.println(methodName);
        try {
            //获取方法对象
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //执行方法
            method.invoke(this,request,response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //校验验证码
    public boolean checkCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //校验验证码
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            ResultInfo info = ResultInfoUtils.getRI(false, "验证码有误！", null);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.getWriter().write(json);

            return false;
        }
        return true;
    }

    //将对象封装成字符串
    public String writeValueAsString(Object obj, HttpServletResponse response) throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        return json;
    }

    //将对象序列化为json，并发回客户端
    public void writeValue(Object obj, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(),obj);
    }
}
