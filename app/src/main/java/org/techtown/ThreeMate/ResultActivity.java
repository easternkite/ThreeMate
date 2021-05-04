package org.techtown.ThreeMate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ImageView ivImage;
    private TextView Answer;
    private TextView hidden;
    private TextView tv;
    private String key="RPT4s8Z6M%2Fx8mQW4CYG1A0l3SEU5EVg818RAioq2uvIy6B05Dn3fJvWjthwkztWzuq75u6Xuqh1rtk27ZM2n7w%3D%3D";
    private String data;
    private String kcal;
    private URL Url;
    private String strUrl,strCookie,result;
    private Font font;




    private ArrayList<String> matchFoods = new ArrayList<String>();
    private ArrayList<String> matchFoods2 = new ArrayList<String>();
    private ArrayList<String> foodkcal = new ArrayList<String>();
    private ArrayList<String> foodtan = new ArrayList<String>();
    private ArrayList<String> fooddan = new ArrayList<String>();
    private ArrayList<String> foodji = new ArrayList<String>();
    private ArrayList<String> imageurl = new ArrayList<String>();
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ivImage = findViewById(R.id.iv_image);
        Answer = findViewById(R.id.Answer);
        final Button button = findViewById(R.id.button);
        tv = findViewById(R.id.tv);
        final Button foodsave = findViewById(R.id.foodsave);
        final Button main = findViewById(R.id.main);
        hidden = findViewById(R.id.hidden);




       main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnswerSetGetter.setClear();
                finish();



            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnswerSetGetter.setClear();
                Intent intent = new Intent(ResultActivity.this, menuQuiz.class);
                finish();
                startActivity(intent);


            }
        });
        foodsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Double parsekcal= 0.0;
                        Double parsetan= 0.0;
                        Double parsedan= 0.0;
                        Double parseji= 0.0;
                        if (TextUtils.isEmpty(tv.getText().toString()) || tv.getText().toString().equals("음식정보 로딩중..")){
                            Toast.makeText(ResultActivity.this, "음식정보가 로딩될 때 까지 기다려주십시오.", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Intent myIntent = new Intent(ResultActivity.this, Diary.class);
                            myIntent.putExtra("name", matchFoods.get(0));
                            myIntent.putExtra("kcal", foodkcal.get(0));
                            myIntent.putExtra("carbs", foodtan.get(0));
                            myIntent.putExtra("protein", fooddan.get(0));
                            myIntent.putExtra("fat", foodji.get(0));
                            myIntent.putExtra("url", imageurl.get(0));
                            myIntent.putExtra("num", 1);

                            startActivity(myIntent);
                            finish();
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setNegativeButton("아니오",null);
                builder.setTitle("음식 저장");
                builder.setMessage("해당 음식을 기록하기 위해 다이어리로 이동하시겠습니까?");
                builder.show();






            }
        });

        main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        main.setBackground(getDrawable(R.drawable.main_menu_btn_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        main.setBackground(getDrawable(R.drawable.main_menu_btn));
                        break;
                }
                return false;
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.setBackground(getDrawable(R.drawable.replay_btn_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        button.setBackground(getDrawable(R.drawable.replay_btn));
                        break;
                }
                return false;
            }
        });

        foodsave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        foodsave.setBackground(getDrawable(R.drawable.food_save_btn_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        foodsave.setBackground(getDrawable(R.drawable.food_save_btn));
                        break;
                }
                return false;
            }
        });





        try{
            JSONObject json = new JSONObject(getJsonString(this));      // json 파일에 있는것을 Object형태로 변환
            JSONArray array = new JSONArray(json.getString("음식"));      // json 파일의 "음식"에 해당하는 object를 JsonArray 형태로 변환

            // json "음식"에 있는 원소의 양만큼 반복.
            // 모든 음식을 하나씩 가져와서 hot, sugar, solt, food 키의 값이 맞는지 비교하고, 맞다면 matchFoods에 삽입.
            for(int idx=0; idx<array.length(); idx++){
                JSONObject object =  new JSONObject(array.get(idx).toString());
                String name = object.getString("name");
                String food = "\"" + object.getString("food") + "\"";
                String type = "\"" + object.getString("type") + "\"";
                String hot = "\"" + object.getString("hot") + "\"";
                String solt = "\"" + object.getString("solt") + "\"";
                String image = object.getString("image");
                String name2 = object.getString("name2");


                if(food.equals(AnswerSetGetter.getAnswers().get(0)) &&
                        type.equals(AnswerSetGetter.getAnswers().get(1)) &&
                        hot.equals(AnswerSetGetter.getAnswers().get(2)) &&
                        solt.equals(AnswerSetGetter.getAnswers().get(3))){
                    matchFoods.add(name);
                    matchFoods2.add(name2);

                    imageurl.add(image);

                }
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 매칭된 음식이 하나이상 있으면
            if(matchFoods.size() > 0 ){

                for (int i =0;i<matchFoods.size();i++) {
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


                Glide.with(this).load(imageurl.get(0)).into(ivImage);
                Answer.setText(matchFoods.get(0));
                hidden.setText(matchFoods2.get(0));
                setTv();

            }
            // 매칭된 음식이 없으면
            else{
                builder.setCancelable(false);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AnswerSetGetter.setClear();
                        Intent intent = new Intent(ResultActivity.this, menuQuiz.class);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setMessage("해당하는 음식이 없습니다.");
                builder.show();
            }




        }
        catch (JSONException e){
            Log.i("lee","error2: " + e);
        }
    }


    public void setTv(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                data=getXmlData();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(data);

                        Log.d("lee", data);

                    }
                });
            }
        }).start();
    }

    String getXmlData(){
        StringBuffer buffer=new StringBuffer();
        String str= hidden.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);
        String query="%EC%A0%84%EB%A0%A5%EB%A1%9C";

        String queryUrl="http://apis.data.go.kr/1470000/FoodNtrIrdntInfoService/getFoodNtrItdntList?serviceKey="
                +key+"&desc_kor="
                +location
                +"&pageNo=1&numOfRows=1&bgn_year=&animal_plant=&";
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기
                        if(tag.equals("item")) ;// 첫번째 검색결과

                        else if(tag.equals("SERVING_WT")){
                            buffer.append(" - 1회제공량 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("g"+"\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("NUTR_CONT1")){
                            buffer.append(" - 열량 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            foodkcal.add(xpp.getText());
                            buffer.append("kcal"+"\n");//줄바꿈 문자 추가
                            kcal = xpp.getText();
                            Log.d("lee", "칼로리 : " + kcal + "kcal");

                        }
                        else if(tag.equals("NUTR_CONT2")){
                            buffer.append(" - 탄수화물 : ");
                            xpp.next();
                            foodtan.add(xpp.getText());
                            buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("g"+"\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("NUTR_CONT3")){
                            buffer.append(" - 단백질 : ");
                            xpp.next();
                            fooddan.add(xpp.getText());
                            buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("g"+"\n");//줄바꿈 문자 추가
                        }
                        else if(tag.equals("NUTR_CONT4")){
                            buffer.append(" - 지방 : ");
                            xpp.next();
                            foodji.add(xpp.getText());
                            buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("g"); //줄바꿈 문자 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....


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

    @Override
    public void onBackPressed() {
        AnswerSetGetter.setClear();

        finish();

    }
}