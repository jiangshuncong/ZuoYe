package com.example.zuoye.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zuoye.activity.MainActivity;
import com.example.zuoye.adapter.MyAdapter;
import com.example.zuoye.R;
import com.example.zuoye.api.HttpUrl;
import com.example.zuoye.bean.News;
import com.example.zuoye.utils.NetWorkInfoUtils;
import com.google.gson.Gson;
import com.kson.slidingmenu.SlidingMenu;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import view.xlistview.XListView;

/**
 * Created by asus on 2017/8/31.
 */

public class TopFragment extends Fragment implements XListView.IXListViewListener {

    private SlidingMenu menu;
    private View view;
    private SharedPreferences sp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.top_fragment,null);
        return view;
    }
//    @ViewInject(R.id.xlv)
    XListView lv_news;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv_news=view.findViewById(R.id.xlv);
        lv_news.setPullRefreshEnable(true);
        lv_news.setPullLoadEnable(true);
        lv_news.setXListViewListener(this);

        initnet();
        sp = getActivity().getSharedPreferences("msg", Context.MODE_PRIVATE);
        loadNews();

    }

    private void loadNews() {
        //获得activity传过来的参数
        Bundle bundle = getArguments();
        String type = bundle.getString("type");

        RequestParams params=new RequestParams(HttpUrl.POST_URL);
        params.addBodyParameter("key",HttpUrl.KEY);
        params.addBodyParameter("type",type);
        //post请求
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(result!=null)
                {
                    System.out.println(result);
                    //解析
                    pareseData(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void pareseData(String data) {
        Gson gson=new Gson();
        News news = gson.fromJson(data, News.class);
        List<News.ResultBean.DataBean> data1 = news.getResult().getData();
        MyAdapter adapter=new MyAdapter((MainActivity) getActivity(),data1);
        lv_news.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    //判断网络记录加载方式的方fa
    private void initnet() {

        //实例化对象
        new NetWorkInfoUtils().getnet(getContext(), new NetWorkInfoUtils.NetWork() {
            @Override
            public void Wifivisible() {
                //有WiFi网络加载大图
                sp.edit().putBoolean("hasnet",true).commit();
            }

            @Override
            public void Unnetvisible() {
                //不加载图片
                sp.edit().putBoolean("hasnet",false).commit();
            }

            @Override
            public void NetMobilevisible() {
                //手机数据网络自定义

            }
        });
    }

}
