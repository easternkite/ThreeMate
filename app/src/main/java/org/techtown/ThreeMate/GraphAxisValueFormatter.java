package org.techtown.ThreeMate;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class GraphAxisValueFormatter implements IAxisValueFormatter {
    private final String[] values;
    private String[] mValues;
    // 생성자 초기화
    GraphAxisValueFormatter(String[] values){
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis){
        return mValues[(int) value];
    }
}
