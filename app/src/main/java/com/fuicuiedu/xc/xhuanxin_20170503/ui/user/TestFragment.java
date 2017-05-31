package com.fuicuiedu.xc.xhuanxin_20170503.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuicuiedu.xc.xhuanxin_20170503.R;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class TestFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_layout,container,false);
        return view;
    }
}
