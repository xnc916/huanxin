package com.fuicuiedu.xc.xhuanxin_20170503.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fuicuiedu.xc.xhuanxin_20170503.MyHelper;
import com.fuicuiedu.xc.xhuanxin_20170503.R;
import com.fuicuiedu.xc.xhuanxin_20170503.ui.MainActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText usernameEditaText;
    @BindView(R.id.password)
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.login_btn_register, R.id.login_btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            //跳转到注册页
            case R.id.login_btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            //执行登录操作
            case R.id.login_btn_login:
                String username = usernameEditaText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                final ProgressDialog pb = new ProgressDialog(this);
                pb.setMessage("正在登录......");
                pb.show();

                //执行环信的登录操作
                EMClient.getInstance().login(username, password, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        pb.dismiss();
                        //两个方法是为了保证进入主页面后本地会话和群组都 load 完毕。
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        //保存登录状态到本地配置中
                        MyHelper.setLogin(true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        pb.dismiss();
                        handler.sendEmptyMessage(0);
                    }
                });

                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    };
}
