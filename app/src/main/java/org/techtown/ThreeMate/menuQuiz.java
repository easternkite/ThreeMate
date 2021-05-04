package org.techtown.ThreeMate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.ThreeMate.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class menuQuiz extends AppCompatActivity {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private ImageView qustion_img;

    private ListView listview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> lst;
    private TextView txt_question;
    private int round = 0;
    private String[] question = {"질문1", "질문2", "질문3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_quiz);
        qustion_img = findViewById(R.id.question_img);
        listview = findViewById(R.id.listview);
        txt_question = findViewById(R.id.txt_question);

        listview.setAdapter(adapter) ;
        listview.setOnItemClickListener(new listClickListener());
        generateListView(round);
        //Glide.with(this).load("https://cdn.pixabay.com/photo/2016/12/21/08/58/questions-1922476_960_720.jpg").into(qustion_img);
    }
    private class listClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            String answer = adapterView.getItemAtPosition(i).toString();    // 1. 현재선택된 값을 가져옴
            Log.i("lee","answer : " + answer);
            AnswerSetGetter.setAnswers(answer);                             // 2. 선택된 값을 AnswerSetGetter에 저장
            adapter.clear();                                                // 3. 리스트뷰 항목 모두 없앰
            adapter.notifyDataSetChanged();                                 // 4. 리스트뷰 갱신 (이게 없으면 없애도 안사라짐)

            if(round < 3) { // 만약 현재라운드가 4 미만이면 (json에 "질문" 키값에 있는 질문이 총 네개니까)
                round ++;                                                       // 5. 라운드 올림
                generateListView(round);                                        // 6. 올린 라운드로 다시 리스트뷰 생성
            }
            else{   // 만약 4라운드가 지났으면, 결과창으로 이동
                Intent intent = new Intent(menuQuiz.this, ResultActivity.class);
                finish();
                startActivity(intent);
            }
        }
    }

    /**
     * generateQuestion() : json 파일로부터 "질문" key를 가져오는 함수.
     * @param round : 단계의 수를 나타냄.  ex) 1단계 질문
     * @return : n 단계의 질문 Object를 리턴. ex) {"question" : "어떤 음식이 좋으신가요?","answers" : ["한식","중식","일식","기타"]}
     */
    private Object generateQuestion(int round){
        try{
           /* for (int i =0;i<question.length;i++) {
                int j = (int) (Math.random() * question.length);
                String tmp = "";

                tmp = question[i];
                question[i] = question[j];
                question[j] = tmp;

            }

            */
            JSONObject json = new JSONObject(getJsonString(this));      // json 파일에 있는것을 Object형태로 변환
            JSONArray array = new JSONArray(json.getString("질문"));      // json 파일의 "질문"에 해당하는 object를 JsonArray 형태로 변환

            return array.get(round);                                             //
        }
        catch (JSONException e){
            Log.i("lee","error: " + e);
            return null;
        }
    }

    /**
     * generateListView() : "질문" key 값에서 뽑은 "question"과 "answers" 키값을 다시 뽑아서, ListView로 생성
     * @param round : 단계의 수를 나타냄.  ex) 1단계 질문
     * @param round
     */
    private void generateListView(int round){
        Object object = generateQuestion(round);
        try{
            JSONObject json = new JSONObject(object.toString());
            String question = json.getString("question");
            JSONArray answersJson = new JSONArray(json.getString("answers"));

            String[] mArray = answersJson.join(",").split(",");

            txt_question.setText(question);
            lst = new ArrayList<String>(Arrays.asList(mArray));
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,lst);
            listview.setAdapter(adapter) ;
        }catch (Exception e){
            Log.i("lee","error : " + e);

        }
    }

    /**
     * getJsonString(): json파일에 있는 내용을 String으로 리턴
     */
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
}