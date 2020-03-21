package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;


public class ViolationPayActivity extends BaseActivity {

    private TextView tv_tip;
    private ImageView im_erweima;
    private String str;
    private int i;
    private JSONArray violations;
    private long index;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        str = getIntent().getStringExtra("username");
        i = getIntent().getIntExtra("number",0);
        InitView();
        initAction();

    }

    private void initAction() {
        try {
            violations = new JSONArray(InitApp.sp.getString(str, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        im_erweima.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    tv_tip.setText(violations.getJSONObject(i).getString("carnumber")+",付款金额="+violations.getJSONObject(i).getString("pmoney")+"元");
                    violations.getJSONObject(i).put("pchuli", 2);
                    InitApp.edit.putString(str, violations.toString()).commit();
                    InitApp.toast("付款成功！");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        index++;
                        if (index % 2 == 0) {
                            im_erweima.setImageResource(R.mipmap.icon_erweima);

                        } else {
                            im_erweima.setImageResource(R.drawable.erweima2);
                        }

                    }
                });
            }
        }, 5000, 5000);

    }
    @Override
    protected int getLayout() {
        return R.layout.activity_violation_pay;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "违章支付");
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        im_erweima = (ImageView) findViewById(R.id.im_er);
    }
    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }



}
