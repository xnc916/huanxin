package com.fuicuiedu.xc.xhuanxin_20170503.ui.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fuicuiedu.xc.xhuanxin_20170503.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ChatActivity extends AppCompatActivity {

    //启动当前activty，需要传两个参数
    public static void open(Context context,String chatId){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_SINGLE);//单聊
        intent.putExtra(EaseConstant.EXTRA_USER_ID,chatId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE,0);
        String chatId = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);

        //添加聊天的Fragment
        //new出EaseChatFragment或其子类的实例
        EaseChatFragment chatFragment = new EaseChatFragment();
        //传入参数
        Bundle args = new Bundle();
        //聊天的类型。。。。单人（一对一），群聊，聊天组
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        //具体的聊天对象（Id）
        args.putString(EaseConstant.EXTRA_USER_ID, chatId);

        chatFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_container, chatFragment)
                .commit();
    }
}
