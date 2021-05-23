package org.techtown.ThreeMate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.String.valueOf;

public class MainActivity extends Activity implements TextWatcher {
    private int dlgCode = 0;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Button quizbtn, diarybtn, mapbtn, roulbtn;
    //스무고개, 다이어리, 지도, 룰렛 버튼
    //private AutoCompleteTextView autoComplete;
    private TextView hidden;
    private ArrayList<String> foodOneTime = new ArrayList<String>();
    private CardView cv_userInfo;
    private TextView tv;
    private Button search;
    private GpsTracker gpsTracker;
    private TextView tv_age;
    private TextView tv_bmi;
    private TextView tv_bmr;


    MapPOIItem marker = new MapPOIItem();
    private ArrayList<String> matchFoods = new ArrayList<String>();
    private ArrayList<String> matchFoods2 = new ArrayList<String>();
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

    /**
     * 데이터 관련
     */
    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri filePath;
    private String userUID;
    private String userName;
    private String userProfile;
    private String userEmail;
    private String bornDate;
    private String gender;
    private String bodyLength;
    private String bodyWeight;
    private SQLiteManager sqLiteManager;
    private ArrayList<String> spy = new ArrayList<String>();
    private TextView tv_userInfo;
    private Button btn_logout;
    private CircleImageView iv_profile;
    private TextView tv_nickname;
    private int age;
    private String bmr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sqLiteManager = new SQLiteManager(getApplicationContext(), "ThreeMate2.db", null, 1);
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화.
        user = auth.getCurrentUser();
        userUID = user.getUid();
        userProfile = user.getPhotoUrl().toString();
        userName = user.getDisplayName();
        userEmail = user.getEmail();
        gpsTracker = new GpsTracker(MainActivity.this);





        firebaseUpdate();


