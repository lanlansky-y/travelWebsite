package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);
        List<Category> list;
        if (categories == null || categories.size() == 0){
            //从数据库中获取
//            System.out.println("从数据库中获取");
            list = categoryDao.findAll();
            for (Category category : list) {
                jedis.zadd("category",category.getCid(),category.getCname());
            }
        }else {
//            System.out.println("从redis中获取");
            list = new ArrayList<Category>();

            for (Tuple category : categories) {
                Category cg = new Category();
                cg.setCid((int) category.getScore());
                cg.setCname(category.getElement());
                list.add(cg);
            }

        }
        return list;

    }
}
