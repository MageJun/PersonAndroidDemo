package com.zed3.sipua.ui.group;

import android.os.Bundle;

import com.lw.demo.adnroid.samples.R;
import com.zed3.sipua.ui.BaseActivity;

public class IdelStateActivity extends BaseActivity {
    @Override
    public boolean isShowTitleLeftMenu() {
        return true;
    }


    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
            setContentView(R.layout.xydj_activity_idle_state);
    }
}
