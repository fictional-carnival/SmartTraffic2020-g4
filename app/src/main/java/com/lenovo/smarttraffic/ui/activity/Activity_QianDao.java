package com.lenovo.smarttraffic.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_QianDao extends BaseActivity{
    @Override
    protected int getLayout() {
        return R.layout.activity_qiandao;
    }

    private ProgressBar progressBar;
    private Timer timer;
    private TimerTask timerTask;
    private int status = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    progressBar.setVisibility(View.GONE);
                    initWeb();
                    break;
            }
        }
    };
    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar),true,"用户签到");
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);
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

    @SuppressLint("JavascriptInterface")
    public void initWeb(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        webView.addJavascriptInterface(new App(),"app");
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    class App{
        @JavascriptInterface
        public void jifen(){
            startActivity(new Intent(getApplicationContext(),Activity_QianDao2.class));
        }
        @JavascriptInterface
        public String user(String type){
            switch (type){
                case "pname":
                    return InitApp.getUser("username",InitApp.sp.getString("UserName",null)).getPname();
                case "ptel":
                    return InitApp.getUser("username",InitApp.sp.getString("UserName",null)).getPtel();
            }
            return "您未登录";
        }
    }

}
