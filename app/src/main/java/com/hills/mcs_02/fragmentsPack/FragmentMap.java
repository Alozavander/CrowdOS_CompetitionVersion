package com.hills.mcs_02.fragmentsPack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import com.hills.mcs_02.dataBeans.Lon_Lat;
import com.hills.mcs_02.networkClasses.interfacesPack.GetRequestMapTaskLoc;
import com.hills.mcs_02.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentMap#newInstance} factory method to
 * create an instance of this fragment.
 */
//地图分页面，还未开始编写
public class FragmentMap extends Fragment implements LocationSource, AMapLocationListener,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    //AMap是地图对象
    private AMap aMap;
    private MapView mapView;
    private ImageView iconCompass;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private MyLocationStyle myLocationStyle = new MyLocationStyle();

    boolean fl = false;
    private String TAG = "fragment_map";
    private Button chBtn;
    private Button enBtn;
    private Button satelliteBtn;
    private Button normalBtn;
    private Button nightBtn;
    private BitmapDescriptor bitmap;
    private BitmapDescriptor bitmap2;
    private List<Lon_Lat> mRequestTaskLocList;
    private List<Lon_Lat> locList = new ArrayList<>();
    private float lastBearing = 0;
    private RotateAnimation rotateAnimation;


    public FragmentMap() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMap newInstance(String param1, String param2) {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //获取地图控件引用
        mapView = (MapView) view.findViewById(R.id.map);
        chBtn = (Button) view.findViewById(R.id.Chinese);
        enBtn = (Button) view.findViewById(R.id.English);
        satelliteBtn = (Button) view.findViewById(R.id.weixing);
        normalBtn = (Button) view.findViewById(R.id.putong);
        nightBtn = (Button) view.findViewById(R.id.yejian);
        iconCompass = (ImageView) view.findViewById(R.id.icon_compass);
        bitmap = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_marka));
        bitmap2 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_markb));
        mRequestTaskLocList = new ArrayList<Lon_Lat>();

        //地图生命周期管理
        mapView.onCreate(savedInstanceState);
        //地图初始化
        initMap();
        //定位
        location();
        //显示任务位置
        initTaskLoc();
        //监听任务点击事件
        setMapListener();

        chBtn.setOnClickListener(this);
        enBtn.setOnClickListener(this);
        satelliteBtn.setOnClickListener(this);
        normalBtn.setOnClickListener(this);
        nightBtn.setOnClickListener(this);

        return view;
    }

    private void initMap(){
        if (aMap == null) {
            aMap = mapView.getMap();
            //设置显示定位按钮 并且可以点击
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
            aMap.setMapLanguage(AMap.ENGLISH);
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                startIvCompass(cameraPosition.bearing);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
            }
        });
    }

    private void initTaskLoc(){

        locList = getRequest();

        locList.clear();
        //西安
        locList.add(new Lon_Lat(0,108.765882,34.030701));
        locList.add(new Lon_Lat(1,108.771032,34.034507));
        locList.add(new Lon_Lat(15,108.761707,34.036803));
        locList.add(new Lon_Lat(16,108.769196,34.0325));
        locList.add(new Lon_Lat(17,108.768831,34.035381));
        locList.add(new Lon_Lat(18,108.762608,34.030331));
        locList.add(new Lon_Lat(19,108.772865,34.032038));
        locList.add(new Lon_Lat(20,108.925139,34.245004));
        locList.add(new Lon_Lat(21,108.963419,34.24997));
        locList.add(new Lon_Lat(22,108.939902,34.282741));
        locList.add(new Lon_Lat(23,108.947798,34.251247));
        //天津
        locList.add(new Lon_Lat(2,117.200912,39.109817));
        //北京（小）
        locList.add(new Lon_Lat(3,116.446893,39.857722));
        locList.add(new Lon_Lat(4,116.393334,39.873664));
        locList.add(new Lon_Lat(5,116.42698,39.905144));
        locList.add(new Lon_Lat(6,116.379172,39.907975));
        locList.add(new Lon_Lat(7,116.351535,39.890723));
        locList.add(new Lon_Lat(8,116.370761,39.951612));
        locList.add(new Lon_Lat(9,116.449038,39.919496));
        locList.add(new Lon_Lat(10,116.403076,39.937168));
        //北京（大）
        locList.add(new Lon_Lat(11,116.399027,39.845868));
        locList.add(new Lon_Lat(12,116.433187,39.941617));
        locList.add(new Lon_Lat(13,116.6728,40.130981));
        locList.add(new Lon_Lat(14,116.098764,39.614545));

        for(int i = 0; i< locList.size()-4; i++){
            MarkerOptions markerOption = new MarkerOptions();
            Lon_Lat b = locList.get(i);
            LatLng point = new LatLng(b.getLat(), b.getLon());
            markerOption.position(point);
            //markerOption.title("任务：" + b.getTaskId().toString()).snippet(b.toString());
            markerOption.draggable(true);//设置marker可拖动
            markerOption.icon(bitmap);
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            aMap.addMarker(markerOption);
        }

        //无用代码，只是为了截图，2019.8.24
        for (int temp = locList.size()-4; temp<locList.size(); temp++){
            MarkerOptions markerOption = new MarkerOptions();
            Lon_Lat b = locList.get(temp);
            LatLng point = new LatLng(b.getLat(), b.getLon());
            markerOption.position(point);
            //markerOption.title("任务：" + b.getTaskId().toString()).snippet(b.toString());
            markerOption.draggable(true);//设置marker可拖动
            markerOption.icon(bitmap2);
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            aMap.addMarker(markerOption);
        }
    }

    private void setMapListener(){
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(fl == true){
                    aMap.clear(true);
                    for(int temp = 0; temp< locList.size()-4; temp++){
                        Lon_Lat loc = locList.get(temp);
                        LatLng point = new LatLng(loc.getLat(), loc.getLon());
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(point);
                        //markerOption.title("任务：" + b.getTaskId().toString()).snippet(b.toString());
                        markerOption.draggable(true);//设置marker可拖动
                        markerOption.icon(bitmap);
                        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                        markerOption.setFlat(true);//设置marker平贴地图效果
                        aMap.addMarker(markerOption);
                    }

                    //无用代码，只是为了截图，2019.8.24
                    for (int temp = locList.size()-4; temp<locList.size(); temp++){
                        Lon_Lat loc = locList.get(temp);
                        LatLng point = new LatLng(loc.getLat(), loc.getLon());
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(point);
                        //markerOption.title("任务：" + b.getTaskId().toString()).snippet(b.toString());
                        markerOption.draggable(true);//设置marker可拖动
                        markerOption.icon(bitmap2);
                        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                        markerOption.setFlat(true);//设置marker平贴地图效果
                        aMap.addMarker(markerOption);
                    }

                    fl = false;
                }else{
                    fl = true;
                    int area = 100;
                    int area1 = 10000;
                    for(int temp = 0; temp < locList.size()-4; temp++){
                        Lon_Lat loc = locList.get(temp);
                        LatLng point = new LatLng(loc.getLat(),loc.getLon());
                        CircleOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
                                .center(point).strokeColor(0x384d73b3).radius(area);
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(point);
                        //markerOption.title("任务：" + c.getTaskId().toString()).snippet(c.toString());
                        markerOption.draggable(true);//设置marker可拖动
                        markerOption.icon(bitmap);
                        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                        markerOption.setFlat(true);//设置marker平贴地图效果
                        aMap.addMarker(markerOption);
                        aMap.addCircle(ooCircle);
                        area = area + 100;
                    }

                    //无用代码，只是为了截图，2019.8.24
                    for (int temp = locList.size()-4; temp<locList.size(); temp++){
                        Lon_Lat loc = locList.get(temp);
                        LatLng point = new LatLng(loc.getLat(),loc.getLon());
                        CircleOptions ooCircle = new CircleOptions().fillColor(0x38FF8C00)
                                .center(point).strokeColor(0x38FF8C00).radius(area1);
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(point);
                        //markerOption.title("任务：" + c.getTaskId().toString()).snippet(c.toString());
                        markerOption.draggable(true);//设置marker可拖动
                        markerOption.icon(bitmap2);
                        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                        markerOption.setFlat(true);//设置marker平贴地图效果
                        aMap.addMarker(markerOption);
                        aMap.addCircle(ooCircle);
                        area1 = area1 + 10000;
                    }
                }
                return true;
            }
        });
    }

    public List<Lon_Lat> getRequest(){
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //创建网络接口实例
        GetRequestMapTaskLoc requestGetTaskLocList = retrofit.create(GetRequestMapTaskLoc.class);
        //包装发送请求
        Call<ResponseBody> call = requestGetTaskLocList.getCall();

        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Lon_Lat>>() {}.getType();
                    try{
                        //在此附近应该加入内容判定，优化响应逻辑
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequestTaskLocList = gson.fromJson(temp, type);
                        //成功获取网络请求内容后，调用内容处理方法
                        Log.i(TAG, mRequestTaskLocList.size() + "");
                    }catch (IOException exp) {
                        exp.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
        return mRequestTaskLocList;
    }

    private void startIvCompass(float bearing) {
        bearing = 360 - bearing;
        rotateAnimation = new RotateAnimation(lastBearing, bearing, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        iconCompass.startAnimation(rotateAnimation);
        lastBearing = bearing;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Chinese:
                aMap.setMapLanguage(AMap.CHINESE);
                break;
            case R.id.English:
                aMap.setMapLanguage(AMap.ENGLISH);
                break;
            case R.id.weixing:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.putong:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                break;
            case R.id.yejian:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                break;
            default:
                break;
        }
    }

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //添加图钉
                    //  aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    Toast.makeText(mContext.getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                //Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
