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

        //addlog是onActivityResult回调方法中的，如果addlog是true表示还没有回调过
        //如果addlog是false表示已经回调过onActivityResult方法了（新闻频道）
        boolean addlog = sp.getBoolean("addlog", true);
        if(addlog){
            //第一次进入进入默认方式(添加第一次的横条)
            initData();
        }else{
            //不是第一次进入取出onActivityResult方法返回的JSON值
            String addmsg = sp.getString("addmsg", null);
            //调用解析JSON方法
            List<ChannelBean> list4 = PindaoJson(addmsg);
            //调用加载横条的方法(横条中的文字发生改变，进行了重置)
            inittwohor(list4);
        }
    }

    //加载控件
    private void initview() {
        //横划菜单栏的＋号
        add = (ImageView) findViewById(R.id.iv_add);
    }

    //加载数据的方法
    private void initData() {
        //创建一个集合用来存储类型名称
        List<String> types=new ArrayList<>();
        //循环存入所有数据(取出typeAll的数据)
            for (int i = 0; i < typeAll.length; i++) {
                //添加进types集合中
                types.add(typeAll[i]);
            }
            //创建Fragment类型集合
            List<Fragment> fragments=new ArrayList<>();
            //for循环对应
            for (int i = 0; i <typeId.length ; i++) {
                //将TopFragment实例化出来
                TopFragment top=new TopFragment();
                //初始化Bundle（存储数据用和map相似）
                Bundle bundle=new Bundle();
                bundle.putString("type",typeId[i]);
                //activity之间传输数据用
                top.setArguments(bundle);
                //添加相应的Fragment
                fragments.add(top);
        }
        //调用horizontalView这个类的getContent方法
        horizontalView.getContent(types,fragments);
    }

    private void pressListenter() {
        iv_more.setOnClickListener(this);//左侧拉菜单图片的点击事件
        iv_user.setOnClickListener(this);//右侧拉菜单图片的点击事件
        add.setOnClickListener(this);//频道加号的点击事件
    }
    //横划
    private void initMenu() {
        //添加做菜单
        //setBehindContentView(R.layout.left_layout);
        menu = new SlidingMenu(this);//因为MainActivity已经继承了AppCompatActivity(单继承多实现)，所以将SlidingMenu实例化出来
        menu.setMenu(R.layout.left_layout);//左布局
        //替换左菜单
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_left,new LeftFragment()).commit();
        //设置相关属性
        // menu = getSlidingMenu();
        //设置左右菜单(如果只有左侧拉只能只写LEFT，如果只有右侧拉只能只写RIGHT，两边都有同时都得写LEFT_RIGHT)
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置边缘滑动
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置滑动后主布局剩余宽度
        menu.setBehindOffsetRes(R.dimen.margin);
        //设置右菜单
        menu.setSecondaryMenu(R.layout.right_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_right,new RightFragment()).commit();
        menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);//使SlidingMenu附加在Activity上
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        //view.getId()判断id值
        switch (view.getId())
        {
            case R.id.iv_user:
                //展示左侧拉
                menu.showMenu();
                break;
            case R.id.iv_more:
                //展示右侧啦
                menu.showSecondaryMenu(true);
                break;
            case R.id.iv_add:
                //取出标识（onActivityResult）确认是否是第一次点击
                boolean addlog = sp.getBoolean("addlog", true);
                //如果不是
                if(!addlog){
                    //直接取出onActivityResult返回存储在sp中的JSON
                        String addmsg = sp.getString("addmsg", null);
                    //解析JSON字符串
                        list2 = PindaoJson(addmsg);
                    //调用方法将上下文和list传进去（详情可点进源码查看）
                    ChannelActivity.startChannelActivity(this,list2);

                }else{
                    //是第一次点击
                    //判断beanList是否为空
                    if(beanList.size() == 0) {
                        //取出原始数据遍历存入ChannelBean bean对象中
                        for (int i = 0; i < typeAll.length; i++) {
                            ChannelBean bean;
                            //全部存为true
                            if (i < typeAll.length) {
                                bean = new ChannelBean(typeAll[i], true);
                            } else {
                                bean = new ChannelBean(typeAll[i], false);
                            }
                            //添加进集合
                            beanList.add(bean);
                        }
                    }
                    //调用方法将上下文和list传进去（详情可点进源码查看）
                    ChannelActivity.startChannelActivity(this,beanList);
                }
                break;
        }

    }

    //回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //存一个标识用来判断是否回调过该方法
        sp.edit().putBoolean("addlog",false).commit();
        //取出回传数据
        String json = data.getStringExtra("json");
        //存一个标识用来存入返回的json值
        sp.edit().putString("addmsg",json).commit();
        System.out.println("============json--"+json);
        //解析json字符串
        list2 = PindaoJson(json);
        //加载横条
        inittwohor(list2);

    }

    //添加横条的方法
    private void inittwohor(List<ChannelBean> list3) {
        //重置list集合(typeAll2存放名字)(typeId2存放类型)
        typeAll2 = new ArrayList<>();
        typeId2 = new ArrayList<>();
        //第二次横条加载
        for (int i = 0; i < list3.size(); i++) {
            //如果list3是选中状态并且对应的Name不为空
            if(list3.get(i).isSelect() &&  !TextUtils.isEmpty(list3.get(i).getName())){
                //新的typeAll2集合存放名字
                typeAll2.add(list3.get(i).getName());
            }
        }

        //利用冒泡排序方法找出名字对应的type值
        for (int i = 0; i < typeAll2.size(); i++) {
            for (int j = 0; j < typeAll.length; j++) {
                String name = typeAll[j];
                if(name.equals(typeAll2.get(i)) && !TextUtils.isEmpty(typeId[j])){
                    //typeId2[num2] = typeId[i];
                    typeId2.add(typeId[j]);
                }
            }
        }

        //新建一个集合
        List<String> types=new ArrayList<>();
        //循环存入所有数据
        for (int i = 0; i < typeAll2.size(); i++) {
            //存入集合中名字
            types.add(typeAll2.get(i));
        }
        //创建Fragment类型集合
        List<Fragment> fragments=new ArrayList<>();
        //for循环对应
        for (int i = 0; i <typeId2.size() ; i++) {
            //实例化TopFragment
            TopFragment top=new TopFragment();
            //创建Bundle用来存取数据
            Bundle bundle=new Bundle();
            //存入形影type值
            bundle.putString("type",typeId2.get(i));
            //存进TopFragment中
            top.setArguments(bundle);
            //添加Fragment
            fragments.add(top);
        }
        //调用horizontalView的getContent方法加载横条
        horizontalView.getContent(types,fragments);
    }

    /**
     * 解析方法
     * @param json
     * @return
     */
    private List<ChannelBean> PindaoJson(String json) {
        //解析json
        Gson gson = new Gson();
        List<ChannelBean>  list2 = gson.fromJson(json,new TypeToken<List<ChannelBean>>(){}
                .getType());

        /*System.out.println("============list2--"+list2.toString());
        for (int i = 0; i < list2.size(); i++) {
            System.out.println(list2.get(i).getName());
        }*/
        //返回一个泛型是ChannelBean的list集合
        return list2;
    }
}
