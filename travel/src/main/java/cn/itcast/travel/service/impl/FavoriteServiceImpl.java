package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public boolean isFavExist(int uid, int rid) {

        Favorite favorite = favoriteDao.findFavByUidAndRid(uid, rid);
        if (favorite != null ){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public int addFavorite(int uid, int rid) {
        int i = favoriteDao.addFavorite(uid,rid);
        if (i == 0){
            return -1;
        }else {
            return favoriteDao.findCountByRid(rid);
        }
    }

    @Override
    public int delFavorite(int uid, int rid) {
        int i = favoriteDao.delFavorite(uid,rid);
        if (i == 0){
            return -1;
        }else {
            return favoriteDao.findCountByRid(rid);
        }

    }
}
