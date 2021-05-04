package org.techtown.ThreeMate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements Serializable {
    private long backKeyPressedTime = 0;
    private Toast toast;
    ListView listview = null ;
    private ImageView ivImage;
    private TextView Answer;
    private TextView hidden;
    private EditText editTextFilter;
    private ListViewAdapter adapter;
    private Button button;
    private TextView tv;
    private String key="RPT4s8Z6M%2Fx8mQW4CYG1A0l3SEU5EVg818RAioq2uvIy6B05Dn3fJvWjthwkztWzuq75u6Xuqh1rtk27ZM2n7w%3D%3D";
    private String data;
    private String data2;
    private String kcal;
    private URL Url;
    private String strUrl,strCookie,result;
    private Font font;
    private Button foodsave;
    private Button main;
    public static Activity activity;




    private ArrayList<String> foodji = new ArrayList<String>();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tv = findViewById(R.id.tv);
        hidden = findViewById(R.id.hidden);
        activity = SearchActivity.this;
        Intent intent = getIntent();
        ArrayList<String> matchFoods = (ArrayList<String>) intent.getSerializableExtra("matchFoods");
        ArrayList<String> matchFoods2 = (ArrayList<String>) intent.getSerializableExtra("matchFoods2");
        ArrayList<String> imageurl = (ArrayList<String>) intent.getSerializableExtra("imageurl");
        ArrayList<String> foodOneTime = (ArrayList<String>) intent.getSerializableExtra("foodOneTime");
        ArrayList<String> foodkcal = (ArrayList<String>) intent.getSerializableExtra("foodkcal");

        for (int i = 0 ; i <foodOneTime.size();i++){
            System.out.println("1회제공량["+i+"] :" + foodOneTime.get(i));
        }


        System.out.println("기타 : " + matchFoods.size());



        // 리스트뷰 참조 및 Adapter달기





        // Adapter 생성
        adapter = new ListViewAdapter() ;
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new listClickListener());
        AnimationSet set = new AnimationSet(true);

        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,1,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0         );
        rtl.setDuration(350);
        set.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(200);
        set.addAnimation(alpha);

        LayoutAnimationController controller= new LayoutAnimationController(set, 0.15f);
        listview.setLayoutAnimation(controller);


        if(matchFoods.size() > 0 ){

            for (int i = 0 ;i< matchFoods.size();i++){
                hidden.setText(matchFoods2.get(i));
                adapter.addItem(new ListViewItem(imageurl.get(i),matchFoods.get(i),matchFoods2.get(i))) ;

            }



        }



        editTextFilter = (EditText)findViewById(R.id.editTextFilter) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;

                ((ListViewAdapter)listview.getAdapter()).getFilter().filter(filterText) ;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;

    }
    private class listClickListener implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListViewItem listViewItem = (ListViewItem) adapter.getItem(position);
            Intent myIntent = new Intent(getApplicationContext(), ResultActivity3.class);
            myIntent.putExtra("name", listViewItem.getTitle());
            myIntent.putExtra("name2", listViewItem.getDesc());
            myIntent.putExtra("image", listViewItem.getIcon());
            startActivity(myIntent);//액티비티 띄우기
        }
    }





    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            editTextFilter.setText(null);
            editTextFilter.clearFocus();
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