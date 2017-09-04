package com.example.zuoye.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.zuoye.R;
import com.example.zuoye.fragment.LeftFragment;
import com.example.zuoye.fragment.RightFragment;
import com.example.zuoye.fragment.TopFragment;
import com.example.zuoye.view.HorizontalView;
import com.kson.slidingmenu.SlidingMenu;
import com.kson.slidingmenu.app.SlidingFragmentActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener {
    private SlidingMenu menu;

    @ViewInject(R.id.iv_user)
    ImageView iv_user;
    @ViewInject(R.id.iv_more)
    ImageView iv_more;

    @ViewInject(R.id.horizontal_scolled)
    HorizontalView horizontalView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        x.view().inject(MainActivity.this);

        initMenu();
        pressListenter();
        initData();
    }

    private void initData() {
        List<String> types=new ArrayList<>();
        //,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
        String[] typeAll={"头条","社会","国内","国际","娱乐","体育","军事","科技","财经","时尚"};
        for (int i = 0; i < typeAll.length; i++) {
            types.add(typeAll[i]);
        }
        List<Fragment> fragments=new ArrayList<>();
        String[] typeId={"top","shehui","guonei","guoji","yule","tiyu","junshi","keji","caijing","shishang",};
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
    }

    //横划
    private void initMenu() {
        //添加做菜单
        setBehindContentView(R.layout.left_layout);
        //替换左菜单
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_left,new LeftFragment()).commit();

        //设置相关属性
        menu = getSlidingMenu();
        //设置左右菜单
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置边缘滑动
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置滑动后主布局剩余宽度
        menu.setBehindOffsetRes(R.dimen.margin);
        //设置右菜单
        menu.setSecondaryMenu(R.layout.right_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_right,new RightFragment()).commit();

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
        }
    }
}
