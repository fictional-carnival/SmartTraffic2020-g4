package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.lenovo.smarttraffic.R;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class ChannelActivity extends BaseActivity{

    private RecyclerView recyclerView;

    @Override
    protected int getLayout() {
        return R.layout.activity_channel;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.title_item_main));
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initData() {
    }

}
