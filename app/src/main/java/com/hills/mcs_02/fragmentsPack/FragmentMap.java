package com.hills.mcs_02.fragmentsPack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hills.mcs_02.dataBeans.LonAndLat;
import com.hills.mcs_02.networkClasses.interfacesPack.GetRequestMapTaskLoc;
import com.hills.mcs_02.R;

public class FragmentMap extends Fragment implements LocationSource, AMapLocationListener,View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context mContext;

    /** AMap is the map object */
    private AMap aMap;
    private MapView mapView;
    private ImageView iconCompass;
    /** declare the AMapLocationClient class object and locate the initiator */
    private AMapLocationClient mLocationClient = null;
    /** Declare the mLocationOption object and locate the parameters */
    public AMapLocationClientOption mLocationOption = null;
    /** declare an MListener object to locate the listener */
    private OnLocationChangedListener mListener = null;
    /** Identifies whether the location information and user relocation are displayed only once */
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
    private List<LonAndLat> mRequestTaskLocList;
    private List<LonAndLat> locList = new ArrayList<>();
    private float lastBearing = 0;
    private RotateAnimation rotateAnimation;


    public FragmentMap() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        /** Get the map control reference */
        mapView = (MapView) view.findViewById(R.id.map);
        chBtn = (Button) view.findViewById(R.id.Chinese);
        enBtn = (Button) view.findViewById(R.id.English);
        satelliteBtn = (Button) view.findViewById(R.id.weixing);
        normalBtn = (Button) view.findViewById(R.id.putong);
        nightBtn = (Button) view.findViewById(R.id.yejian);
        iconCompass = (ImageView) view.findViewById(R.id.icon_compass);
        bitmap = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_marka));
        bitmap2 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_markb));
        mRequestTaskLocList = new ArrayList<LonAndLat>();

        /** Map lifecycle management */
        mapView.onCreate(savedInstanceState);
        /** Map initialization */
        initMap();
        /** position */
        location();
        /** Display the task location */
        initTaskLoc();
        /**  Listen for task click events */
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
            /** Sets the display location button to be clickable */
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);
            /** Whether to display the position button */
            settings.setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
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
        locList.add(new LonAndLat(0,108.765882,34.030701));
        locList.add(new LonAndLat(1,108.771032,34.034507));
        locList.add(new LonAndLat(15,108.761707,34.036803));
        locList.add(new LonAndLat(16,108.769196,34.0325));
        locList.add(new LonAndLat(17,108.768831,34.035381));
        locList.add(new LonAndLat(18,108.762608,34.030331));
        locList.add(new LonAndLat(19,108.772865,34.032038));
        locList.add(new LonAndLat(20,108.925139,34.245004));
        locList.add(new LonAndLat(21,108.963419,34.24997));
        locList.add(new LonAndLat(22,108.939902,34.282741));
        locList.add(new LonAndLat(23,108.947798,34.251247));
        locList.add(new LonAndLat(2,117.200912,39.109817));
        locList.add(new LonAndLat(3,116.446893,39.857722));
        locList.add(new LonAndLat(4,116.393334,39.873664));
        locList.add(new LonAndLat(5,116.42698,39.905144));
        locList.add(new LonAndLat(6,116.379172,39.907975));
        locList.add(new LonAndLat(7,116.351535,39.890723));
        locList.add(new LonAndLat(8,116.370761,39.951612));
        locList.add(new LonAndLat(9,116.449038,39.919496));
        locList.add(new LonAndLat(10,116.403076,39.937168));
        locList.add(new LonAndLat(11,116.399027,39.845868));
        locList.add(new LonAndLat(12,116.433187,39.941617));
        locList.add(new LonAndLat(13,116.6728,40.130981));
        locList.add(new LonAndLat(14,116.098764,39.614545));

        for(int temp = 0; temp< locList.size()-4; temp++){
            MarkerOptions markerOption = new MarkerOptions();
            LonAndLat loc = locList.get(temp);
            LatLng point = new LatLng(loc.getLat(), loc.getLon());
            markerOption.position(point);
            markerOption.draggable(true); /** Set marker to be draggable */
            markerOption.icon(bitmap);
            /** When Marker is set to display on the ground, you can pull down the map with two fingers to view the effect*/
            markerOption.setFlat(true);
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
                        LonAndLat loc = locList.get(temp);
                        LatLng point = new LatLng(loc.getLat(), loc.getLon());
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(point);
                        markerOption.draggable(true); /** Set marker to be draggable */
                        markerOption.icon(bitmap);
                        /** When Marker is set to display on the ground, you can pull down the map with two fingers to view the effect*/
                        markerOption.setFlat(true);
                        aMap.addMarker(markerOption);
                    }
                    fl = false;
                }else{
                    fl = true;
                    int area = 100;
                    int area1 = 10000;
                    for(int temp = 0; temp < locList.size()-4; temp++){
                        LonAndLat loc = locList.get(temp);
                        LatLng point = new LatLng(loc.getLat(),loc.getLon());
                        CircleOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
                                .center(point).strokeColor(0x384d73b3).radius(area);
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(point);
                        markerOption.draggable(true);/** Set marker to be draggable */
                        markerOption.icon(bitmap);
                        /** When Marker is set to display on the ground, you can pull down the map with two fingers to view the effect*/
                        markerOption.setFlat(true);
                        aMap.addMarker(markerOption);
                        aMap.addCircle(ooCircle);
                        area = area + 100;
                    }

                }
                return true;
            }
        });
    }

    public List<LonAndLat> getRequest(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        GetRequestMapTaskLoc requestGetTaskLocList = retrofit.create(GetRequestMapTaskLoc.class);

        Call<ResponseBody> call = requestGetTaskLocList.getCall();


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<LonAndLat>>() {}.getType();
                    try{

                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequestTaskLocList = gson.fromJson(temp, type);
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
        /** Initialize the location */
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        /** Set the location callback listener */
        mLocationClient.setLocationListener(this);
        /**  Initialize the location parameter */
        mLocationOption = new AMapLocationClientOption();
        /** Set the location mode */
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        /** Sets whether to return address information */
        mLocationOption.setNeedAddress(true);
        /** Sets whether to locate only once */
        mLocationOption.setOnceLocation(false);
        /** Sets whether to allow emulation locations */
        mLocationOption.setMockEnable(false);
        /** Set the location interval */
        mLocationOption.setInterval(2000);
        /** Set the location parameters for the location client object */
        mLocationClient.setLocationOption(mLocationOption);
        /** Start positioning */
        mLocationClient.startLocation();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocationClient.stopLocation(); /** Stop positioning */
        mLocationClient.onDestroy(); /** Destroy the location client */
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();
                aMapLocation.getLatitude();
                aMapLocation.getLongitude();
                aMapLocation.getAccuracy();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);
                aMapLocation.getAddress();
                aMapLocation.getCountry();
                aMapLocation.getProvince();
                aMapLocation.getCity();
                aMapLocation.getDistrict();/** Urban area information */
                aMapLocation.getStreet(); /**  Street information */
                aMapLocation.getStreetNum(); /** Street number information */
                aMapLocation.getCityCode();/** City code */
                aMapLocation.getAdCode(); /** Locale code */

                if (isFirstLoc) {
                    /** Set the zoom level */
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    /** Move map to anchor point */
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    /** Click the location button to move the center of the map to the anchor point */
                    mListener.onLocationChanged(aMapLocation);
                    
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
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
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
