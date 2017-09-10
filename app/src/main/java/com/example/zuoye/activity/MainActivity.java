package com.example.zuoye.activity;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.andy.library.ChannelActivity;
        import com.andy.library.ChannelBean;
        import com.example.zuoye.R;
        import com.example.zuoye.bean.PindaoJson;
        import com.example.zuoye.fragment.LeftFragment;
        import com.example.zuoye.fragment.RightFragment;
        import com.example.zuoye.fragment.TopFragment;
        import com.example.zuoye.utils.NetWorkInfoUtils;
        import com.example.zuoye.view.HorizontalView;
        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;
        import com.kson.slidingmenu.SlidingMenu;
        import com.kson.slidingmenu.app.SlidingFragmentActivity;

        import org.json.JSONObject;
        import org.xutils.view.annotation.ContentView;
        import org.xutils.view.annotation.ViewInject;
        import org.xutils.x;

        import java.util.ArrayList;
        import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SlidingMenu menu;

    @ViewInject(R.id.iv_user)
    ImageView iv_user;
    @ViewInject(R.id.iv_more)
    ImageView iv_more;

    @ViewInject(R.id.horizontal_scolled)
    HorizontalView horizontalView;
    private ImageView add;
    private List<ChannelBean> beanList = new ArrayList<>();
    String[] typeAll={"头条","社会","国内","国际","娱乐","体育","军事","科技","财经","时尚"};
    String[] typeId={"top","shehui","guonei","guoji","yule","tiyu","junshi","keji","caijing","shishang"};
    private SharedPreferences sp;
    private List<ChannelBean> list2;
    private List<String> typeAll2;
    private List<String> typeId2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        x.view().inject(MainActivity.this);
        //创建 sp
        sp = getSharedPreferences("msg", MODE_PRIVATE);
        //hasnet判断网络状态
        sp.edit().putBoolean("hasnet",true).commit();
        //加载控件
        initview();
        //加载菜单栏
        initMenu();
        //点击事件
        pressListenter();
        //加载新闻数据
        boolean addlog = sp.getBoolean("addlog", true);
        if(addlog){
            initData();
        }else{
            String addmsg = sp.getString("addmsg", null);
            List<ChannelBean> list4 = PindaoJson(addmsg);
            inittwohor(list4);
        }
    }

    //加载控件
    private void initview() {
        //横划菜单栏的＋号
        add = (ImageView) findViewById(R.id.iv_add);
    }

    private void initData() {
        //创建一个集合用来存储类型名称
        List<String> types=new ArrayList<>();
        //循环存入所有数据
            for (int i = 0; i < typeAll.length; i++) {
                types.add(typeAll[i]);
            }
            //创建Fragment类型集合
            List<Fragment> fragments=new ArrayList<>();
            //for循环对应
            for (int i = 0; i <typeId.length ; i++) {
                TopFragment top=new TopFragment();
                Bundle bundle=new Bundle();
                bundle.putString("type",typeId[i]);
                top.setArguments(bundle);
                fragments.add(top);
        }
        horizontalView.getContent(types,fragments);
    }

    private void pressListenter() {
        iv_more.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    //横划
    private void initMenu() {
        //添加做菜单
        //setBehindContentView(R.layout.left_layout);
        menu = new SlidingMenu(this);
        menu.setMenu(R.layout.left_layout);
        //替换左菜单
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_left,new LeftFragment()).commit();

        //设置相关属性
        // menu = getSlidingMenu();
        //设置左右菜单
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置边缘滑动
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置滑动后主布局剩余宽度
        menu.setBehindOffsetRes(R.dimen.margin);
        //设置右菜单
        menu.setSecondaryMenu(R.layout.right_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_right,new RightFragment()).commit();
        menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_user:
                menu.showMenu();
                break;
            case R.id.iv_more:
                menu.showSecondaryMenu(true);
                break;
            case R.id.iv_add:
                boolean addlog = sp.getBoolean("addlog", true);
                if(!addlog){
                        String addmsg = sp.getString("addmsg", null);
                        list2 = PindaoJson(addmsg);

                    ChannelActivity.startChannelActivity(this,list2);

                }else{
                    if(beanList.size() == 0) {
                        for (int i = 0; i < typeAll.length; i++) {
                            ChannelBean bean;
                            if (i < typeAll.length) {
                                bean = new ChannelBean(typeAll[i], true);
                            } else {
                                bean = new ChannelBean(typeAll[i], false);
                            }
                            beanList.add(bean);
                        }
                    }
                    ChannelActivity.startChannelActivity(this,beanList);
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        sp.edit().putBoolean("addlog",false).commit();
        String json = data.getStringExtra("json");
        sp.edit().putString("addmsg",json).commit();
        System.out.println("============json--"+json);
        list2 = PindaoJson(json);

        //加载横条
        inittwohor(list2);

    }

    private void inittwohor(List<ChannelBean> list3) {
        typeAll2 = new ArrayList<>();
        typeId2 = new ArrayList<>();
        //第二次横条加载
        for (int i = 0; i < list3.size(); i++) {
            if(list3.get(i).isSelect() &&  !TextUtils.isEmpty(list3.get(i).getName())){
                //typeAll2[num] = list3.get(i).getName();
                typeAll2.add(list3.get(i).getName());
            }
        }

        /*for (int i = 0; i < typeAll.length; i++) {
            for (int j = 0; j < typeAll2.size(); j++) {
                String name = typeAll[i];
                if(name.equals(typeAll2.get(j)) && !TextUtils.isEmpty(typeId[i])){
                    //typeId2[num2] = typeId[i];
                    typeId2.add(typeId[i]);
                }
            }
        }*/

        for (int i = 0; i < typeAll2.size(); i++) {
            for (int j = 0; j < typeAll.length; j++) {
                String name = typeAll[j];
                if(name.equals(typeAll2.get(i)) && !TextUtils.isEmpty(typeId[j])){
                    //typeId2[num2] = typeId[i];
                    typeId2.add(typeId[j]);
                }
            }
        }


       /* for (int i = 0; i < typeAll2.size(); i++) {
            System.out.println("--------typeAll2----"+typeAll2.get(i));
            System.out.println("--------typeAll2.length----"+typeAll2.size());

        }


        for (int i = 0; i < typeId2.size(); i++) {
            System.out.println("--------typeId2----"+typeId2.get(i));
        }*/

        List<String> types=new ArrayList<>();
        //循环存入所有数据
        for (int i = 0; i < typeAll2.size(); i++) {
            types.add(typeAll2.get(i));
        }
        //创建Fragment类型集合
        List<Fragment> fragments=new ArrayList<>();
        //for循环对应
        for (int i = 0; i <typeId2.size() ; i++) {
            TopFragment top=new TopFragment();
            Bundle bundle=new Bundle();
            bundle.putString("type",typeId2.get(i));
            top.setArguments(bundle);
            fragments.add(top);
        }
        horizontalView.getContent(types,fragments);
    }

    private List<ChannelBean> PindaoJson(String json) {
        //解析json
        Gson gson = new Gson();
        List<ChannelBean>  list2 = gson.fromJson(json,new TypeToken<List<ChannelBean>>(){}
                .getType());

        System.out.println("============list2--"+list2.toString());
        for (int i = 0; i < list2.size(); i++) {
            System.out.println(list2.get(i).getName());
        }
        return list2;
    }
}
