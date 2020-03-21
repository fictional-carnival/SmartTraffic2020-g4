package com.lenovo.smarttraffic.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SubwayActivity extends BaseActivity {

    @SuppressLint("HandlerLeak")
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = load1.length();
            gridView1.setNumColumns(i);
            ViewGroup.LayoutParams layoutParams = gridView1.getLayoutParams();
            layoutParams.width = 100 * i;
            gridView1.setLayoutParams(layoutParams);
            Myadpter myadpter = new Myadpter();
            gridView1.setAdapter(myadpter);
        }
    };
    private TextView tv_start;
    private TextView tv_end;
    private RelativeLayout ll_load;
    private PopupWindow popupwindow;
    private LinearLayout ll_back;
    private int io;
    private ArrayList<TextView> pop_views;
    private RelativeLayout pop_rl_load;
    private int[] nums;
    private JSONArray loadArray;
    private int load_line;
    private long start = 0;
    private LinearLayout ll_line_photo;
    private GridView gridView1;
    private int popstaus = 0;
    private TextView tv_title;
    private int id;
    private ArrayList<JSONArray> loads;
    private JSONArray load1;
    private String site_name = "";
    private PopupWindow popupWindow;
    private int site_load = -1;
    private int click_status = 0;
    private TextView query_load;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        initAction();
    }

    private void initAction() {
        load_click();
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_load.setTranslationY(40);
                ll_load.setBackgroundColor(Color.parseColor("#ffffff"));
                ll_back.setBackgroundColor(Color.parseColor("#cccccc"));
                click_status = 1;
                load_click();
            }
        });
        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_load.setTranslationY(40);
                ll_load.setBackgroundColor(Color.parseColor("#ffffff"));
                ll_back.setBackgroundColor(Color.parseColor("#cccccc"));
                click_status = 2;

            }
        });
        query_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tv_end.getText().toString().isEmpty() && !tv_start.getText().toString().isEmpty()) {
                    startActivity(new Intent(getApplicationContext(), TipActivity.class));
                } else {
                    InitApp.toast("您有起点或终点尚未选择！");
                }
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canpop();
                ll_load.setBackgroundColor(Color.parseColor("#ffffff"));
                ll_back.setBackgroundColor(Color.parseColor("#ffffff"));
                ll_load.setTranslationY(0);
                ll_line_photo.setVisibility(View.INVISIBLE);
                click_status = 0;
            }
        });
    }


    public void load_click() {
        for (int i = 0; i < 8; i++) {
            int finalI = i;
            pop_views.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (click_status) {
                        case 0:
                            startActivity(new Intent(getApplicationContext(), SubwayDeailActivity.class));
                            InitApp.edit.putInt("load", finalI).commit();
                            break;
                        case 1:
                        case 2:
                            ll_line_photo.setVisibility(View.VISIBLE);
                            ll_load.setTranslationY(0);
                            ll_load.setBackgroundColor(Color.parseColor("#cccccc"));
                            load_line = finalI;
                            load1 = loads.get(load_line);
                            try {
                                tv_title.setText(loadArray.getJSONObject(load_line).getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            site_load = -1;
                            mHander.sendEmptyMessage(0);
                            break;
                    }
                }

            });
        }
    }



    @Override
    protected int getLayout () {
        return R.layout.activity_subway;
    }
    private void InitView () {
        initToolBar(findViewById(R.id.toolbar), true, "地铁线路查询");
        tv_start = (TextView) findViewById(R.id.start_address);
        tv_title = (TextView) findViewById(R.id.tv_title2);
        query_load = (TextView) findViewById(R.id.query_load);
        tv_end = (TextView) findViewById(R.id.end_address);
        tv_end = (TextView) findViewById(R.id.end_address);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_line_photo = (LinearLayout) findViewById(R.id.ll_line_photo);
        ll_load = (RelativeLayout) findViewById(R.id.ll_load);
        gridView1 = (GridView) findViewById(R.id.gv_list);
        pop_views = new ArrayList<TextView>() {{
            add((TextView) findViewById(R.id.tv_1));
            add((TextView) findViewById(R.id.tv_2));
            add((TextView) findViewById(R.id.tv_3));
            add((TextView) findViewById(R.id.tv_4));
            add((TextView) findViewById(R.id.tv_5));
            add((TextView) findViewById(R.id.tv_6));
            add((TextView) findViewById(R.id.tv_7));
            add((TextView) findViewById(R.id.tv_8));
        }};
        nums = new int[]{1, 2, 3, 5, 6, 4, 8, 7};
        loadArray = new JSONArray();
        //地铁站的集合数组
        loads = new ArrayList<>();
        id = 0;
        initData();
    }

    private void initData () {
        if (id != 8) {
            Map map = new HashMap();
            map.put("UserName", "admin");
            map.put("Line", nums[id]);
            InitApp.doPost("GetMetroInfo", map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        loadArray.put(jsonObject.getJSONArray("ROWS_DETAIL").getJSONObject(0));
                        id++;
                        initData();
                        loads.add(jsonObject.getJSONArray("ROWS_DETAIL").getJSONObject(0).getJSONArray("sites"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Log.i("sfasfsa", "initData: "+loadArray.toString());
            InitApp.edit.putString("loads", loadArray.toString()).commit();
        }


    }

    private class Myadpter extends BaseAdapter {
        @Override
        public int getCount() {
            return load1.length();
        }

        @Override
        public Object getItem(int i) {
            try {
                return load1.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View conview = null;
            if (view == null) {
                conview = View.inflate(getApplicationContext(), R.layout.loadsite_list, null);
            } else {
                conview = view;
            }
            RelativeLayout ll_site = (RelativeLayout) conview.findViewById(R.id.ll_site);
            TextView tv_site = (TextView) conview.findViewById(R.id.tv_site);
            RadioButton rd_ch = (RadioButton) conview.findViewById(R.id.rb_ch);
            try {
                site_name = load1.getString(i);
                tv_site.setText(site_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == site_load) {
                rd_ch.setChecked(true);
            }
            View finalConview = conview;
            ll_site.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        site_name = load1.getString(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isSite(site_name, finalConview);
                    if (start == 0) {
                        InitApp.toast("再点击一次选择！");
                        start = System.currentTimeMillis();
                    } else {
                        if (site_load == i) {
                            if (System.currentTimeMillis() - start < 1000) {
                                set_textInfo();
                            }
                            start = 0;
                        } else {
                            start = 0;
                        }
                    }
                    site_load = i;
                    mHander.sendEmptyMessage(0);
                }
            });
            return conview;
        }
    }


    private void set_textInfo () {
        if (click_status == 1 && !site_name.equals(tv_end.getText().toString())) {
            tv_start.setText(site_name);
            ll_back.performClick();


        } else if (click_status == 2 && !site_name.equals(tv_start.getText().toString())) {
            tv_end.setText(site_name);
            ll_back.performClick();

        } else {
            InitApp.toast("起点和终点不可以选择同意站点");
        }
        if (!tv_end.getText().toString().isEmpty() && !tv_start.getText().toString().isEmpty()) {
            query_load.setBackgroundResource(R.drawable.load_blue);
        }
    }

    private void isSite (String site_name, View finalConview){
        canpop();
        for (int i = 0; i < loads.size(); i++) {
            if (i != load_line && loads.get(i).toString().indexOf(site_name) != -1) {
                try {
                    String load_name = loadArray.getJSONObject(i).getString("name");
                    popSite(finalConview, load_name);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void popSite (View finalConview, String load_name){
        int location[] = new int[2];
        finalConview.getLocationOnScreen(location);
        View pop_view = this.getLayoutInflater().inflate(R.layout.pop_subway, null);
        popupWindow = new PopupWindow(pop_view, -2, -2, false);
        View parentView = LayoutInflater.from(SubwayActivity.this).inflate(R.layout.activity_subway, null);
        popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, location[0], location[1] - 50);
        TextView textView = (TextView) pop_view.findViewById(R.id.tv_dec);
        textView.setText("可换乘"+load_name);
        popstaus = 1;
    }

    private void canpop () {
        if (popstaus == 1) {
            popupWindow.dismiss();
            popstaus = 0;
        }
    }
    @Override
    protected void onResume() {
        tv_start.setText("");
        tv_end.setText("");
        super.onResume();
    }
}
