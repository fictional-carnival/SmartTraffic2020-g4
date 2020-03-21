package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.User;
import com.lenovo.smarttraffic.bean.UserInfo2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class ScActivity extends BaseActivity {
    private ListView listView;
    private User userInfos;
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myAdapter = new MyAdapter();
            listView.setAdapter(myAdapter);

        }
    };
    private JSONArray scs;
    private UserInfo2 userInfo;
    private Map<String, JSONObject> map;
    private JSONObject zd;
    private String[] names;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sc;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "用户收藏");
        listView = (ListView) findViewById(R.id.lv_list);
    }

    private void initData() {
        try {

            scs = new JSONArray(InitApp.sp.getString("sc", "[]"));
            mHander.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return scs.length();
        }

        @Override
        public String getItem(int i) {
            try {
                return scs.getJSONObject(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View conview = null;
            if (view == null) {
                conview = View.inflate(getApplicationContext(), R.layout.sc_list, null);

            } else {
                conview = view;
            }
            ImageView im_tx = conview.findViewById(R.id.im_tx);
            TextView tv_user = conview.findViewById(R.id.tv_user);
            TextView tv_name = conview.findViewById(R.id.tv_name);
            TextView tv_tel = conview.findViewById(R.id.tv_tel);
            TextView tv_sf = conview.findViewById(R.id.tv_sf);
            TextView tv_datil = conview.findViewById(R.id.tv_datil);
            TextView tv_sc = conview.findViewById(R.id.tv_sc);
            TextView tv_date = conview.findViewById(R.id.tv_date);
            TextView tv_zd = conview.findViewById(R.id.tv_zd);
            HorizontalScrollView horizontalScrollView = conview.findViewById(R.id.hs_view);
            horizontalScrollView.scrollTo(0, 0);
            userInfo = new Gson().fromJson(getItem(i), UserInfo2.class);
            im_tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userInfo = new Gson().fromJson(getItem(i), UserInfo2.class);
                    Intent intent = new Intent(getApplicationContext(), ViolationActivity.class);
                    String str = userInfo.getUsername().substring(4);
                    intent.putExtra("violation", Integer.parseInt(str)-1);
                    startActivity(intent);
                }
            });
            Log.i("sfasf", "getView: " + userInfo.toString());
            if (userInfo.getGly() < 3) {
                tv_sf.setText("一般管理员");
            } else {
                tv_sf.setText("普通用户");
            }
            if (userInfo.getZd() == 0) {
                tv_zd.setText("置顶");
                tv_sc.setText("取消收藏");
                horizontalScrollView.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                tv_zd.setText("取消置顶");
                tv_sc.setText("取消收藏");
                horizontalScrollView.setBackgroundColor(Color.parseColor("#cccccc"));
            }
            if (userInfo.getPsex().equals("男")) {
                im_tx.setImageResource(R.mipmap.touxiang_2);
            } else {
                im_tx.setImageResource(R.mipmap.touxiang_1);

            }
            tv_user.setText("用户名："+userInfo.getUsername());
            tv_name.setText("姓名："+userInfo.getPname());
            tv_tel.setText("电话："+userInfo.getPtel());
            tv_date.setText(userInfo.getDate());
            tv_sc.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
                    scs.remove(i);
                    tv_sc.setText("收藏");
                    InitApp.toast("取消收藏");
                    notifyDataSetChanged();
                    InitApp.edit.putString("sc", scs.toString()).commit();
                    horizontalScrollView.scrollTo(0, 0);
                }
            });
            tv_zd.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {

                    if (tv_zd.getText().toString().equals("取消置顶")) {
                        try {
                            scs.getJSONObject(i).put("zd", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tv_zd.setText("置顶");
                        tv_sc.setText("取消收藏");
                        horizontalScrollView.setBackgroundColor(Color.parseColor("#ffffff"));
                        rond(0, i);
                        notifyDataSetChanged();
                    } else {
                        try {
                            scs.getJSONObject(i).put("zd", 1);
                            tv_zd.setText("取消收藏");
                            tv_sc.setText("取消置顶");
                            horizontalScrollView.setBackgroundColor(Color.parseColor("#cccccc"));
                            rond(1, i);
                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    InitApp.edit.putString("sc", scs.toString()).commit();
                    horizontalScrollView.scrollTo(0, 0);
                }
            });
            return conview;
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void rond(int t, int i) {

        try {

            if (t == 0) {
                JSONObject jc = scs.getJSONObject(i);
                scs.remove(i);
                scs.put(jc);

            } else {
                JSONObject jc = scs.getJSONObject(i);
                JSONArray jcS = new JSONArray();
                scs.remove(i);
                jcS.put(jc);
                for (int j = 0; j <scs.length() ; j++) {
                    jcS.put(scs.get(j));
                }
                scs = jcS;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
