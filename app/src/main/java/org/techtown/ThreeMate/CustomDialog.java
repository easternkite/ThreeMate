package org.techtown.ThreeMate;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CustomDialog extends Dialog{
    private Context context;
    private String date ;
    private String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
    private Calendar myCalendar = Calendar.getInstance();
    private EditText bornDate;
    private RadioButton genderMale;
    private RadioButton genderFemale;
    private Button button;
    private RadioGroup radioGroup;
    private EditText bodyLength;
    private EditText bodyWeight;
    private SQLiteManager sqLiteManager;

    /**
     * Fire Base 등장
     */
    private static final String TAG = "MainActivity";
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Uri filePath;
    private StorageReference storageRef;
    private String stringUri;
    private String userName="Master";
    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private FirebaseUser user;
    private String userEmail;
    private String userProfile;
    private String userUID;;
    private String gender = " " ;

    private String userUID1;
    private String userProfile1;
    private String userName1;
    private String userEmail1;
    private String gender1;
    private String bodyLength1;
    private String bodyWeight1;
    private String bornDate1;




    private DatePickerDialog datePickerDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);



        //다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sqLiteManager = new SQLiteManager(getContext(), "ThreeMate2.db", null, 1);
        /**
         * FireBase 인증 객체 초기화
         */
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화.
        user = auth.getCurrentUser();
        userUID = user.getUid();
        userProfile = user.getPhotoUrl().toString();
        userName = user.getDisplayName();
        userEmail = user.getEmail();
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(userUID); // DB 테이블 연결



        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        bornDate = (EditText) findViewById(R.id.bornDate);
        bornDate.setFocusableInTouchMode(true);
        bornDate.setFocusable(false);
        bornDate.setClickable(true);


        genderMale = (RadioButton) findViewById(R.id.gender_male);
        genderFemale = (RadioButton) findViewById(R.id.gender_female);
        button = (Button) findViewById(R.id.btn_input);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        bodyLength = findViewById(R.id.bodyLength);
        bodyWeight = findViewById(R.id.bodyWeight);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(userUID); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    if (user.getUserName() != null){
                        userUID1 = user.getUserUID();
                        userName1 = user.getUserName();
                        userProfile1 = user.getUserProfile();;
                        userEmail1 = user.getUserEmail();
                        bornDate1 = user.getBornDate();
                        gender1 = user.getGender();
                        bodyLength1= user.getBodyLength();
                        bodyWeight1 = user.getBodyWeight();

                        bornDate.setText(bornDate1);
                        if (gender1.equals("남")){
                            genderMale.setChecked(true);
                        }else {
                            genderFemale.setChecked(true);
                        }
                        bodyLength.setText(bodyLength1);
                        bodyWeight.setText(bodyWeight1);
                        button.setText("수정");

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });




        bornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bornDate.getText().toString().equals("") || !bornDate.getText().toString().equals(null)){
                    String getYear = bornDate.getText().toString().substring(0,4);
                    String getMonth = bornDate.getText().toString().substring(5,7);
                    String getDay = bornDate.getText().toString().substring(8,10);
                    datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            // TODO Auto-generated method stub

                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            date = year+"/"+monthOfYear+"/"+dayOfMonth;
                            updateLabel();
                        }
                    }, Integer.parseInt( getYear),Integer.parseInt(getMonth)-1, Integer.parseInt(getDay));

                }else {
                    datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            // TODO Auto-generated method stub

                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            date = year+"/"+monthOfYear+"/"+dayOfMonth;
                            updateLabel();
                           
                        }
                    }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                }


                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                datePickerDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bodyLength.getText().toString().equals(null) ||bodyLength.getText().toString().equals("") ||
                        bodyWeight.getText().toString().equals(null) || bodyWeight.getText().toString().equals("") ||
                        bornDate.getText().toString().equals(null) || bornDate.getText().toString().equals("") ||
                        gender.equals(null) || gender.equals(" ")
                ){
                    Toast.makeText(context,"정보를 모두 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    sqLiteManager.deleteAll();
                    sqLiteManager.insertUser(userName, userProfile,
                            bornDate.getText().toString(),
                            gender,
                            bodyLength.getText().toString(),
                            bodyWeight.getText().toString());
                    /**
                     * insert data to FireBase Realtime DB
                     */
                    writeNewUser(userUID,
                            userName,
                            userProfile,
                            userEmail,
                            gender,
                            bornDate.getText().toString(),bodyLength.getText().toString(),
                            bodyWeight.getText().toString());



                    Intent intent = new Intent(context, MainActivity.class);
                    ((Activity)context).startActivity(intent);
                    ((Activity)context).overridePendingTransition(0, 0); //액티비티 전환 모션 제거
                    ((Activity)context).finish();

                    dismiss();
                }

            }
        });

    }

    private void updateLabel() {


        date = sdf.format(myCalendar.getTime());
        bornDate.setText(sdf.format(myCalendar.getTime()));

    }
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup,  int i) {
            if(i == R.id.gender_male){
                gender = "남";
            }
            else if(i == R.id.gender_female){
                gender = "여";
            }
        }
    };

    public CustomDialog(Context mContext) {
        super(mContext);
        this.context = mContext;
    }

    public void writeNewUser(String userUID, String userName , String userProfile, String userEmail,String bornDate,String gender, String bodyLength, String bodyWeight) {
        User user = new User(userUID, userName, userProfile, userEmail, gender, bornDate, bodyLength, bodyWeight);

        databaseReference.child("UserInfo").setValue(user);
        sqLiteManager.insertUser(userName,userProfile,bornDate,gender,bodyLength,bodyWeight);
    }
}