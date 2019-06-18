package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.ResultInfoUtils;
import cn.itcast.travel.util.UuidUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserServiceImpl implements UserService {

    private UserDao dao = new UserDaoImpl();

    @Override
    public String regist(User user) {
        //1.根据用户名查询用户对象
        User u = dao.findUser(user);
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        //判断u是否为null
        if (u!=null){
            //用户名存在，注册失败
            ResultInfo info = ResultInfoUtils.getRI(false, "用户名已存在！", null);
            try {
                json = mapper.writeValueAsString(info);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        }
        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        String uuid = UuidUtil.getUuid();
        user.setCode(uuid);
        //2.2设置激活状态
        user.setStatus("N");
        int save = dao.save(user);
        ResultInfo info = ResultInfoUtils.getRI(true, null, null);
        try {
            json = mapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //3.激活邮件发送，邮件正文？

        String content="<a href='http://localhost:8080/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";

        MailUtils.sendMail(user.getEmail(),content,"激活邮件");

        return json;
    }

    @Override
    public int active(String code) {

        return dao.findByCode(code);
    }

    @Override
    public User login(User user) {

        User u = dao.findUserByUsernameAndPassword(user);
        return u;
    }

    @Override
    public User findY(User user) {


        return dao.findYByUsernameAndPassword(user);
    }
}
