package com.example.zuoye.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.zuoye.R;
import com.example.zuoye.activity.OffLineActivity;
import com.example.zuoye.utils.NetWorkInfoUtils;

public class RightFragment extends Fragment implements View.OnClickListener {
    public View view=null;
    private RelativeLayout net;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null)
        {
            view=inflater.inflate(R.layout.right_fragment,container,false);
        }
                return view;
            }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initnet();
        RelativeLayout lixian = view.findViewById(R.id.rl_lixian);
        net = view.findViewById(R.id.rl_net);

        lixian.setOnClickListener(this);
        net.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_lixian:
                startActivity(new Intent(getActivity(), OffLineActivity.class));
                break;

            case R.id.rl_net:
                new AlertDialog.Builder(getContext()).setSingleChoiceItems(new String[]{"大图", "无图"}, 0, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i == 0) {

                            //需要存储本地状态，选择的什么网络流量节省方式，"wifi"="hasnet"，加载大图


                        } else if (i == 1) {
                            //需要存储本地状态，选择的什么网络流量节省方式，"wifi"="nonet"，不加载图

                        }

                        dialogInterface.dismiss();
                    }
                }).show();
                break;

        }

    }


    //判断网络记录加载方式的方fa
    private void initnet() {

        //实例化对象
        new NetWorkInfoUtils().getnet(getContext(), new NetWorkInfoUtils.NetWork() {
            @Override
            public void Wifivisible() {
                //有WiFi网络加载大图
            }

            @Override
            public void Unnetvisible() {
                //不加载图片

            }

            @Override
            public void NetMobilevisible() {
                //手机数据网络自定义

            }
        });
    }
}
