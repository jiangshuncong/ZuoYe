package com.example.zuoye.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.zuoye.R;
import com.example.zuoye.adapter.RecyclerAdapter;
import com.example.zuoye.api.HttpUrl;
import com.example.zuoye.bean.LXBean;
import com.example.zuoye.sql.MySQLite;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class OffLineActivity extends AppCompatActivity {

    private RecyclerView listview;
    private Button btn;
    List<LXBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_line);

        //获取控件
        initview();
        //添加条目数据
        inititemdata();
    }

    private void inititemdata() {

        /*LXBean lx = new LXBean("top","推荐",false);
        list.add(lx);
        lx = new LXBean("top","热点",false);
        list.add(lx);
        lx = new LXBean("top","军事",false);
        list.add(lx);
        lx = new LXBean("top","关注",false);
        list.add(lx);
        lx = new LXBean("top","国际",false);
        list.add(lx);
        lx = new LXBean("top","视频",false);
        list.add(lx);
        lx = new LXBean("top","问答",false);
        list.add(lx);
        lx = new LXBean("top","科技",false);
        list.add(lx);
        lx = new LXBean("top","汽车",false);
        list.add(lx);*/

        LXBean lx = new LXBean();
        lx.type = "top";
        lx.name = "推荐";
        list.add(lx);
        lx = new LXBean();
        lx.type = "top";
        lx.name = "热点";
        list.add(lx);
        lx = new LXBean();
        lx.type = "top";
        lx.name = "军事";
        list.add(lx);
        lx = new LXBean();
        lx.type = "top";
        lx.name = "关注";
        list.add(lx);
        lx = new LXBean();
        lx.type = "top";
        lx.name = "国际";
        list.add(lx);
        lx = new LXBean();
        lx.type = "top";
        lx.name = "问答";
        list.add(lx);
        lx = new LXBean();
        lx.type = "top";
        lx.name = "科技";
        list.add(lx);


        RecyclerAdapter adapter = new RecyclerAdapter(this,list);
        listview.setLayoutManager(new LinearLayoutManager(this));
        listview.setAdapter(adapter);

        //点击事件
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                //获取控件
                CheckBox cb  = view.findViewById(R.id.cb_itemcb);
                //获取当前list对象
                LXBean lxBean = list.get(position);
                if(cb.isChecked()){
                    cb.setChecked(false);
                    //lxBean.setState(false);
                    lxBean.state = false;
                }else{
                    cb.setChecked(true);
                    //lxBean.setState(true);
                    lxBean.state = true;
                }
                //修改list数据
                list.set(position,lxBean);
            }
        });
    }

    private void initview() {
        listview = (RecyclerView) findViewById(R.id.rl_list);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list != null && list.size() > 0) {
                    LXBean lx = new LXBean();
                    for (int i = 0; i < list.size(); i++) {
                        if (lx.state) {//判断是否是选中状态，如果选中则执行下载操作
                            loadData(lx.type);
                        }
                    }
                }

                for (LXBean catogray : list) {

                    System.out.println("state====" + catogray.state);
                    System.out.println("num====" + list.size());

                }

            }
        });

    }


    private void loadData(final String type) {
        RequestParams params = new RequestParams(HttpUrl.POST_URL);
        params.addQueryStringParameter("key", HttpUrl.KEY);
        //params.addParameter("type", type);
        x.http().get(params, new Callback.CommonCallback<String>() {
            public void onSuccess(String result) {
                //下载成功后保存到数据库
                saveData(type, result);
                System.out.println("======================="+result);

            }

            public void onError(Throwable ex, boolean isOnCallback) {

            }

            public void onCancelled(CancelledException cex) {

            }

            public void onFinished() {

            }
        });

    }

    private void saveData(String type, String result) {

        MySQLite sql = new MySQLite(this);
        SQLiteDatabase sd = sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type",type);
        values.put("name",result);
        sd.insert("tb",null,values);
    }

}
