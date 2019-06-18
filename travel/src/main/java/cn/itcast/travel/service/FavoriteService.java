package cn.itcast.travel.service;

public interface FavoriteService {
    boolean isFavExist(int uid,int rid);

    int addFavorite(int uid, int rid);

    int delFavorite(int uid, int parseInt);
}