        final double latitude = gpsTracker.getLatitude();
        final double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);

        cv_userInfo = findViewById(R.id.cv_userInfo);
        tv_age = findViewById(R.id.tv_age);
        tv_bmi = findViewById(R.id.tv_bmi);
        tv_bmr = findViewById(R.id.tv_bmr);
        tv_userInfo = findViewById(R.id.tv_userInfo);
        tv_nickname = findViewById(R.id.tv_nickname);
        iv_profile = findViewById(R.id.iv_profile);
        btn_logout = findViewById(R.id.btn_logout);
        updateUserInfo();


        hidden = findViewById(R.id.hidden);
        search = findViewById(R.id.search);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                sqLiteManager.deleteAll();
                Toast.makeText(getApplicationContext(),"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        cv_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(MainActivity.this);
                customDialog.show();
            }
        });



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

    private void updateUserInfo() {
        spy.clear();
        ArrayList<JSONObject> array = sqLiteManager.getResultUser(); // DB의 내용을 배열단위로 모두 가져온다
        try {
            Calendar current = Calendar.getInstance();
            int currentYear = current.get(Calendar.YEAR);
            int currentMonth = current.get(Calendar.MONTH)+1;
            int currentDay = current.get(Calendar.DAY_OF_MONTH);
            database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
            databaseReference = database.getReference(userUID); // DB 테이블 연결
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                        User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                        if (user.getBodyLength() != null){
                            userUID = user.getUserUID();
                            userName = user.getUserName();
                            userProfile = user.getUserProfile();;
                            userEmail = user.getUserEmail();
                            bornDate = user.getBornDate();
                            gender = user.getGender();
                            bodyLength= user.getBodyLength();
                            bodyWeight = user.getBodyWeight();

                            sqLiteManager.insertUser(
                                    userName,
                                    userProfile,
                                    bornDate,
                                    gender,
                                    bodyLength,
                                    bodyWeight);

                            spy.add(userName);
                            int birthYear = Integer.valueOf(bornDate.substring(0,4));
                            int birthMonth = Integer.valueOf(bornDate.substring(5,7));
                            int birthDay = Integer.valueOf(bornDate.substring(8,10));
                            age = currentYear-birthYear;
                            if (birthMonth * 100 + birthDay >= currentMonth * 100 + currentDay){
                                age = currentYear-birthYear-1;
                            }
                            Log.d("Lee", String.valueOf(birthMonth * 100 + birthDay) + String.valueOf(currentMonth * 100 + currentDay));
                            Double bmi =  Double.valueOf(bodyWeight) / ((Double.valueOf(bodyLength)/100) *  (Double.valueOf(bodyLength)/100)) ;
                             bmr = gender.equals("남")?String.format("%.2f",(66.47+(13.75*Double.valueOf(bodyWeight) )+(5*Double.valueOf(bodyLength)) - (6.76 * Double.valueOf(age))))
                                    :String.format("%.2f",(665.1+(9.56*Double.valueOf(bodyWeight) )+(1.85*Double.valueOf(bodyLength)) - (4.68 * Double.valueOf(age))));
                            Log.d("Lee", bodyWeight+ String.valueOf((Double.valueOf(bodyLength)/10) *  (Double.valueOf(bodyLength)/10)) );
                            if (bmi > 26.35){
                                tv_bmi.setText("BMI : " + String.format("%.2f",bmi) + "(비만)");
                                tv_bmi.setTextColor(Color.parseColor("#BA78D9"));
                            }else if (bmi >23.32){
                                tv_bmi.setText("BMI : " + String.format("%.2f",bmi) + "(과체중)");
                                tv_bmi.setTextColor(Color.parseColor("#787AD6"));
                            }else if (bmi > 15.35){
                                tv_bmi.setText("BMI : " + String.format("%.2f",bmi) + "(정상)");
                                tv_bmi.setTextColor(Color.parseColor("#557CD5"));
                            }else{
                                tv_bmi.setText("BMI : " + String.format("%.2f",bmi) + "(저체중)");
                                tv_bmi.setTextColor(Color.parseColor("#7DA4BD"));
                            }

                            tv_userInfo.setText("   Age : " + age + " / BMI : " + bmi + " / BMR : " + bmr + "kcal" );
                            tv_age.setText("Age : " + age + "세");

                            tv_bmr.setText("BMR : " + bmr + "kcal");
                            tv_nickname.setText(userName);
                            Glide.with(getApplicationContext()).load(valueOf(userProfile)).into(iv_profile);


                        }else{



                        }



                    }
                    if (spy.size()<1){
                        CustomDialog customDialog = new CustomDialog(MainActivity.this);
                        customDialog.setCancelable(false);
                        customDialog.show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 디비를 가져오던중 에러 발생 시
                    Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }
            });



            int length = array.size(); // 배열의 길이
            for (int idx = 0; idx < length; idx++) {  // 배열의 길이만큼 반복
                JSONObject object = array.get(idx);// json의 idx번째 object를 가져와서,
                String id = object.getString("id");
                String userName = object.getString("userName");
                String userProfile = object.getString("userProfile");
                String bornDate = object.getString("bornDate");
                String gender = object.getString("gender");
                String bodyLength = object.getString("bodyLength");
                String bodyWeight = object.getString("bodyWeight");

                // 저장한 내용을 토대로 ListView에 다시 그린다.


            }
        } catch (Exception e) {
            Log.i("seo", "error : " + e);

        }
    }

    private void firebaseUpdate(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("데이터 동기화중...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        sqLiteManager = new SQLiteManager(this, "ThreeMate2.db", null, 1);




        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(userUID); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    FD fd = snapshot.getValue(FD.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    if (fd.getName() != null){
                        String name = fd.getName();
                        String kcal = fd.getKcal();
                        String carbs = fd.getCarbs();
                        String protein = fd.getProtein();
                        String fat = fd.getFat();
                        String date = fd.getDate();
                        String url = fd.getIcon();
                        String time = fd.getTime();



                        sqLiteManager.insert2(name,
                                kcal,
                                carbs,
                                protein,
                                fat,
                                date, url,time);



                    }


                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }

}