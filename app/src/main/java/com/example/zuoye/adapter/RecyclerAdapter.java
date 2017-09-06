package com.example.zuoye.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.zuoye.R;
import com.example.zuoye.activity.OffLineActivity;
import com.example.zuoye.bean.LXBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蒋順聪 on 2017/9/5.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private OnItemClickListener onItemClickListener;
    private Context context;
    private List<LXBean> list ;

    public RecyclerAdapter(Context context, List<LXBean> list) {
        this.context = context;
        this.list = list;
    }

    //加载布局
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item, null);

        MyViewHolder holder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClickListener((Integer) view.getTag(),view);
            }
        });

        return holder;
    }

    /**
     * 處理邏輯繪製UI
     * @param holder
     * @param position
     */
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemname.setText(list.get(position).name);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private final CheckBox itemcb;
        private final TextView itemname;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemname = itemView.findViewById(R.id.tv_itemname);
            itemcb = itemname.findViewById(R.id.cb_itemcb);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener{
        void onItemClickListener(int position,View view);
    }

}
