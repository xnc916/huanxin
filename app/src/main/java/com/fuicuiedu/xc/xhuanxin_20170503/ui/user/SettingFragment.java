package com.fuicuiedu.xc.xhuanxin_20170503.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fuicuiedu.xc.xhuanxin_20170503.MyHelper;
import com.fuicuiedu.xc.xhuanxin_20170503.R;
import com.fuicuiedu.xc.xhuanxin_20170503.ui.SplashActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class SettingFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_logout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.btn_Logout)
    public void loginout(){
        final ProgressDialog pb = new ProgressDialog(getContext());
        pb.setMessage("正在退出......");
        pb.show();

        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                pb.dismiss();
                MyHelper.setLogin(false);
                startActivity(new Intent(getContext(),SplashActivity.class));
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                handler.sendEmptyMessage(0);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getContext(), "退出失败", Toast.LENGTH_SHORT).show();
        }
    };
}
