package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Ditu;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class XiaoFeiActivity extends BaseActivity {
    private MapView mapView;
    private AMap map;
    private LatLng latLng;
    private ArrayList<LatLng> latLngs;
    private ArrayList<Marker> markers;
    private List<Ditu.ROWSDETAILBean> parkInfo;
    private ListView lv_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_xiaofei;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "用户停车");
        lv_list = findViewById(R.id.lv_list);
        ImageView im_1 = findViewById(R.id.im_1);
        ImageView im_3 = findViewById(R.id.im_3);
        map = mapView.getMap();
        latLng = new LatLng(39.941853, 116.385307);
        im_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,9));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.marker_self)));
                markerOptions.title("什刹海");

                map.addMarker(markerOptions);

            }
        });
        im_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv_list.setVisibility(View.VISIBLE);
                for (Marker m : markers) {
                    m.setVisible(true);
                }
            }
        });
        map.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!latLngs.contains(latLng)) {
                    lv_list.setVisibility(View.GONE);
                }
            }
        });
    }

    private void InitData() {
        markers = new ArrayList<>();
        latLngs = new ArrayList<LatLng>(){{
            add(new LatLng(40.042737, 116.309884));
            add(new LatLng(39.860512, 116.347593));
            add(new LatLng(39.802303, 116.209749));
        }};
        int imgs[] = new int[]{
                R.mipmap.marker_one,
                R.mipmap.marker_second,
                R.mipmap.marker_thrid
        };
        for (int i = 0; i < latLngs.size(); i++) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(i),9));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), imgs[i])));
            markerOptions.position(latLngs.get(i));
            Marker marker = map.addMarker(markerOptions);
            marker.setVisible(false);
            markers.add(marker);
        }
        String str = InitApp.sp.getString("ditu", "");
        if (str.equals("")) {
            InitApp.doPost("GetCarParkInfo", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    InitApp.edit.putString("ditu", jsonObject.toString()).commit();
                    parkInfo = new Gson().fromJson(jsonObject.toString(), Ditu.class).getROWS_DETAIL();
                    Collections.sort(parkInfo, new Comparator<Ditu.ROWSDETAILBean>() {
                        @Override
                        public int compare(Ditu.ROWSDETAILBean rowsdetailBean, Ditu.ROWSDETAILBean t1) {
                            return rowsdetailBean.getDistance() - t1.getDistance();
                        }
                    });
                    lv_list.setAdapter(new ParkInfoAdapter());

                }
            });
        } else {
            parkInfo = new Gson().fromJson(str.toString(), Ditu.class).getROWS_DETAIL();
            Collections.sort(parkInfo, new Comparator<Ditu.ROWSDETAILBean>() {
                @Override
                public int compare(Ditu.ROWSDETAILBean rowsdetailBean, Ditu.ROWSDETAILBean t1) {
                    return rowsdetailBean.getDistance() - t1.getDistance();
                }
            });
            lv_list.setAdapter(new ParkInfoAdapter());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private class ParkInfoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return parkInfo.size();
        }

        @Override
        public Ditu.ROWSDETAILBean getItem(int i) {
            return parkInfo.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View conview;
            if (view == null) {
                conview = View.inflate(getApplicationContext(), R.layout.xiaofei_list, null);
            } else {
                conview = view;
            }

            TextView tv_1 = conview.findViewById(R.id.tv_1);
            TextView tv_2 = conview.findViewById(R.id.tv_2);
            TextView tv_3 = conview.findViewById(R.id.tv_3);
            TextView tv_4 = conview.findViewById(R.id.tv_4);
            TextView tv_5 = conview.findViewById(R.id.tv_5);
            tv_1.setText(i + 1 + "");

            tv_2.setText(getItem(i).getName());
            tv_3.setText("空车位"+getItem(i).getEmptySpace()+"个，停车费5元/小时");
            tv_4.setText(getItem(i).getAddress()+"  "+getItem(i).getDistance()+"m");
            if (getItem(i).getOpen() == 0) {
                tv_5.setText("关闭");
                conview.setBackgroundColor(Color.parseColor("#cccccc"));
            } else if (getItem(i).getEmptySpace() == 0) {
                tv_5.setText("已满");
                conview.setBackgroundColor(Color.parseColor("#cccccc"));
            } else {
                tv_5.setBackgroundResource(R.mipmap.arrow_right);
                tv_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),ParkInfoActivity.class);
                        intent.putExtra("p", i);
                        startActivity(intent);
                    }
                });
            }
            return conview;
        }
    }

}
