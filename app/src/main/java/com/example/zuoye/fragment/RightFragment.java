package com.example.zuoye.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.zuoye.R;

public class RightFragment extends Fragment {
    public View view=null;
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

        RelativeLayout lixian = view.findViewById(R.id.rl_lixian);

    }
}
