package org.techtown.ThreeMate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Diary extends AppCompatActivity {
    final AnimationSet set = new AnimationSet(true);
    private TextView resultText;
    private Button btn_upload;// 업로드버튼
    private EditText edit_name, edit_kcal, edit_carbs, edit_protein, edit_fat;       // 입력받을 폼 3개(음식이름, 음식칼로리, 날짜)
                      // SQLite Class 관리용 객체
    private EditText textView;
    private String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
    private ListView listView;// DB에 저장된 내용을 보여주기위한 리스트뷰
    int figures;
    private ArrayAdapter<String> adapter;
    public SQLiteManager sqLiteManager;
    private TextView calories_remaining_number;
    private Button main_btn;
    private Button btn_upload2;
    private Button DateUp;
    private Button DateDown;
    private double parsekcal = 0.0;
    private ArrayList<String> foodkcal = new ArrayList<String>();
    private ArrayList<String> idIndicator2 = new ArrayList<String>();
    private ArrayList<String> matchfood = new ArrayList<String>();
    private ArrayList<String> matchdate = new ArrayList<String>();
    private String idIndicator ="";
    private Context context;
    private int sum=0;
    private SwipeRefreshLayout refreshLayout = null;
    private String date;
    private int down;
    private int up;

    RecyclerView recyclerView;
    FoodAdapter adapter2;

    SearchActivity MA = (SearchActivity) SearchActivity.activity;
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date = year+"/"+month+"/"+dayOfMonth;
            updateLabel();
            updateList();
            LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
            recyclerView.setLayoutAnimation(controller);

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);




        Intent secondIntent = getIntent();
        String name = secondIntent.getStringExtra("name");
        String kcal = secondIntent.getStringExtra("kcal");
        String carbs = secondIntent.getStringExtra("carbs");
        String protein = secondIntent.getStringExtra("protein");
        String fat = secondIntent.getStringExtra("fat");
        int num = secondIntent.getIntExtra("num", 0);
        final int ACode = secondIntent.getIntExtra("ACode", 0);
        final String[] url = {secondIntent.getStringExtra("url")};
        if(num == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(Diary.this);
            builder.setCancelable(false);
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    edit_name.setText(null);
                    edit_kcal.setText(null);
                    edit_carbs.setText(null);
                    edit_protein.setText(null);
                    edit_fat.setText(null);
                    url[0] = null;
                }
            });
            builder.setTitle("다이어리 기입");
            builder.setMessage("[" + name + "]" + " 음식을 저장하시겠습니까?");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sqLiteManager.insert(
                            edit_name.getText().toString(),
                            edit_kcal.getText().toString(),
                            edit_carbs.getText().toString(),
                            edit_protein.getText().toString(),
                            edit_fat.getText().toString(),
                            textView.getText().toString(),
                            url[0]);

                    // 리스트뷰를 갱신한다. (DB의 내용이 달라지니까)
                    updateList();

                    Toast.makeText(getApplicationContext(),"끄적끄적!",Toast.LENGTH_LONG).show();
                    edit_name.setText(null);
                    edit_kcal.setText(null);
                    edit_carbs.setText(null);
                    edit_protein.setText(null);
                    edit_fat.setText(null);
                    url[0] = null;
                    LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                    recyclerView.setLayoutAnimation(controller);
                }
            });
            builder.show();
        }


        // 각 컴포넌트 제어를 위한 아이디할당 (EditText, Button)
        btn_upload = findViewById(R.id.btn_upload);
        btn_upload2 = findViewById(R.id.btn_upload2);
        main_btn = findViewById(R.id.main_button); //'메인버튼으로' 버튼
        DateUp = findViewById(R.id.DateUp);
        DateDown = findViewById(R.id.DateDown);
        edit_name = findViewById(R.id.edit_name);
        edit_kcal = findViewById(R.id.edit_kcal);
        edit_carbs = findViewById(R.id.edit_carbs);
        edit_protein = findViewById(R.id.edit_protein);
        edit_fat = findViewById(R.id.edit_fat);
        textView = (EditText) findViewById(R.id.date);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Diary.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        updateLabel();


        recyclerView = findViewById(R.id.recyclerView);

        calories_remaining_number = findViewById(R.id.calories_remaining_number);
        main_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                final Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        main_btn.setBackground(getDrawable(R.drawable.mainbtn_pressed));
                        main_btn.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        main_btn.setBackground(getDrawable(R.drawable.mainbtn));
                        main_btn.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });
        main_btn.setOnClickListener(new View.OnClickListener() {
            //메인메뉴로 돌아가기
            @Override
            public void onClick(View view) {
                if (MA != null){
                    MA.finish();
                    finish();
                }else{
                    finish();
                }



            }
        });

        DateDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dayDown=sdf.format(myCalendar.getTime()).replace("-","");
               int dayDownint =Integer.parseInt(dayDown);
               dayDownint = dayDownint -1;
               dayDown = String.valueOf(dayDownint);

               SimpleDateFormat sdfmt = new SimpleDateFormat("yyyyMMdd");
                try {
                    Date date = sdfmt.parse(dayDown);
                    dayDown = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
                    myCalendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                textView.setText(dayDown);
                updateList();
                LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                recyclerView.setLayoutAnimation(controller);
            }
        });

        DateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dayUp=sdf.format(myCalendar.getTime()).replace("-","");
                int dayUpint =Integer.parseInt(dayUp);
                dayUpint = dayUpint +1;
                dayUp = String.valueOf(dayUpint);

                SimpleDateFormat sdfmt = new SimpleDateFormat("yyyyMMdd");
                try {
                    Date date = sdfmt.parse(dayUp);
                    dayUp = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
                    myCalendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                textView.setText(dayUp);
                updateList();
                LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                recyclerView.setLayoutAnimation(controller);
            }
        });
        DateUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                final Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DateUp.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        DateUp.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });
        DateDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                final Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DateDown.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        DateDown.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        sqLiteManager = new SQLiteManager(getApplicationContext(), "NewDiary2.db", null, 1);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter2 = new FoodAdapter();
        adapter2.setOnItemClickListener(new OnFoodItemClickListener() {
            @Override
            public void onItemClick(FoodAdapter.ViewHolder holder, View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Diary.this);
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        sqLiteManager.delete(idIndicator2.get(position));
                        Toast.makeText(getApplicationContext(),"["+matchdate.get(position)+"]"+matchfood.get(position)+" 쓱싹쓱싹!",Toast.LENGTH_LONG).show();
                        updateList();
                        LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                        recyclerView.setLayoutAnimation(controller);



                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton("아니오",null);
                builder.setTitle("데이터 삭제");
                builder.setMessage("["+matchdate.get(position)+"]"+matchfood.get(position)+ " 데이터를 삭제하시겠습니까?");
                builder.show();



            }
        });


        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,-1,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,-1,
                Animation.RELATIVE_TO_SELF,0         );
        rtl.setDuration(500);
        set.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(700);
        set.addAnimation(alpha);

        final LayoutAnimationController[] controller = {new LayoutAnimationController(set, 0.17f)};
        recyclerView.setLayoutAnimation(controller[0]);


        /**
         * 리스트뷰 설정
         */
        // 각 컴포넌트 제어를 위한 아이디할당 (ListVeiw)
        listView = (ListView)findViewById(R.id.listview);

        //데이터를 저장하게 되는 리스트
        List<String> list = new ArrayList<>();

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);


        /**
         * SQLite 제어 설정
         */
        // SQLite 객체 초기화









        edit_name.setText(name);
        edit_kcal.setText(kcal);
        edit_carbs.setText(carbs);
        edit_protein.setText(protein);
        edit_fat.setText(fat);


        resultText = (TextView) findViewById(R.id.calories_remaining_number);
        updateList();
        // 버튼을 눌렀을때 해야할 이벤트 작성
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (edit_kcal.getText().toString().equals("")||edit_name.getText().toString().equals("")||edit_carbs.getText().toString().equals("")||edit_protein.getText().toString().equals("")||edit_fat.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"음식 정보를 입력하십시오.",Toast.LENGTH_LONG).show();
            }else{
                // EditText에 입력한 정보를 DB에 Insert.
                sqLiteManager.insert(
                        edit_name.getText().toString(),
                        edit_kcal.getText().toString(),
                        edit_carbs.getText().toString(),
                        edit_protein.getText().toString(),
                        edit_fat.getText().toString(),
                        textView.getText().toString(),
                        url[0]);

                // 리스트뷰를 갱신한다. (DB의 내용이 달라지니까)
                updateList();

                Toast.makeText(getApplicationContext(),"끄적끄적!",Toast.LENGTH_LONG).show();
                edit_name.setText(null);
                edit_kcal.setText(null);
                edit_carbs.setText(null);
                edit_protein.setText(null);
                edit_fat.setText(null);
                url[0] = null;
                LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                recyclerView.setLayoutAnimation(controller);
            }

            }
        });
        btn_upload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                final Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_upload.setBackground(getDrawable(R.drawable.plus_pressed));
                        btn_upload.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_upload.setBackground(getDrawable(R.drawable.plus));
                        btn_upload.startAnimation(anim2);
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.btn_upload2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Diary.this);
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        sqLiteManager.clear(date);
                        updateList();



                        LayoutAnimationController controller= new LayoutAnimationController(set, 0.17f);
                        recyclerView.setLayoutAnimation(controller);
                        Toast.makeText(getApplicationContext(),"쓱싹쓱싹!",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton("아니오",null);
                builder.setTitle("데이터 삭제");
                builder.setMessage("데이터를 전부 삭제하시겠습니까?");
                builder.show();
            }
        });
        findViewById(R.id.btn_upload2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim2);
                anim.setFillAfter(true);
                final Animation anim2 = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.scale_anim);
                anim2.setFillAfter(true);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        findViewById(R.id.btn_upload2).setBackground(getDrawable(R.drawable.minus_pressed));
                        findViewById(R.id.btn_upload2).startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        findViewById(R.id.btn_upload2).setBackground(getDrawable(R.drawable.minus));
                        findViewById(R.id.btn_upload2).startAnimation(anim2);
                        break;
                }
                return false;
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    Intent intent = new Intent(Diary.this, Diary.class);
                    finish();
                    startActivity(intent);
            }
        });
    }

    private void updateList(){
        adapter2.removeItem();
        matchdate.clear();
        matchfood.clear();
        idIndicator2.clear();
        sum=0;
        resultText.setText(Double.toString(0)+ " kcal");
        ArrayList<JSONObject> array = sqLiteManager.getResult(textView.getText().toString()); // DB의 내용을 배열단위로 모두 가져온다
            try{



                int length =  array.size(); // 배열의 길이
                for(int idx = 0; idx < length; idx++){  // 배열의 길이만큼 반복

                    JSONObject object = array.get(idx);                // json의 idx번째 object를 가져와서,
                    String id = object.getString("id");         // object 내용중 id를 가져와 저장.
                    String name = object.getString("name");     // object 내용중 name를 가져와 저장.
                    String kcal = object.getString("kcal");     // object 내용중 kcal를 가져와 저장.
                    String carbs = object.getString("carbs");     // object 내용중 carbs를 가져와 저장.
                    String protein = object.getString("protein");     // object 내용중 protein를 가져와 저장.
                    String fat = object.getString("fat");     // object 내용중 fat를 가져와 저장.
                    String date = object.getString("date");     // object 내용중 date를 가져와 저장.
                    String Url = object.getString("url");     // object 내용중 date를 가져와 저장.
                    // 저장한 내용을 토대로 ListView에 다시 그린다.

                    adapter2.addItem(new FD(Url, "음식명 : " + name  , "열량 : " + kcal + "kcal","탄수화물 : " + carbs+ "g", "단백질 : " + protein + "g","지방 : " +  fat+ "g"));
                    recyclerView.setAdapter(adapter2);


                    foodkcal.add(kcal);
                    idIndicator2.add(id);
                    matchfood.add(name);
                    matchdate.add(date);


                idIndicator = id;
                sum += Double.parseDouble(kcal);
                    resultText.setText(Double.toString(sum)+ " kcal");

                }
                Log.d("Lee", String.valueOf(sum + "kcal"));

            }
            catch (Exception e){
                Log.i("seo","error : " + e);

            }

            adapter2.notifyDataSetChanged();
        }
    @Override
    public void onBackPressed() {
            finish();
        }
    private void updateLabel() {


        date = sdf.format(myCalendar.getTime());
        textView.setText(sdf.format(myCalendar.getTime()));

    }

    }

