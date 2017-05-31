package com.fuicuiedu.xc.xhuanxin_20170503.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.fuicuiedu.xc.xhuanxin_20170503.MyHelper;
import com.fuicuiedu.xc.xhuanxin_20170503.R;
import com.fuicuiedu.xc.xhuanxin_20170503.ui.user.LoginActivity;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //1.5s跳转到相应位置
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //判断登录状态，决定跳转的位置
                Intent intent;
                if (MyHelper.getLogin()){
                    //两个方法是为了保证进入主页面后本地会话和群组都 load 完毕。
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    //跳转到主页
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                }else{
                    //跳转到登录页
                    intent = new Intent(SplashActivity.this,LoginActivity.class);
                }

                //注册环信连接状态监听
                EMClient.getInstance().addConnectionListener(new MyConnectionListener());


                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    //实现环信连接状态监听
    private class MyConnectionListener implements EMConnectionListener{
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int errorCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(errorCode == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        Toast.makeText(SplashActivity.this, "显示帐号已经被移除", Toast.LENGTH_SHORT).show();
                    }else if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        Toast.makeText(SplashActivity.this, "显示帐号在其他设备登录", Toast.LENGTH_SHORT).show();
                        // TODO: 2017/5/10 0010 当前账号踢出 ，跳入欢迎页
                    } else {
                        if (NetUtils.hasNetwork(SplashActivity.this)){
                            //连接不到聊天服务器
                            Toast.makeText(SplashActivity.this, "连接不到聊天服务器", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //当前网络不可用，请检查网络设置
                            Toast.makeText(SplashActivity.this, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }
}
