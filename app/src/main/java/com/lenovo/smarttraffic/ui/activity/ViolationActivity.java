package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Color;
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

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Car;
import com.lenovo.smarttraffic.bean.User;
import com.lenovo.smarttraffic.bean.Violation;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class ViolationActivity extends BaseActivity {

    private User.ROWSDETAILBean userinfo;
    private JSONArray userViolations;
    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (userViolations.length() != 0) {
                MyAdapter myAdapter = new MyAdapter();
                lv_list.setAdapter(myAdapter);
            } else {
                lll.setVisibility(View.GONE);
                tv_text.setVisibility(View.VISIBLE);
            }


        }
    };
    private ListView lv_list;
    private TextView tv_tel;
    private TextView tv_sex;
    private TextView tv_name;
    private ImageView im_tx;
    private TextView tv_text;
    private LinearLayout lll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        initData();
    }

    private void initData() {
        try {
            int i = getIntent().getIntExtra("violation", 0);
            userinfo = new Gson().fromJson(InitApp.sp.getString("userinfo", ""), User.class).getROWS_DETAIL().get(i);
            if (InitApp.sp.getString(userinfo.getUsername(), "").isEmpty()) {
                List<Car.ROWSDETAILBean> carInfo = new Gson().fromJson(InitApp.sp.getString("carInfo", ""), Car.class).getROWS_DETAIL();
                ArrayList<Car.ROWSDETAILBean> list = new ArrayList<>();
                for (int j = 0; j < carInfo.size(); j++) {
                    if (userinfo.getPcardid().equals(carInfo.get(j).getPcardid())) {
                        list.add(carInfo.get(j));
                    }
                }
                JSONArray violations = new JSONArray(InitApp.sp.getString("violations", "[]"));
                userViolations = new JSONArray();
                for (int j = 0; j < list.size(); j++) {
                    for (int k = 0; k < violations.length(); k++) {
                        if (violations.getJSONObject(k).getString("carnumber").equals(list.get(j).getCarnumber())) {
                            violations.getJSONObject(k).put("carbrand", list.get(j).getCarbrand());
                            userViolations.put(violations.getJSONObject(k));
                        }
                    }
                }
                InitApp.edit.putString(userinfo.getUsername(), userViolations.toString()).commit();
                mHander.sendEmptyMessage(0);
            } else {
                userViolations = new JSONArray(InitApp.sp.getString(userinfo.getUsername(), "[]"));
                mHander.sendEmptyMessage(0);
            }
            if (userinfo.getPsex().equals("男")) {
                im_tx.setImageResource(R.mipmap.touxiang_2);
            } else {
                im_tx.setImageResource(R.mipmap.touxiang_1);
            }
            tv_name.setText("姓名："+userinfo.getPname());
            tv_sex.setText("性别："+userinfo.getPsex());
            tv_tel.setText("手机号码："+userinfo.getPtel());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_violation;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "违章详情");
        tv_name = findViewById(R.id.wz_name);
        tv_sex = findViewById(R.id.wz_sex);
        tv_tel = findViewById(R.id.wz_tel);
        im_tx = findViewById(R.id.wz_tx);
        lv_list = findViewById(R.id.lv_list);
        tv_text = findViewById(R.id.tv_text);
        lll = findViewById(R.id.llll);

    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userViolations.length();
        }

        @Override
        public Violation getItem(int i) {
            try {
                return new Gson().fromJson(userViolations.getJSONObject(i).toString(), Violation.class);
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
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(getApplicationContext(), R.layout.violation_list, null);
                view.setTag(viewHolder);
                viewHolder.tv_xh = (TextView) view.findViewById(R.id.tv_xh);
                viewHolder.im_zp = (ImageView) view.findViewById(R.id.im_zp);
                viewHolder.tv_cp = (TextView) view.findViewById(R.id.tv_cp);
                viewHolder.tv_dd = (TextView) view.findViewById(R.id.tv_dd);
                viewHolder.tv_yx = (TextView) view.findViewById(R.id.tv_yx);
                viewHolder.tv_kf = (TextView) view.findViewById(R.id.tv_kf);
                viewHolder.tv_fk = (TextView) view.findViewById(R.id.tv_fk);
                viewHolder.tv_sj = (TextView) view.findViewById(R.id.tv_sj);
                viewHolder.tv_zt = (TextView) view.findViewById(R.id.tv_zt);
            }else
                viewHolder = (ViewHolder) view.getTag();
            viewHolder.tv_xh.setText(i + 1 + "");
            viewHolder.tv_cp.setText(getItem(i).getCarnumber());
            viewHolder.tv_dd.setText(getItem(i).getPaddr());
            //违章原因的简写
            String yx = getItem(i).getPremarks();
            yx = yx.replaceAll(yx.substring(2, yx.length() - 3), "...");
            viewHolder.tv_yx.setText(yx);
            viewHolder.tv_sj.setText(getItem(i).getPdatetime());
            if (getItem(i).getPmoney().equals("0")) {
                viewHolder.tv_fk.setText("无");
                viewHolder.tv_zt.setTextColor(Color.parseColor("#cccccc"));
            } else {
                viewHolder.tv_fk.setText(getItem(i).getPmoney());
            }

            if (getItem(i).getPscore().equals("0")) {
                viewHolder.tv_kf.setText("无");
            } else {
                viewHolder.tv_kf.setText(getItem(i).getPscore());
            }

            if (getItem(i).getPchuli() == 1) {
                viewHolder.tv_zt.setText("未处理");
                viewHolder.tv_zt.setTextColor(Color.RED);

            } else {
                viewHolder.tv_zt.setText("已处理");
                viewHolder.tv_zt.setTextColor(Color.GREEN);
            }
            int icon2 = getResources().getIdentifier(getItem(i).getCarbrand(), "mipmap", getPackageName());
            viewHolder.im_zp.setImageResource(icon2);
            viewHolder.tv_zt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!getItem(i).getPmoney().equals("0") && getItem(i).getPchuli() == 1) {
                        Intent intent = new Intent(getApplicationContext(), ViolationPayActivity.class);
                        intent.putExtra("username",userinfo.getUsername());
                        intent.putExtra("number",i);
                        startActivity(intent);
                    }
                }
            });
            return view;
        }

        class ViewHolder {
            TextView tv_xh;
            ImageView im_zp;
            TextView tv_cp;
            TextView tv_dd;
            TextView tv_yx;
            TextView tv_kf;
            TextView tv_fk;
            TextView tv_sj;
            TextView tv_zt;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
