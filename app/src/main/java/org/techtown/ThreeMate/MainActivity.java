package org.techtown.ThreeMate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import net.daum.mf.map.api.MapPOIItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements TextWatcher {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Button quizbtn, diarybtn, mapbtn, roulbtn;
    //스무고개, 다이어리, 지도, 룰렛 버튼
    //private AutoCompleteTextView autoComplete;
    private TextView hidden;
    private ArrayList<String> foodOneTime = new ArrayList<String>();

    private TextView tv;
    private Button search;
    private GpsTracker gpsTracker;
    MapPOIItem marker = new MapPOIItem();
    private ArrayList<String> matchFoods = new ArrayList<String>();
    private ArrayList<String> matchFoods2 = new ArrayList<String>();
    private ArrayList<String> matchFoods3 = new ArrayList<String>();
    private ArrayList<String> imageurl = new ArrayList<String>();
    private ArrayList<String> placeName = new ArrayList<String>();
    private ArrayList<String> address_name = new ArrayList<String>();
    private ArrayList<String> categoryName = new ArrayList<String>();
    private ArrayList<String> phone = new ArrayList<String>();
    private ArrayList<String> place_url = new ArrayList<String>();
    private ArrayList<String> road_address_name = new ArrayList<String>();
    private ArrayList<String> x = new ArrayList<String>();
    private ArrayList<String> y = new ArrayList<String>();
    String data;
    private String name2;
    private String image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        gpsTracker = new GpsTracker(MainActivity.this);

        CustomDialog customDialog = new CustomDialog(MainActivity.this);
        customDialog.setCancelable(false);
        customDialog.show();



        final double latitude = gpsTracker.getLatitude();
        final double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);




        hidden = findViewById(R.id.hidden);
        search = findViewById(R.id.search);




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
                myIntent.putExtra("matchFoods", matchFoods);
                myIntent.putExtra("matchFoods2", matchFoods2);
                myIntent.putExtra("imageurl", imageurl);
                myIntent.putExtra("foodOneTime", foodOneTime);

                startActivity(myIntent);
            }
        });
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        search.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        search.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });

        quizbtn = findViewById(R.id.button1);
        quizbtn.setOnClickListener(new View.OnClickListener() {
            //스무고개 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, menuQuiz.class);
                startActivity(intent);

            }
        });
        quizbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        quizbtn.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        quizbtn.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });

        diarybtn = findViewById(R.id.button2);
        diarybtn.setOnClickListener(new View.OnClickListener() {
            //다이어리 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Diary.class);
                startActivity(intent);
            }
        });
        diarybtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        diarybtn.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        diarybtn.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });

        mapbtn = findViewById(R.id.button3);
        mapbtn.setOnClickListener(new View.OnClickListener() {
            //지도 실행
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        data = request("FD6", 1, latitude, longitude);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {

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
                                    }

                                    Intent myIntent = new Intent(MainActivity.this, MenuMap.class);
                                    myIntent.putExtra("placeName", placeName);
                                    myIntent.putExtra("address_name", address_name);
                                    myIntent.putExtra("phone", phone);
                                    myIntent.putExtra("place_url", place_url);
                                    myIntent.putExtra("road_address_name", road_address_name);
                                    myIntent.putExtra("categoryName", categoryName);
                                    myIntent.putExtra("x", x);
                                    myIntent.putExtra("y", y);

                                    startActivity(myIntent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    }
                }).start();



            }
        });
        mapbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mapbtn.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        mapbtn.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });

        roulbtn = findViewById(R.id.button4);
        roulbtn.setOnClickListener(new View.OnClickListener() {
            //룰렛 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RouletteActivity.class);
                startActivity(intent);

            }
        });
        roulbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        roulbtn.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        roulbtn.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });


        try {
            JSONObject json = new JSONObject(getJsonString(this));      // json 파일에 있는것을 Object형태로 변환
            JSONArray array = new JSONArray(json.getString("음식"));      // json 파일의 "음식"에 해당하는 object를 JsonArray 형태로 변환

            // json "음식"에 있는 원소의 양만큼 반복.
            // 모든 음식을 하나씩 가져와서 hot, sugar, solt, food 키의 값이 맞는지 비교하고, 맞다면 matchFoods에 삽입.
            for (int idx = 0; idx < array.length(); idx++) {
                JSONObject object = new JSONObject(array.get(idx).toString());
                String name = object.getString("name");
                String image = object.getString("image");
                String name2 = object.getString("name2");


                if (name.length() >= 1) {
                    matchFoods.add(name);
                    matchFoods2.add(name2);
                    imageurl.add(image);
                }


            }


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 매칭된 음식이 하나이상 있으면
            if (matchFoods.size() > 0) {

                for (int i = 0; i < matchFoods.size(); i++) {
                    int j = (int) (Math.random() * matchFoods.size());
                    String tmp = "";

                    tmp = matchFoods.get(i);
                    matchFoods.set(i, matchFoods.get(j));
                    matchFoods.set(j, tmp);

                    tmp = matchFoods2.get(i);
                    matchFoods2.set(i, matchFoods2.get(j));
                    matchFoods2.set(j, tmp);

                    tmp = imageurl.get(i);
                    imageurl.set(i, imageurl.get(j));
                    imageurl.set(j, tmp);

                }
           /*      System.out.println("Start..." + new Date());
                // delay 5 seconds
                Thread.sleep(1300);

            */



            }
            // 매칭된 음식이 없으면

        } catch (JSONException e) {
            Log.i("lee", "error2: " + e);
        }



    }

    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
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

    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub

    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

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

    private static String getJsonString(Context context){
        String json = "";
        try {
            InputStream is = context.getAssets().open("jsons/test.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return json;
    }


    private static String request(String category_group_code, int page, double latitude, double longitude) {
        StringBuilder output = new StringBuilder();
        String line = null;
        try {
            URL url = new URL("http://dapi.kakao.com/v2/local/search/category.json?category_group_code="+category_group_code+
                    "&page=" + page + "&size=15&sort=distance" +
                    "&x=" + longitude + "&y=" + latitude);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(3 * 1000);
                conn.setReadTimeout(3 * 1000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.addRequestProperty("Authorization", "KakaoAK " + MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY); //key값 설정
                int resCode = conn.getResponseCode();
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
    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */


}