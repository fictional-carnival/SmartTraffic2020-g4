package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Ditu;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class ParkInfoActivity extends BaseActivity {

    private int p;
    private TextView tv_2;
    private TextView tv_1;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private Ditu.ROWSDETAILBean ditu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = getIntent().getIntExtra("i", p);
        ditu = new Gson().fromJson(InitApp.sp.getString("ditu", ""), Ditu.class).getROWS_DETAIL().get(p);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_parkinfo;
    }
    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "停车场详情");
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_4 = findViewById(R.id.tv_4);
        tv_1.setText(ditu.getName());
        tv_2.setText(ditu.getAddress());
        tv_3.setText(ditu.getDistance()+"米");
        tv_4.setText(ditu.getEmptySpace() + "个/" + ditu.getAllSpace()+"个");
    }

    private void InitData() {
        BasePagerAdapter basePagerAdapter = new BasePagerAdapter(getSupportFragmentManager());

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
