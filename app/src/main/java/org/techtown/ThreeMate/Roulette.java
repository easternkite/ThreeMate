package org.techtown.ThreeMate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ThreeMate.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class Roulette extends AppCompatActivity {
    List<LuckyItem> data = new ArrayList<>();
    private Button main_btn;
    private Button reset;
    private TextView info;
    private SwipeRefreshLayout refreshLayout = null;
    private int buttonclicked = 0;

    private ArrayList<String> matchFoods = new ArrayList<String>();
    private ArrayList<String> matchFoods2 = new ArrayList<String>();
    private ArrayList<String> imageurl = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roulette);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        main_btn = findViewById(R.id.main_button);
        info = findViewById(R.id.info);
        final Button play = findViewById(R.id.play);



        final LuckyWheelView luckyWheelView = (LuckyWheelView) findViewById(R.id.luckyWheel);

       /* //룰렛에 랜덤으로 음식이름 넣기
        String foodname[] = {"초밥", "돈까스", "떡볶이", "곱창", "김치찌개",
                            "피자", "연어덮밥", "쌀국수", "치킨", "부대찌개",
                            "햄버거", "쫄면", "냉면", "파스타", "짜장면",
                            "알밥", "돼지불백", "보쌈", "닭갈비", "볶음밥",
                            "삼겹살", "라면", "마라탕",  "찜닭", "양꼬치"};
        //현재 25개. 수정&추가 가능


        int foodnumber[] = new int[6]; //룰렛 항목 (6개)
        Random r = new Random(); //랜덤객체 생성

        for(int i=0; i<6; i++) {
            foodnumber[i] = r.nextInt(foodname.length);
                    //배열 foodname 크기(0~24)의 랜덤 정수 중 6개를 뽑아서 foodnumber 배열에 넣기.

            for(int j=0; j<i; j++){
                //중복으로 뽑힌 숫자 제거하기.
                //같은숫자가 들어갔는지 확인하고 중복됐다면 되돌아가 다시 숫자를 뽑아서 넣음
                if(foodnumber[i] == foodnumber[j]){
                    i--;
                }
            }
        }

        */
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
        //룰렛 항목 생성

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = matchFoods.get(0);
        luckyItem1.topText2 = matchFoods2.get(0);
        luckyItem1.topText3 = imageurl.get(0);
        //luckyItem1.icon = R.drawable.test1;
        luckyItem1.color = 0xffFFBB61;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = matchFoods.get(1);
        luckyItem2.topText2 = matchFoods2.get(1);
        luckyItem2.topText3 = imageurl.get(1);
        //luckyItem2.icon = R.drawable.test2;
        luckyItem2.color = 0xffFFD399;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = matchFoods.get(2);
        luckyItem3.topText2 = matchFoods2.get(2);
        luckyItem3.topText3 = imageurl.get(2);
        //luckyItem3.icon = R.drawable.test3;
        luckyItem3.color = 0xffFFBB61;
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = matchFoods.get(3);
        luckyItem4.topText2 = matchFoods2.get(3);
        luckyItem4.topText3 = imageurl.get(3);
        //luckyItem4.icon = R.drawable.test4;
        luckyItem4.color = 0xffFFD399;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = matchFoods.get(4);
        luckyItem5.topText2 = matchFoods2.get(4);
        luckyItem5.topText3 = imageurl.get(4);
        //luckyItem5.icon = R.drawable.test5;
        luckyItem5.color = 0xffFFBB61;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = matchFoods.get(5);
        luckyItem6.topText2 = matchFoods2.get(5);
        luckyItem6.topText3 = imageurl.get(5);
        //luckyItem6.icon = R.drawable.test6;
        luckyItem6.color = 0xffFFD399;
        data.add(luckyItem6);


        luckyWheelView.setData(data);
        luckyWheelView.setRound(5);

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index = getRandomIndex();
                luckyWheelView.startLuckyWheelWithTargetIndex(index);
                info.setVisibility(View.INVISIBLE);
                buttonclicked = 1;

            }
        });

        play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        play.setBackground(getDrawable(R.drawable.start_btn_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        play.setBackground(getDrawable(R.drawable.start_btn));
                        break;
                }
                return false;
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                Intent myIntent = new Intent(getApplicationContext(), ResultActivity2.class);
                myIntent.putExtra("name", data.get(index).topText);
                myIntent.putExtra("name2", data.get(index).topText2);
                myIntent.putExtra("image", data.get(index).topText3);
                finish();
                startActivity(myIntent);//액티비티 띄우기
                Toast.makeText(getApplicationContext(), data.get(index).topText + " 당첨!", Toast.LENGTH_SHORT).show();
            }
        });

    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (buttonclicked ==0) {
                Intent intent = new Intent(Roulette.this, Roulette.class);
                finish();
                startActivity(intent);
            }
            else{
                refreshLayout.setEnabled(false);
            }
        }
    });
    }


    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1) + 0;
    }


    private static String getJsonString(Context context) {
        String json = "";
        try {
            InputStream is = context.getAssets().open("jsons/test.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        if (buttonclicked == 1);
        else{
            finish();

        }
    }


}

