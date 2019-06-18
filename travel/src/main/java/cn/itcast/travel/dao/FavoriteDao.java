package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    Favorite findFavByUidAndRid(int uid,int rid);

    int findCountByRid(int rid);

    int addFavorite(int uid, int rid);

    int delFavorite(int uid, int rid);
}
