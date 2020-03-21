package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lenovo.smarttraffic.R;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class TipActivity extends BaseActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_tip;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "出行建议");
    }

}
