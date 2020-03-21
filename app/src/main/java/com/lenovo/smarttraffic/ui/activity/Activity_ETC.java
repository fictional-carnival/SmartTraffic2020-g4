package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Activity_ETC extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_etc;
    }

    private ListView etc_listview;
    private Spinner spinner_1,spinner_2,spinner_3;
    private Button etc_select,etc_rechage,etc_record;
    private TextView successful_seelct,successful_rechage;
    private String[] str;
    private TextView etc_balance;
    private RadioButton time_up,time_down;
    private int index = 1;
    private JSONArray etcArrays;
    private int index2;
    private int money;
    private int money2;
    private JSONArray etc_arrays;
    private int px;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            try {
                if (time_down.isChecked()) {
                    px = 1;
                } else {
                    px = 0;
                }
                etc_arrays = etcArrays.getJSONObject(index2).getJSONArray("history");
                etc_listview.setAdapter(new MyAdapter());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar),true,"ETC账户");
        etc_listview = findViewById(R.id.etc_listview);

        etc_select = findViewById(R.id.etc_select);
        etc_rechage = findViewById(R.id.etc_rechage);
        etc_record = findViewById(R.id.etc_record);

        etc_balance = findViewById(R.id.etc_balance);
        time_up = findViewById(R.id.time_up);
        time_down = findViewById(R.id.time_down);
        spinner_2 = findViewById(R.id.spinner_2);
        spinner_1 = findViewById(R.id.spinner_1);
        spinner_3 = findViewById(R.id.spinner_3);
        successful_seelct = findViewById(R.id.successful_select);
        successful_rechage = findViewById(R.id.successful_rechage);
        initData();
        initSpinner();

    }

    private void initData() {
        try {
            if (InitApp.sp.getString("ETC_Info", "").equals("")) {
                etcArrays = new JSONArray();
                for (int i = 1; i <= 4; i++) {
                    JSONObject k = new JSONObject();
                    k.put("money", 50);
                    k.put("history", new JSONArray());
                    etcArrays.put(k);
                }
            } else {
                etcArrays = new JSONArray(InitApp.sp.getString("ETC_Info", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void initSpinner(){
        String str2[] = new String[]{"1", "2", "3", "4"};
        spinner_1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner,str2));
        spinner_3.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner,str2));
        str = new String[4];
        str[0] = "50";
        str[1] = "100";
        str[2] = "150";
        str[3] = "200";
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner,str);
        arrayAdapter.setDropDownViewResource(R.layout.spinner);
        spinner_2.setAdapter(arrayAdapter);
        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                money = (i + 1) * 50;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index2 = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initAction();

    }

    private void initAction() {

        //查询
        etc_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    etc_balance.setText(etcArrays.getJSONObject(index).getInt("money")+"");
                    successful_seelct.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //充值
        etc_rechage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    money2 = money+etcArrays.getJSONObject(index).getInt("money");
                    etc_balance.setText(money2 + "");
                    etcArrays.getJSONObject(index).put("money", money2);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("acarId", index + 1 + "");
                    jsonObject.put("visit", money+"");
                    jsonObject.put("user", "user1");
                    jsonObject.put("date", InitApp.timeFormat(new Date(), "yyyy.MM.dd HH:mm"));
                    etcArrays.getJSONObject(index).getJSONArray("history").put(jsonObject);
                    InitApp.edit.putString("ETC_Info", etcArrays.toString()).commit();
                    successful_rechage.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        etc_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(0);
            }
        });

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return etc_arrays.length();
        }

        @Override
        public JSONObject getItem(int position) {
            try {
                if (px == 0) {
                    return etc_arrays.getJSONObject(position);
                } else {
                    return etc_arrays.getJSONObject(etc_arrays.length()-1-position);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ETCHorder etcHorder;
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_etc_list,null);
                etcHorder = new ETCHorder();
                etcHorder.chehao = view.findViewById(R.id.chehao);
                etcHorder.xuhao = view.findViewById(R.id.xuhao);
                etcHorder.etc_money = view.findViewById(R.id.etc_money);
                etcHorder.etc_user = view.findViewById(R.id.etc_user);
                etcHorder.etc_time = view.findViewById(R.id.etc_time);
                view.setTag(etcHorder);
            }else {
                view = convertView;
                etcHorder = (ETCHorder)view.getTag();
            }
            etcHorder.xuhao.setText("" + (position + 1));

            try {
                etcHorder.chehao.setText(getItem(position).getString("acarId"));
                etcHorder.etc_money.setText(getItem(position).getString("visit"));
                etcHorder.etc_user.setText(getItem(position).getString("user"));
                etcHorder.etc_time.setText(getItem(position).getString("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
    class ETCHorder{
        TextView xuhao;
        TextView chehao;
        TextView etc_money;
        TextView etc_user;
        TextView etc_time;
    }

}
