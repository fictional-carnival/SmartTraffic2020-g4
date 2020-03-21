package com.lenovo.smarttraffic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class InitApp extends MultiDexApplication {

    private static Handler mainHandler;
    //    private static Context AppContext;
    private static InitApp instance;
    private Set<Activity> allActivities;
    public static Toast tos;
    public static SharedPreferences.Editor edit;
    public static SharedPreferences sp;
    public static RequestQueue queue;
    public static ArrayList<String> label = new ArrayList<String>() {{
        add("推荐");
        add("热点");
        add("科技");
        add("汽车咨讯");
        add("健康");
        add("财经");
        add("教育");
        add("旅游");
        add("军事");
        add("实时路况");
        add("文化");
        add("二手车");
        add("违章咨讯");
        add("娱乐");
        add("体育");
        add("视频");
        add("游戏");
        add("电影");
    }};

    public static synchronized InitApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        AppContext = this;
        instance = this;
        mainHandler = new Handler();

        tos = Toast.makeText(instance, null, Toast.LENGTH_SHORT);
        sp = getSharedPreferences("settings", MODE_PRIVATE);
        edit = sp.edit();
        queue = Volley.newRequestQueue(instance);
        initData();
    }

    private void initData() {
        if (null == sp.getString("userinfo", null)) {
            getUserInfo();
            violation();
        }
    }

    private void getUserInfo() {
        doPost("GetSUserInfo", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                edit.putString("userinfo", String.valueOf(jsonObject)).commit();
            }
        });
    }
    private void violation() {
        doPost("GetCarInfo", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                InitApp.edit.putString("carInfo", jsonObject.toString()).commit();
            }
        });
        doPost("GetAllCarPeccancy", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray rows_detail = jsonObject.getJSONArray("ROWS_DETAIL");
                    doPost("GetPeccancyType", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray rows_detail1 = jsonObject.getJSONArray("ROWS_DETAIL");
                                for (int i = 0; i < rows_detail.length(); i++) {
                                    for (int j = 0; j < rows_detail1.length(); j++) {
                                        if (rows_detail.getJSONObject(i).getString("pcode").equals(rows_detail1.getJSONObject(j).getString("pcode"))) {
                                            rows_detail.getJSONObject(i).put("premarks", rows_detail1.getJSONObject(j).getString("premarks"));
                                            rows_detail.getJSONObject(i).put("pscore", rows_detail1.getJSONObject(j).getString("pscore"));
                                            rows_detail.getJSONObject(i).put("pmoney", rows_detail1.getJSONObject(j).getString("pmoney"));
                                            rows_detail.getJSONObject(i).put("pchuli", 1);
                                        }
                                    }
                                }
                                edit.putString("violations", rows_detail.toString()).commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static User.ROWSDETAILBean getUser(String type, String name) {
        try {
            JSONArray array = new JSONObject(sp.getString("userinfo", null)).getJSONArray("ROWS_DETAIL");
            for (int i = 0, l = array.length(); i < l; i++) {
                JSONObject object = array.getJSONObject(i);
                if (name.equals(object.getString(type))) {
                    return new Gson().fromJson(String.valueOf(object), User.ROWSDETAILBean.class);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void toast(String s) {
        tos.cancel();
        tos = Toast.makeText(instance, s, Toast.LENGTH_SHORT);
        tos.show();
    }

    public static int random(int a, int b) {
        return (int) Math.round(Math.random() * (a - b) + b);
    }

    public static String timeFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    public static void doPost(String path, Map map, Response.Listener<JSONObject> listener) {
        HashMap m = new HashMap();
        m.put("UserName", "user1");
        JSONObject object = new JSONObject(m);
        if (null != map) {
            object = new JSONObject(map);
        }
        String url = "http://www.lylala8.com:8081/transportservice/action/" + path;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast("网络连接错误");
            }
        });
        queue.add(request);
    }

    //    public static Context getContext(){
//        return AppContext;
//    }
    public static Handler getHandler() {
        return mainHandler;
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
