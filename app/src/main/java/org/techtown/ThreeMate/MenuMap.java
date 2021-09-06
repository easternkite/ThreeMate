package org.techtown.ThreeMate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MenuMap extends AppCompatActivity implements Serializable, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, MapView.POIItemEventListener, CalloutBalloonAdapter {
    private static final String LOG_TAG = "MainActivity";
    private long backKeyPressedTime = 0;
    private Toast toast;
    RecyclerView recyclerView;
    PersonAdapter adapter;
    private GpsTracker gpsTracker;
    private String data;
    private TextView textView;
    public MapView mMapView;
    private int rtOn = 1;
    private int cfOn = 0;
    private int csOn = 0;
    ArrayList<String> placeName ;
    ArrayList<String> categoryName ;
    ArrayList<String> phone;
    ArrayList<String> place_url ;
    ArrayList<String> address_name ;
    ArrayList<String> road_address_name;
    ArrayList<String> x ;
    ArrayList<String> y ;
    ArrayList<String> matchScore ;
    final String[] words = new String[] {"정보","전화걸기"};

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menumap);





        Intent intent = getIntent();
        placeName = (ArrayList<String>) intent.getSerializableExtra("placeName");
       categoryName = (ArrayList<String>) intent.getSerializableExtra("categoryName");
       phone = (ArrayList<String>) intent.getSerializableExtra("phone");
            place_url = (ArrayList<String>) intent.getSerializableExtra("place_url");
        address_name = (ArrayList<String>) intent.getSerializableExtra("address_name");
        road_address_name = (ArrayList<String>) intent.getSerializableExtra("road_address_name");
        x = (ArrayList<String>) intent.getSerializableExtra("x");
        y = (ArrayList<String>) intent.getSerializableExtra("y");
        matchScore = (ArrayList<String>) intent.getSerializableExtra("matchScore");
        mMapView = (MapView) findViewById(R.id.map_view);
        //mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setCurrentLocationEventListener(this);
        mMapView.setPOIItemEventListener(poiItemEventListener);
        textView = findViewById(R.id.textView);
        final Button button = findViewById(R.id.button);
        final Button button2 = findViewById(R.id.button2);
        final Button button3 = findViewById(R.id.button3);


        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PersonAdapter();
        adapter.setOnItemClickListener(new OnPersonItemClickListener() {
            @Override
            public void onItemClick(PersonAdapter.ViewHolder holder, View view,  int position) {
                MapPOIItem[] mapPOIItems = mMapView.getPOIItems();
                Item item = adapter.getItem(position);
                mMapView.selectPOIItem(mapPOIItems[position],true);


            }
        });

        final AnimationSet set = new AnimationSet(true);

        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,-1,
                Animation.RELATIVE_TO_SELF,0         );
        rtl.setDuration(500);
        set.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(700);
        set.addAnimation(alpha);

        LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
        recyclerView.setLayoutAnimation(controller);






        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(MenuMap.this);

        final double latitude = gpsTracker.getLatitude();
        final double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);

        final MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude)));



        button.setBackground(getDrawable(R.drawable.restaurant_btn_pressed));

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (rtOn == 0)
                            button.setBackground(getDrawable(R.drawable.rt_toched));

                        break;
                    case MotionEvent.ACTION_UP:
                        if (rtOn ==0)
                        button.setBackground(getDrawable(R.drawable.restaurant_btn));
                        break;
                }
                return false;
            }
        });

        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (cfOn == 0)
                        button2.setBackground(getDrawable(R.drawable.cf_touched));
                        break;
                    case MotionEvent.ACTION_UP:
                        if (cfOn == 0)
                        button2.setBackground(getDrawable(R.drawable.caffe_btn));
                        break;
                }
                return false;
            }
        });

        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (csOn == 0)
                        button3.setBackground(getDrawable(R.drawable.cs_toiched));
                        break;
                    case MotionEvent.ACTION_UP:
                        if (csOn == 0)
                        button3.setBackground(getDrawable(R.drawable.convenience_btn));
                        break;
                }
                return false;
            }
        });







        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                final ArrayList<String> placeName = (ArrayList<String>) intent.getSerializableExtra("placeName");
                final ArrayList<String> categoryName = (ArrayList<String>) intent.getSerializableExtra("categoryName");
                final ArrayList<String> phone = (ArrayList<String>) intent.getSerializableExtra("phone");
                final ArrayList<String> place_url = (ArrayList<String>) intent.getSerializableExtra("place_url");
                final ArrayList<String> address_name = (ArrayList<String>) intent.getSerializableExtra("address_name");
                final ArrayList<String> road_address_name = (ArrayList<String>) intent.getSerializableExtra("road_address_name");
                final ArrayList<String> x = (ArrayList<String>) intent.getSerializableExtra("x");
                final ArrayList<String> y = (ArrayList<String>) intent.getSerializableExtra("y");
                final ArrayList<String> matchScore = (ArrayList<String>) intent.getSerializableExtra("matchScore");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPoint,2f));
                        for (int i = 0; i < placeName.size(); i++) {
                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(y.get(i)), Double.parseDouble(x.get(i)));
                            MapPOIItem marker = new MapPOIItem();
                            marker.setItemName(placeName.get(i));
                            marker.setTag(i);
                            marker.setMapPoint(mapPoint);
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomImageResourceId(R.drawable.custom_marker_fd);
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomSelectedImageResourceId(R.drawable.custom_marker_fd);
                            marker.setCustomImageAutoscale(true);
                            marker.setCustomImageAnchor(0.5f, 1.0f);
                            mMapView.addPOIItem(marker);

                            adapter.addItem(new Item(R.drawable.icon_pz_1 , placeName.get(i), categoryName.get(i), road_address_name.get(i), phone.get(i)));
                            recyclerView.setAdapter(adapter);

                        }

                    }
                });
            }
        }).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rtOn=0;
                cfOn=0;
                csOn=0;
                button.setBackground(getDrawable(R.drawable.restaurant_btn));
                button2.setBackground(getDrawable(R.drawable.caffe_btn));
                button3.setBackground(getDrawable(R.drawable.convenience_btn));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.removeItem();
                        mMapView.removeAllPOIItems();


                        data = request("맛집","FD6", 1, latitude, longitude);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    button.setBackground(getDrawable(R.drawable.restaurant_btn_pressed));
                                    rtOn=1;
                                    textView.setText("");
                                    placeName.clear();
                                    categoryName.clear();
                                    address_name.clear();
                                    phone.clear();
                                    place_url.clear();
                                    road_address_name.clear();
                                    y.clear();
                                    x.clear();
                                    JSONObject json = new JSONObject(data);
                                    JSONArray array = new JSONArray(json.getString("documents"));

                                    for (int idx = 0; idx < array.length(); idx++) {
                                        JSONObject object = new JSONObject(array.get(idx).toString());
                                        String name = object.getString("place_name");
                                        String address = object.getString("address_name");
                                        String category_name = object.getString("category_name");
                                        String call = object.getString("phone");
                                        String url = object.getString("place_url");
                                        String roadAddress = object.getString("road_address_name");
                                        String longitude = object.getString("x");
                                        String latitude = object.getString("y");

                                        placeName.add(name);
                                        address_name.add(address);
                                        phone.add(call);
                                        place_url.add(url);
                                        road_address_name.add(roadAddress);
                                        categoryName.add(category_name);
                                        x.add(longitude);
                                        y.add(latitude);

                                  /*      textView.append("- 건물명 : " + placeName.get(idx) + "\n" +
                                                "- 주소 명 : " + address_name.get(idx) + "\n" +
                                                "- 전화번호 : " + phone.get(idx) + "\n" +
                                                "- URL : " + place_url.get(idx) + "\n" +
                                                "- 도로명 주소 : " + road_address_name.get(idx) + "\n" +
                                                "- 위도 : " + y.get(idx) + "\n" +
                                                "- 경도 : " + x.get(idx) + "\n" + "\n");

                                   */
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                for (int i = 0; i < placeName.size(); i++) {
                                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(y.get(i)), Double.parseDouble(x.get(i)));
                                    MapPOIItem marker = new MapPOIItem();
                                    marker.setItemName(placeName.get(i));
                                    marker.setTag(i);
                                    ;
                                    marker.setMapPoint(mapPoint);
                                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomImageResourceId(R.drawable.custom_marker_fd);
                                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomSelectedImageResourceId(R.drawable.custom_marker_fd);

                                    marker.setCustomImageAutoscale(true);
                                    marker.setCustomImageAnchor(0.5f, 1.0f);

                                    mMapView.addPOIItem(marker);
                                    adapter.addItem(new Item(R.drawable.icon_pz_1 , placeName.get(i), categoryName.get(i), road_address_name.get(i), phone.get(i)));
                                    recyclerView.setAdapter(adapter);

                                }

                            }
                        });
                    }
                }).start();
                LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                recyclerView.setLayoutAnimation(controller);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setBackground(getDrawable(R.drawable.restaurant_btn));
                button2.setBackground(getDrawable(R.drawable.caffe_btn));
                button3.setBackground(getDrawable(R.drawable.convenience_btn));
                rtOn=0;
                cfOn=0;
                csOn=0;
                mMapView.removeAllPOIItems();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.removeItem();
                        //for (int i = 1; i < 5; i++) {
                        data = request("맛집","CE7", 1, latitude, longitude);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    cfOn=1;
                                    button2.setBackground(getDrawable(R.drawable.caffe_btn_pressed));
                                    //버튼 중복클릭

                                    textView.setText("");
                                    categoryName.clear();
                                    placeName.clear();
                                    address_name.clear();
                                    phone.clear();
                                    place_url.clear();
                                    road_address_name.clear();
                                    y.clear();
                                    x.clear();
                                    JSONObject json = new JSONObject(data);
                                    JSONArray array = new JSONArray(json.getString("documents"));

                                    for (int idx = 0; idx < array.length(); idx++) {
                                        JSONObject object = new JSONObject(array.get(idx).toString());
                                        String name = object.getString("place_name");
                                        String address = object.getString("address_name");
                                        String category_name = object.getString("category_name");
                                        String call = object.getString("phone");
                                        String url = object.getString("place_url");
                                        String roadAddress = object.getString("road_address_name");
                                        String longitude = object.getString("x");
                                        String latitude = object.getString("y");

                                        placeName.add(name);
                                        address_name.add(address);
                                        phone.add(call);
                                        place_url.add(url);
                                        road_address_name.add(roadAddress);
                                        categoryName.add(category_name);
                                        x.add(longitude);
                                        y.add(latitude);
                                        adapter.addItem(new Item(R.drawable.icon_pz_2,placeName.get(idx), categoryName.get(idx), road_address_name.get(idx), phone.get(idx)));
                                        recyclerView.setAdapter(adapter);
                                      /*  textView.append("- 건물명 : " + placeName.get(idx) + "\n" +
                                                "- 주소 명 : " + address_name.get(idx) + "\n" +
                                                "- 전화번호 : " + phone.get(idx) + "\n" +
                                                "- URL : " + place_url.get(idx) + "\n" +
                                                "- 도로명 주소 : " + road_address_name.get(idx) + "\n" +
                                                "- 위도 : " + y.get(idx) + "\n" +
                                                "- 경도 : " + x.get(idx) + "\n" + "\n");

                                       */
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                for (int i = 0; i < placeName.size(); i++) {
                                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(y.get(i)), Double.parseDouble(x.get(i)));
                                    MapPOIItem marker = new MapPOIItem();
                                    marker.setItemName(placeName.get(i));
                                    marker.setTag(i);
                                    marker.setMapPoint(mapPoint);
                                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomImageResourceId(R.drawable.custom_marker_cf);
                                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomSelectedImageResourceId(R.drawable.custom_marker_cf);

                                    marker.setCustomImageAutoscale(true);
                                    marker.setCustomImageAnchor(0.5f, 1.0f);

                                    mMapView.addPOIItem(marker);

                                }

                            }
                        });
                    }
                    //   }
                }).start();
                LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                recyclerView.setLayoutAnimation(controller);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeAllPOIItems();
                button.setBackground(getDrawable(R.drawable.restaurant_btn));
                button2.setBackground(getDrawable(R.drawable.caffe_btn));
                button3.setBackground(getDrawable(R.drawable.convenience_btn));
                rtOn=0;
                cfOn=0;
                csOn=0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.removeItem();
                        recyclerView.removeAllViewsInLayout();
                        // for (int i = 1; i < 5; i++) { 이거 결과 30개이상 띄우려고 쓰는데 너무 자원소비가 심함.
                        data = request("편의점","CS2", 1, latitude, longitude);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    csOn=1;
                                    button3.setBackground(getDrawable(R.drawable.convenience_btn_pressed));
                                    textView.setText("");
                                    placeName.clear();
                                    categoryName.clear();
                                    address_name.clear();
                                    phone.clear();
                                    place_url.clear();
                                    road_address_name.clear();
                                    y.clear();
                                    x.clear();
                                    JSONObject json = new JSONObject(data);
                                    JSONArray array = new JSONArray(json.getString("documents"));

                                    for (int idx = 0; idx < array.length(); idx++) {
                                        JSONObject object = new JSONObject(array.get(idx).toString());
                                        String name = object.getString("place_name");
                                        String address = object.getString("address_name");
                                        String call = object.getString("phone");
                                        String category_name = object.getString("category_name");
                                        String url = object.getString("place_url");
                                        String roadAddress = object.getString("road_address_name");
                                        String longitude = object.getString("x");
                                        String latitude = object.getString("y");

                                        placeName.add(name);
                                        address_name.add(address);
                                        phone.add(call);
                                        place_url.add(url);
                                        road_address_name.add(roadAddress);
                                        categoryName.add(category_name);
                                        x.add(longitude);
                                        y.add(latitude);
                                        adapter.addItem(new Item(R.drawable.icon_pz_3,placeName.get(idx), categoryName.get(idx), road_address_name.get(idx), phone.get(idx)));
                                        recyclerView.setAdapter(adapter);
                                    /**    textView.append("- 건물명 : " + placeName.get(idx) + "\n" +
                                      *          "- 주소 명 : " + address_name.get(idx) + "\n" +
                                      *          "- 전화번호 : " + phone.get(idx) + "\n" +
                                      *          "- URL : " + place_url.get(idx) + "\n" +
                                      *          "- 도로명 주소 : " + road_address_name.get(idx) + "\n" +
                                      *          "- 위도 : " + y.get(idx) + "\n" +
                                      *          "- 경도 : " + x.get(idx) + "\n" + "\n");
                                     */
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                for (int i = 0; i < placeName.size(); i++) {
                                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(y.get(i)), Double.parseDouble(x.get(i)));
                                    MapPOIItem marker = new MapPOIItem();
                                    marker.setItemName(placeName.get(i));
                                    marker.setTag(i);
                                    marker.setMapPoint(mapPoint);
                                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomImageResourceId(R.drawable.custom_marker_hr);
                                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomSelectedImageResourceId(R.drawable.custom_marker_hr);

                                    marker.setCustomImageAutoscale(true);
                                    marker.setCustomImageAnchor(0.5f, 1.0f);

                                    mMapView.addPOIItem(marker);

                                }

                            }
                        });
                    }
                    // }
                }).start();
                LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                recyclerView.setLayoutAnimation(controller);
            }
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }




    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(MenuMap.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MenuMap.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MenuMap.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MenuMap.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MenuMap.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MenuMap.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MenuMap.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }
    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MenuMap.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }




    private static String request(String quary, String category_group_code, int page, double latitude, double longitude) {
        StringBuilder output = new StringBuilder();
        String line = null;
        try {
            URL url = new URL("http://dapi.kakao.com/v2/local/search/keyword.json?query=" + quary +
                    "&category_group_code=" + category_group_code +
                    "&page=" + page + "&size=15&sort=distance" +
                    "&x=" + longitude + "&y=" + latitude);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(3 * 1000);
                conn.setReadTimeout(3 * 1000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.addRequestProperty("Authorization", "KakaoAK " + MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY); //key값 설정
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }

                    output.append(line + "\n");
                }
                reader.close();
                conn.disconnect();
            }


        } catch (Exception ex) {
            Log.d("Lee", "예외 발생함 : " + ex.toString());
        }

        Log.d("Lee", "응답 -> " + output.toString());

        return output.toString();

    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
     ;
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
      }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

            finish();
            toast.cancel();
        }

    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }



    private MapView.POIItemEventListener poiItemEventListener = new MapView.POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {


        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
            new AlertDialog.Builder(MenuMap.this).setItems(words, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Toast.makeText(getApplicationContext(), mapPOIItem.getItemName() + " 정보", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(place_url.get(mapPOIItem.getTag())));
                            startActivity(intent);
                            break;
                        case 1:
                            if (phone.get(mapPOIItem.getTag()).equals(null)||phone.get(mapPOIItem.getTag()).equals("")){
                                Toast.makeText(getApplicationContext(),"'" + mapPOIItem.getItemName()+ "' 업체의 전화번호를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), mapPOIItem.getItemName() + "에 전화걸기", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.get(mapPOIItem.getTag())));
                                startActivity(intent2);
                            }


                    }

                }
            }).show();




        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };


}


