package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class UserCenterActivity extends BaseActivity {

    private List<User.ROWSDETAILBean> userInfos;
    private ListView lv_list;
    private JSONArray scs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_usercenter;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "用户中心");
        lv_list = findViewById(R.id.lv_list);
    }

    private void InitData() {
        userInfos = new Gson().fromJson(InitApp.sp.getString("userinfo", ""), User.class).getROWS_DETAIL();
        MyAdapter myAdapter = new MyAdapter();
        lv_list.setAdapter(myAdapter);
        try {
            scs = new JSONArray(InitApp.sp.getString("sc", "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        InitData();
        super.onResume();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userInfos.size();
        }

        @Override
        public User.ROWSDETAILBean getItem(int i) {
            return userInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View conview = null;
            if (view == null) {
                conview = View.inflate(getApplicationContext(), R.layout.userinfo_list, null);

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
            HorizontalScrollView horizontalScrollView = conview.findViewById(R.id.hs_view);
            horizontalScrollView.scrollTo(0, 0);
            if (scs.toString().indexOf(getItem(i).getPname())!=-1) {
                tv_sc.setText("已收藏");
            } else {
                tv_sc.setText("收藏");
            }
          tv_sc.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
                    try {

                        if (tv_sc.getText().toString().equals("收藏")) {
                            JSONObject sc = new JSONObject(new Gson().toJson(getItem(i)));
                            sc.put("date", InitApp.timeFormat(new Date(), "yyyy.MM.dd HH:mm"));
                            sc.put("zd", 0);
                            sc.put("gly", i);
                            scs.put(sc);
                            tv_sc.setText("已收藏");
                            InitApp.toast("已收藏");
                        } else {
                            scs.remove(i);
                            tv_sc.setText("收藏");
                            InitApp.toast("取消收藏");
                        }
                        InitApp.edit.putString("sc", scs.toString()).commit();
                        horizontalScrollView.scrollTo(0, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (getItem(i).getPsex().equals("男")) {
                im_tx.setImageResource(R.mipmap.touxiang_2);
            } else {
                im_tx.setImageResource(R.mipmap.touxiang_1);

            }
            tv_user.setText("用户名："+getItem(i).getUsername());
            tv_name.setText("姓名："+getItem(i).getPname());
            tv_tel.setText("电话："+getItem(i).getPtel());

            if (i < 3) {
                tv_sf.setText("一般管理员");
            } else {
                tv_sf.setText("普通用户");
            }
            tv_datil.setText("查看详情");
            tv_datil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ScActivity.class));
                }
            });
            im_tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViolationActivity.class);
                    intent.putExtra("violation", i);
                    startActivity(intent);
                }
            });
            return conview;
        }

    }
}
