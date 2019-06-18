package cn.itcast.travel.util;

import cn.itcast.travel.domain.ResultInfo;

public class ResultInfoUtils {
    private static ResultInfo info = new ResultInfo();


    public static ResultInfo getRI(boolean b,String errorMsg, Object data){
        info.setFlag(b);
        info.setErrorMsg(errorMsg);
        info.setData(data);

        return info;
    }
}
