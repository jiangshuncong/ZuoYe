package com.example.zuoye.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 蒋順聪 on 2017/9/5.
 */

public class NetWorkInfoUtils {

    private Context context;

    //方法调用接口
    public void getnet(Context context,NetWork netWork) {
        this.context = context;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取当前网络状态
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        //判断网络状态
        if(info != null){
            if(info.getType() == ConnectivityManager.TYPE_WIFI){
                //网络是有网WiFi状态
                netWork.Wifivisible();
            }else if(info.getType() == ConnectivityManager.TYPE_MOBILE){
                //手机数据网络
                netWork.NetMobilevisible();
            }else{
                //无网络状态
                netWork.Unnetvisible();
            }
        }else{
            //无网络状态
            netWork.Unnetvisible();
        }

    }

    //接口回调
    public interface NetWork{

        //有WIfi时的逻辑
        void Wifivisible();

        //无网络状态
        void Unnetvisible();

        //数据网络逻辑
        void NetMobilevisible();

    }

}
