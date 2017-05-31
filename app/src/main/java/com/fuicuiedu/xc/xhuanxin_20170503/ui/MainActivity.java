package com.fuicuiedu.xc.xhuanxin_20170503.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fuicuiedu.xc.xhuanxin_20170503.MyHelper;
import com.fuicuiedu.xc.xhuanxin_20170503.R;
import com.fuicuiedu.xc.xhuanxin_20170503.ui.user.ChatActivity;
import com.fuicuiedu.xc.xhuanxin_20170503.ui.user.SettingFragment;
import com.fuicuiedu.xc.xhuanxin_20170503.ui.user.TestFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_vp)
    ViewPager viewPager;
    private List<String> mContacts = new ArrayList<>();
    private EaseContactListFragment mContactListFragment;
    @BindView(R.id.main_edit)EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    private FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                //会话
                case 0:
                    //显示会话列表页面
                    EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
                    conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                        @Override
                        public void onListItemClicked(EMConversation conversation) {
                            //点击某个人，跳转到聊天页面
                            ChatActivity.open(MainActivity.this,conversation.conversationId());
                        }
                    });
                    return conversationListFragment;
                // 通讯录
                case 1:
                    //显示通讯录页面
                    mContactListFragment = new EaseContactListFragment();
                    //需要设置联系人列表才能启动fragment
                    asyncGetContactsFromServer();
                    //设置item点击事件
                    mContactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
                        @Override
                        public void onListItemClicked(EaseUser user) {
                            //跳转到聊天页面
                            ChatActivity.open(MainActivity.this,user.getUsername());
                        }
                    });

                    return mContactListFragment;
                //设置
                case 2:
                    //显示设置页面
                    return new SettingFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    @OnClick({R.id.btn_conversation, R.id.btn_address_list, R.id.btn_setting,R.id.main_start_chat})
    public void onClick(View view) {
        switch (view.getId()) {
            //会话
            case R.id.btn_conversation:
                //显示会话页面
                viewPager.setCurrentItem(0);
                break;
            //通讯录
            case R.id.btn_address_list:
                //显示通讯录页面
                viewPager.setCurrentItem(1);
                break;
            //设置
            case R.id.btn_setting:
                //显示设置页面
                viewPager.setCurrentItem(2);
                break;
            //发起会话
            case R.id.main_start_chat:// 跳转到聊天页面
                ChatActivity.open(MainActivity.this,editText.getText().toString());
                break;
        }
    }

    //异步获取联系人
    private void asyncGetContactsFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //从环信服务器获取到所有联系人
                    mContacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //刷新联系人
                    refreshContacts();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //刷新联系人
    private void refreshContacts(){
        HashMap<String,EaseUser> hashmap = new HashMap<>();
        for (String hxId:mContacts){
            EaseUser user = new EaseUser(hxId);
            hashmap.put(hxId,user);
        }
        mContactListFragment.setContactsMap(hashmap);
        mContactListFragment.refresh();
    }
}
