package com.lenovo.smarttraffic.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Weather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_Weather extends BaseActivity{
    @Override
    protected int getLayout() {
        return R.layout.activity_weather;
    }

    private GridView grid_top,grid_bottom;
    private TextView time,week,temp,type;
    private ImageView weather_img;
    private LineChart lineChart;
    private String[] str = new String[]{"紫外线指数","空气污染指数","运动指数","穿衣指数","感冒指数","洗车指数"};
    private int[] str_img = new int[]{R.drawable.zwx,R.drawable.shuye,R.drawable.ren,
            R.drawable.yifu,R.drawable.yao,R.drawable.che};
    private List<Entry> entries1;
    private List<Entry> entries2;
    private List<Weather.ROWSDETAILBean> weathterlist;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    grid_bottom.setAdapter(new grid_bottom_Adapter());
                    break;
            }
        }
    };
    private int random;
    private Timer timer;
    private TimerTask timerTask;
    private long time1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar),true,"天气预报");
        grid_top = findViewById(R.id.grid_top);
        grid_bottom = findViewById(R.id.grid_bottom);
        grid_bottom.setAdapter(new grid_bottom_Adapter());
        time = findViewById(R.id.time);
        week = findViewById(R.id.week);
        temp = findViewById(R.id.temp);
        type = findViewById(R.id.type);
        weather_img = findViewById(R.id.weather_img);
        lineChart = findViewById(R.id.lineChart);
        weathterlist = new Gson().fromJson(InitApp.sp.getString("GetWeather",null),Weather.class).getROWS_DETAIL();
        grid_top.setAdapter(new grid_top_Adapter());
        initData();
        initChart();
        refresh();
    }

    public void initData(){
        time.setText(InitApp.timeFormat(new Date(),"yyyy年MM月dd日"));
        week.setText("星期"+getWeek(new Date()));
        String[] du = weathterlist.get(0).getTemperature().split("~");
        System.out.println("=-=-=-=-=-=" + du[1]);
        temp.setText(du[1]+ "度");
        type.setText(weathterlist.get(0).getType());
        switch (weathterlist.get(0).getType()){
            case "晴":
                weather_img.setImageResource(R.mipmap.sun);
                break;
            case "小雨":
                weather_img.setImageResource(R.mipmap.rain);
                break;
            case "阴":
                weather_img.setImageResource(R.mipmap.cloudy_);
                break;
            case "多云":
                weather_img.setImageResource(R.mipmap.cloudy);
                break;
        }
    }

    public String getWeek(Date date){
        String[] str = new String[]{"日","一","二","三","四","五","六"};
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return str[instance.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public void initChart(){
        entries1 = new ArrayList<>();
        entries2 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            entries1.add(new Entry(i,InitApp.random(10,15)));
            entries2.add(new Entry(i,InitApp.random(3,9)));
        }
        lineChart.setExtraOffsets(30,15,30,15);
        lineChart.setScaleEnabled(false);
        LineDataSet dataSet1 = new LineDataSet(entries1,"");
        LineDataSet dataSet2 = new LineDataSet(entries2,"");

        dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet2.setCircleColor(Color.parseColor("#fcc862"));
        dataSet1.setCircleColor(Color.parseColor("#94caf0"));

        dataSet1.setColor(Color.parseColor("#94caf0"));
        dataSet2.setColor(Color.parseColor("#fcc862"));

        LineData lineData = new LineData(dataSet1,dataSet2);
        dataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value + "°";
            }
        });
        dataSet2.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value + "°";
            }
        });

        dataSet1.setDrawCircleHole(false);
        dataSet2.setDrawCircleHole(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.setData(lineData);
    }

    public void refresh(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask,0,5000);
    }

    class grid_top_Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return weathterlist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1;
            if (view == null){
                view1 = View.inflate(getApplicationContext(),R.layout.activity_weather_top,null);
            }else {
                view1 = view;
            }
            TextView tv_1 = view1.findViewById(R.id.top_1);
            TextView tv_2 = view1.findViewById(R.id.top_2);
            TextView tv_3 = view1.findViewById(R.id.top_3);
            ImageView top_img = view1.findViewById(R.id.img_top);
            try {
                if (i == 0) {
                    time1 = new Date().getTime();
                    tv_2.setText("(今日 周"+ getWeek(new Date(time1))+")");
                } else {
                    time1 += 1000 * 60 * 60 * 24;
                    tv_2.setText("周"+ getWeek(new Date(time1)));

                }
                tv_1.setText(InitApp.timeFormat(new Date(time1), "MM月dd日"));
                tv_3.setText(weathterlist.get(i).getType());
                switch (weathterlist.get(i).getType()){
                    case "晴":
                        top_img.setImageResource(R.mipmap.sun);
                        break;
                    case "小雨":
                        top_img.setImageResource(R.mipmap.rain);
                        break;
                    case "阴":
                        top_img.setImageResource(R.mipmap.cloudy_);
                        break;
                    case "多云":
                        top_img.setImageResource(R.mipmap.cloudy);
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return view1;
        }
    }

    class grid_bottom_Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return str.length;
        }

        @Override
        public String getItem(int i) {
            return str[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1;
            if (view == null){
                view1 = View.inflate(getApplicationContext(),R.layout.activity_weather_bottom,null);
            }else {
                view1 = view;
            }
            TextView bottom_1 = view1.findViewById(R.id.bottom_1);
            TextView bottom_2 = view1.findViewById(R.id.bottom_2);
            TextView bottom_3 = view1.findViewById(R.id.bottom_3);
            ImageView bottom_img = view1.findViewById(R.id.img_bottom);
            bottom_1.setText(str[i]);
            bottom_img.setImageResource(str_img[i]);
            switch (i){
                case 0:
                    random = InitApp.random(0,5000);
                    if (random > 0 && random <1000){
                        bottom_2.setText(random +"");
                        bottom_3.setText("弱");
                        bottom_3.setBackgroundColor(Color.parseColor("#4472c4"));
                    }else if (random >= 1000 && random <=3000){
                        bottom_2.setText(random +"");
                        bottom_3.setText("中等");
                        bottom_3.setBackgroundColor(Color.parseColor("#00b050"));
                    }else {
                        bottom_2.setText(random +"");
                        bottom_3.setText("强");
                        bottom_3.setBackgroundColor(Color.parseColor("#ff0000"));
                    }
                    break;
                case 1:
                    random = InitApp.random(0,200);
                    if (random > 0 && random <35){
                        bottom_2.setText(random +"");
                        bottom_3.setText("优");
                        bottom_3.setBackgroundColor(Color.parseColor("#44dc68"));
                    }else if (random >= 35 && random <=75){
                        bottom_2.setText(random +"");
                        bottom_3.setText("良");
                        bottom_3.setBackgroundColor(Color.parseColor("#92d050"));
                    }else if (random >= 75 && random <=115){
                        bottom_2.setText(random +"");
                        bottom_3.setText("轻度污染");
                        bottom_3.setBackgroundColor(Color.parseColor("#ffff40"));
                    }else if (random >= 115 && random <=150){
                        bottom_2.setText(random +"");
                        bottom_3.setText("中度污染");
                        bottom_3.setBackgroundColor(Color.parseColor("#bf9000"));
                    }else {
                        bottom_2.setText(random +"");
                        bottom_3.setText("重度污染");
                        bottom_3.setBackgroundColor(Color.parseColor("#993300"));
                    }
                    break;
                case 2:
                    random = InitApp.random(0,10000);
                    if (random > 0 && random <3000){
                        bottom_2.setText(random +"");
                        bottom_3.setText("适宜");
                        bottom_3.setBackgroundColor(Color.parseColor("#44dc68"));
                    }else if (random >= 3000 && random <=6000){
                        bottom_2.setText(random +"");
                        bottom_3.setText("中");
                        bottom_3.setBackgroundColor(Color.parseColor("#ffc000"));
                    }else {
                        bottom_2.setText(random +"");
                        bottom_3.setText("较不宜");
                        bottom_3.setBackgroundColor(Color.parseColor("#8149ac"));
                    }
                    break;
                case 3:
                    random = InitApp.random(0,40);
                    if (random > -10 && random <12){
                        bottom_2.setText(random +"");
                        bottom_3.setText("冷");
                        bottom_3.setBackgroundColor(Color.parseColor("#3462f4"));
                    }else if (random >= 12 && random <=21){
                        bottom_2.setText(random +"");
                        bottom_3.setText("舒适");
                        bottom_3.setBackgroundColor(Color.parseColor("#92d050"));
                    } else if (random >= 21 && random <=35){
                        bottom_2.setText(random +"");
                        bottom_3.setText("温暖");
                        bottom_3.setBackgroundColor(Color.parseColor("#44dc68"));
                    }else {
                        bottom_2.setText(random +"");
                        bottom_3.setText("热");
                        bottom_3.setBackgroundColor(Color.parseColor("#ff0000"));
                    }
                    break;
                case 4:
                    random = InitApp.random(0,100);
                    if (random > 0 && random <50){
                        bottom_2.setText(random +"");
                        bottom_3.setText("较易发");
                        bottom_3.setBackgroundColor(Color.parseColor("#ff0000"));
                    }else {
                        bottom_2.setText(random +"");
                        bottom_3.setText("少发");
                        bottom_3.setBackgroundColor(Color.parseColor("#ffff40"));
                    }
                    break;
                case 5:
                    bottom_2.setText("");
                    bottom_3.setText("不太适宜");
                    bottom_3.setBackgroundColor(Color.parseColor("#ffff40"));
                    break;
            }
            return view1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
