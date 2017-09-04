package com.example.zuoye.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoye.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EventHandler eventHandler;
    private EditText et_verification;
    private TextView tv_getmsg;
    private TextView tv_send;
    private EditText et_phone;
    private int TIME = 60;
    private ImageView iv_qq;
    private ImageView iv_weibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();
        initregist();

        tv_getmsg.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        iv_qq.setOnClickListener(this);

    }

    private void initview() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_getmsg = (TextView) findViewById(R.id.tv_getmsg);
        tv_send = (TextView) findViewById(R.id.tv_send);
        et_verification = (EditText) findViewById(R.id.et_verification);
        iv_qq = (ImageView) findViewById(R.id.iv_qq);
        iv_weibo = (ImageView) findViewById(R.id.iv_weibo);
    }

    private void initregist() {
        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable) data;
                    final String msg = throwable.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成//提交验证码成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "服务器验证成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "验证码成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                }
            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);

    }


    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_send:
                if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_verification.getText().toString())) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                SMSSDK.submitVerificationCode("86", et_phone.getText().toString(), et_verification.getText().toString());

                break;

            case R.id.tv_getmsg:
                if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                handler.postDelayed(timeRunable, 1000);
                SMSSDK.getVerificationCode("86", et_phone.getText().toString());

                break;

            case R.id.iv_qq:
                //qq方式
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;

            case R.id.iv_weibo:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, umAuthListener);
                break;
        }
    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
           /* if (UMShareAPI.get(LoginActivity.this).isInstall(LoginActivity.this, SHARE_MEDIA.QQ)) {
                Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "no install QQ", Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    final Handler handler = new Handler();

    Runnable timeRunable = new Runnable() {
        @Override
        public void run() {

            TIME--;

            if (TIME == 0) {
                handler.removeCallbacks(this);
                TIME = 5;
                tv_getmsg.setEnabled(true);
                tv_getmsg.setText("再次获取");
            } else {
                tv_getmsg.setEnabled(false);
                tv_getmsg.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv_getmsg.setText(TIME + "s");
                handler.postDelayed(this, 1000);
            }


        }
    };
}
