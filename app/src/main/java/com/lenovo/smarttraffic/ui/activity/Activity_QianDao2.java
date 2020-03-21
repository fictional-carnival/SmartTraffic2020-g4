package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_QianDao2 extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_qiandao2;
    }

    private ProgressBar progressBar;
    private TimerTask timerTask;
    private Timer timer;
    private int status;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    progressBar.setVisibility(View.GONE);
                    qiandao_img.setVisibility(View.VISIBLE);
                    click();
                    break;
            }
        }
    };
    private ImageView qiandao_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar),true,"积分签到");
        progressBar = findViewById(R.id.progressBar);
        qiandao_img = findViewById(R.id.qiandao_img);
        qiandao();
    }
    public void qiandao(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                status = status + 5;
                progressBar.setProgress(status);
                if (status == 99){
                    progressBar.setProgress(status);
                    handler.sendEmptyMessage(0);
                }else if (status == 100){
                    progressBar.setProgress(status);
                    handler.sendEmptyMessage(0);
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,20);
    }

    public void click(){
        qiandao_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InitApp.sp.getBoolean("isQianDao",false)){
                    InitApp.toast("您已领取");
                }else {
                    InitApp.edit.putBoolean("isQianDao",true).commit();
                    InitApp.toast("签到有彩蛋，积分+100");
                }
            }
        });
    }
}
