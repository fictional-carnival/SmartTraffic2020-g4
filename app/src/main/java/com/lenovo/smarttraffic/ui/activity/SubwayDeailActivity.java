package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class SubwayDeailActivity extends BaseActivity {

    private JSONObject loadJson;
    private ArrayList<TextView> textViews;
    private ListView lv_list;
    private ListView lv_list2;
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            DeatilAdapter deatilAdapter = new DeatilAdapter();
            lv_list.setAdapter(deatilAdapter);

            if (strings2.size() != 0) {
                DeatilAdapter2 deatilAdapter2 = new DeatilAdapter2();
                lv_list2.setAdapter(deatilAdapter2);
            }
        }
    };
    private ArrayList<String> strings1;
    private ArrayList<String> strings2;
    private LinearLayout li_er;
    private ImageView im_load;
    private ImageView im_load2;
    private long start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        initAction();
    }

    private void initAction() {

        //点击两次放大，点击一次缩小
        im_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start == 0) {
                    start = System.currentTimeMillis();
                    InitApp.toast("再点击一次放大！");
                } else {
                    if (System.currentTimeMillis() - start < 800) {
                        li_er.setVisibility(View.GONE);
                        im_load2.setVisibility(View.VISIBLE);
                        InitApp.tos.cancel();
                    }
                    start = 0;
                }

            }
        });
        im_load2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                li_er.setVisibility(View.VISIBLE);
                im_load2.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_subway_deatil;
    }

    private void InitView() {
        int load = InitApp.sp.getInt("load", 0);
        try {
            //获取该路线的json信息
            loadJson = new JSONArray(InitApp.sp.getString("loads", "")).getJSONObject(load);
            initToolBar(findViewById(R.id.toolbar), true, loadJson.getString("name") + "线路详情");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        li_er = (LinearLayout) findViewById(R.id.ll_load);
        im_load = (ImageView) findViewById(R.id.im_load);
        im_load2 = (ImageView) findViewById(R.id.im_load2);

        textViews = new ArrayList<TextView>() {{
            add((TextView) findViewById(R.id.tv_1));
            add((TextView) findViewById(R.id.tv_2));
            add((TextView) findViewById(R.id.tv_3));
            add((TextView) findViewById(R.id.tv_4));
            add((TextView) findViewById(R.id.tv_5));
            add((TextView) findViewById(R.id.tv_6));
            add((TextView) findViewById(R.id.tv_7));
            add((TextView) findViewById(R.id.tv_8));
        }};
        lv_list = (ListView) findViewById(R.id.lv_1);
        lv_list2 = (ListView) findViewById(R.id.lv_2);

        initData();

    }

    private void initData() {

        try {
            JSONArray sites = loadJson.getJSONArray("sites");
            strings1 = new ArrayList<>();
            strings2 = new ArrayList<>();
            for (int i = 0; i < sites.length(); i++) {
                if (i < 15) {
                    strings1.add(sites.getString(i));
                } else {
                    strings2.add(sites.getString(i));
                }
                if (i == sites.length() - 1) {
                    mHander.sendEmptyMessage(0);
                }
            }

            JSONArray time_Array = loadJson.getJSONArray("time");
            textViews.get(0).setText(time_Array.getJSONObject(0).getString("site"));
            String start_time = time_Array.getJSONObject(0).getString("starttime");
            String end_timee = time_Array.getJSONObject(0).getString("endtime");
            textViews.get(1).setText("首班：" + start_time.substring(0, 5));
            textViews.get(2).setText("末班：" + end_timee.substring(0, 5));

            textViews.get(3).setText(loadJson.getJSONArray("time").getJSONObject(1).getString("site"));
            start_time = time_Array.getJSONObject(1).getString("starttime");
            end_timee = time_Array.getJSONObject(1).getString("endtime");
            textViews.get(4).setText("首班：" + start_time.substring(0, 5));
            textViews.get(5).setText("末班：" + end_timee.substring(0, 5));
            double l_kg = (sites.length() - 1) * 2;
            textViews.get(6).setText(sites.length() + "站/" + (int) l_kg + "公里");
            textViews.get(7).setText("票价：最高票价" + (float) l_kg * 2 / 10 + "元");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private class DeatilAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return strings1.size();
        }

        @Override
        public Object getItem(int i) {
            return strings1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View contentView = null;


            if (contentView == null) {
                contentView = View.inflate(getApplicationContext(), R.layout.subwaydeatil_list, null);
            } else {
                contentView = view;
            }

            TextView tv_site =(TextView) contentView.findViewById(R.id.tv_site);

            tv_site.setText(strings1.get(i));

            return contentView;
        }
    }


    private class DeatilAdapter2 extends BaseAdapter {
        @Override
        public int getCount() {
            return strings2.size();
        }

        @Override
        public Object getItem(int i) {
            return strings2.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View conview = null;
            if (conview == null) {
                conview = View.inflate(getApplicationContext(), R.layout.subwaydeatil_list, null);
            } else {
                conview = view;
            }
            TextView tv_site =(TextView) conview.findViewById(R.id.tv_site);
            tv_site.setText(strings2.get(i));
            return conview;
        }
    }
}
