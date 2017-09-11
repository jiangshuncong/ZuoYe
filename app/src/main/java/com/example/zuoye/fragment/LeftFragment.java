package com.example.zuoye.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zuoye.R;
import com.example.zuoye.activity.LoginActivity;
import com.example.zuoye.activity.MainActivity;

public class LeftFragment extends Fragment implements View.OnClickListener {
    public View view=null;
    private TextView tv;
    private LinearLayout night;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null)
        {
        view=inflater.inflate(R.layout.left_fragment,container,false);
        }
        return view;
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //加载控件

        initview();

        night.setOnClickListener(this);


    }

    private void initview() {
        tv = view.findViewById(R.id.tv_login);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });

        night = view.findViewById(R.id.ll_night);

    }

    @Override
    public void onClick(View view) {

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(currentNightMode==Configuration.UI_MODE_NIGHT_YES){
            ((MainActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            ((MainActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }


}
