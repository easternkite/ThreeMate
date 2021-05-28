package org.techtown.ThreeMate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class Chart extends AppCompatActivity {
    private Drawable drawable;
    private Button main_btn;
    private Button diary_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        /*//메인버튼, 다이어리보기 버튼 추가
        main_btn = findViewById(R.id.main_button); //'메인버튼으로' 버튼
        diary_btn = findViewById(R.id.diary_button); //'다이어리보기' 버튼

        SearchActivity MA = (SearchActivity) SearchActivity.activity;

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
        });*/

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");
        xAxisLabel.add("Sat");
        xAxisLabel.add("Sun");

        List<Entry> entries = new ArrayList<>();
        int i = 0;
        while (i <= 100){
            entries.add(new Entry(i, i+i*i));
            i++;
        }
        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(0f, 30f));
        entries2.add(new Entry(1f, 80f));
        entries2.add(new Entry(2f, 60f));
        entries2.add(new Entry(3f, 50f));
        entries2.add(new Entry(5f, 70f));
        entries2.add(new Entry(6f, 60f));

        LineDataSet lineDataSet = new LineDataSet(entries, "섭취칼로리");
        LineDataSet lineDataSet2 = new LineDataSet(entries, "권장섭취량");

        //섭취칼로리
        //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(Color.YELLOW); //라인 색상 변경
        lineDataSet.setLineWidth(5);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10); //값 넣기
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setFillAlpha(50);
        lineDataSet.setFillColor(0xffFFD399);
        lineDataSet.setFillColor(Color.YELLOW);
        lineDataSet.setHighLightColor(Color.WHITE);
        lineDataSet.setHighlightLineWidth(1.0f);
        lineDataSet.setDrawCircleHole(false);

        //권장섭취량
        lineDataSet2.setColor(Color.BLUE); //ColorTemplate.getHoloBlue() //라인 색상 변경
        lineDataSet2.setLineWidth(5);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setDrawValues(true);
        lineDataSet2.setValueTextSize(5); //값 넣기
        lineDataSet2.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabel.get((int) value);

            }
        });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setLabelCount(5, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정, 5개 force가 true 이면 반드시 보여줌
        xAxis.setGranularityEnabled(true); //x 축에 값이 중복되어 출력되는 현상 제거

        YAxis yLAxis = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        /*Description description = new Description();
        description.setText("");*/

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(true);

//        lineChart.setDescription(description);
        lineChart.animateXY(1000, 1000);
//        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        MyMarkerView marker = new MyMarkerView(this,R.layout.activity_main);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

    }
}
