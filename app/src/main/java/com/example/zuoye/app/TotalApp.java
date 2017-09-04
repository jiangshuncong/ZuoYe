package com.example.zuoye.app;

import android.app.Application;
import android.content.Context;

import com.example.zuoye.api.HttpUrl;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;


/**
 * Created by asus on 2017/8/30.
 */

public class TotalApp extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        initXutils();
        initImageLoader();
        initmob();
        initsan();

    }

    private void initsan() {
        UMShareAPI.get(this);
    }

    {

        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    private void initmob() {
        mContext = this;
        MobSDK.init(this, HttpUrl.mKEY, HttpUrl.mSECRET);
    }


    private void initImageLoader() {
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .build();
        ImageLoaderConfiguration con=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(con);
    }

    private void initXutils() {
        x.Ext.init(this);
        x.Ext.setDebug(false);

    }
}
