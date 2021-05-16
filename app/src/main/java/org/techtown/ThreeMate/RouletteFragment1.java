package org.techtown.ThreeMate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class RouletteFragment1  extends Fragment {
    private View view;


    List<LuckyItem> data = new ArrayList<>();
    private Button main_btn;
    private Button reset;
    private TextView info;
    private SwipeRefreshLayout refreshLayout = null;
    private int buttonclicked = 0;

    private ArrayList<String> matchFoods = new ArrayList<String>();
    private ArrayList<String> matchFoods2 = new ArrayList<String>();
    private ArrayList<String> imageurl = new ArrayList<String>();

    public static RouletteFragment1 newInstance(){
       RouletteFragment1 rouletteFragment1 = new RouletteFragment1();
       return  rouletteFragment1;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.roulette, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        main_btn = view.findViewById(R.id.main_button);
        info = view.findViewById(R.id.info);
        final Button play =view.findViewById(R.id.play);

        final LuckyWheelView luckyWheelView = (LuckyWheelView)view.findViewById(R.id.luckyWheel);

        try {
            JSONObject json = new JSONObject(getJsonString(getContext()));      // json 파일에 있는것을 Object형태로 변환
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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


        view.findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
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
                        play.setBackground(getActivity().getDrawable(R.drawable.start_btn_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        play.setBackground(getActivity().getDrawable(R.drawable.start_btn));
                        break;
                }
                return false;
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                Intent myIntent = new Intent(getActivity(), ResultActivity2.class);
                myIntent.putExtra("name", data.get(index).topText);
                myIntent.putExtra("name2", data.get(index).topText2);
                myIntent.putExtra("image", data.get(index).topText3);
                getActivity().finish();
                startActivity(myIntent);//액티비티 띄우기
                Toast.makeText(getActivity(), data.get(index).topText + " 당첨!", Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (buttonclicked ==0) {
                    Intent intent = new Intent(getActivity(), RouletteActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                }
                else{
                    refreshLayout.setEnabled(false);
                }
            }
        });

        //선택메뉴룰렛 화면 전환

            return view;

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




}

