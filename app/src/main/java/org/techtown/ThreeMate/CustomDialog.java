package org.techtown.ThreeMate;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CustomDialog extends Dialog{
    private Context context;
    private String date ;
    private String gender = " " ;
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
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date = year+"/"+month+"/"+dayOfMonth;
            updateLabel();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        //다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



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

        bornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // TODO Auto-generated method stub

                        try {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            date = year+"/"+monthOfYear+"/"+dayOfMonth;
                            updateLabel();
                        } catch (Exception e) {

                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));


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
                    dismiss();
                }

            }
        });

    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {



        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);
        dlg.setCancelable(false);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();






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
}