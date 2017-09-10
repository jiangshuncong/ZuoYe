package com.example.zuoye.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.city_picker.CityListActivity;
import com.example.zuoye.R;
import com.example.zuoye.activity.OffLineActivity;
import com.example.zuoye.utils.NetWorkInfoUtils;

public class RightFragment extends Fragment implements View.OnClickListener {
    public View view=null;
    private RelativeLayout net;
    private SharedPreferences sp;
    private LinearLayout city;

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

        sp = getActivity().getSharedPreferences("msg", Context.MODE_PRIVATE);

        RelativeLayout lixian = view.findViewById(R.id.rl_lixian);
        net = view.findViewById(R.id.rl_net);
        city = view.findViewById(R.id.ll_city);

        lixian.setOnClickListener(this);
        net.setOnClickListener(this);
        city.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_lixian:
                startActivity(new Intent(getActivity(), OffLineActivity.class));
                break;

            case R.id.rl_net:
                new AlertDialog.Builder(getContext()).setSingleChoiceItems(new String[]{"加载图片", "不加载图片"}, 0, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {

                            //需要存储本地状态，选择的什么网络流量节省方式，"wifi"="hasnet"，加载大图
                            sp.edit().putBoolean("hasnet",true).commit();
                            boolean hasnet = sp.getBoolean("hasnet", false);
                            Toast.makeText(getContext(),hasnet+"=================", Toast.LENGTH_SHORT).show();

                        } else if (i == 1) {
                            //需要存储本地状态，选择的什么网络流量节省方式，"wifi"="nonet"，不加载图
                            sp.edit().putBoolean("hasnet",false).commit();

                            boolean hasnet = sp.getBoolean("hasnet", false);
                            Toast.makeText(getContext(),hasnet+"=================", Toast.LENGTH_SHORT).show();

                        }

                        dialogInterface.dismiss();
                    }
                }).show();
                break;

            case R.id.ll_city:
                    startActivity(new Intent(getActivity(),CityListActivity.class));
                break;

        }

    }

}
