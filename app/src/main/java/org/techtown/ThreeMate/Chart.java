package org.techtown.ThreeMate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class Chart extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        int i = 0;
        while (i <= 100){
            entries.add(new Entry(i, i+i*i));
            i++;
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "섭취칼로리");

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(15);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setFillAlpha(50);
        lineDataSet.setFillColor(0xffFFD399);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setHighlightLineWidth(1.0f);
        lineDataSet.setDrawCircleHole(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
//        lineChart.setDescription(description);
        lineChart.animateXY(1000, 1000);
//        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        MyMarkerView marker = new MyMarkerView(this,R.layout.activity_main);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

    }
}
