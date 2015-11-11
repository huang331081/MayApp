package com.haley.may.mayapp.View.Daily;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by lenovo on 2015/10/28.
 */
public class DailyStatisticsView extends LinearLayout {

    private final String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

    private DailyModel mModel = null;

    public DailyStatisticsView(Context context,DailyModel model){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_dailystatistics, this);

        this.mModel = model;
        this.mModel.initStatistics();

        this.initChartLine();

        this.initWeekChart();

        this.initMonthChart();
    }

    private void initChartLine(){
        LineChartView chartView = (LineChartView)this.findViewById(R.id.chartLine);

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < days.length; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartView.setLineChartData(lineData);

//        // For build-up animation you have to disable viewport recalculation.
//        chartView.setViewportCalculationEnabled(false);
//
//        // And set initial max viewport and current viewport- remember to set viewports after data.
//        Viewport v = new Viewport(0, 110, 6, 0);
//        chartView.setMaximumViewport(v);
//        chartView.setCurrentViewport(v);
//
//        chartView.setZoomType(ZoomType.HORIZONTAL);
    }

    private void initWeekChart(){
        ColumnChartView chartView = (ColumnChartView)this.findViewById(R.id.chartWeek);
        ColumnChartData columnChartData = new ColumnChartData();

        List<Column> columns = new ArrayList<Column>();
        for (int i=0; i<mModel.getDailyStatistics().getWeekCost().size() ;i++){
            List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(mModel.getDailyStatistics().getWeekCost().get(i).getValue(), ChartUtils.pickColor()));

            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }
        columnChartData.setColumns(columns);

        Axis axis = new Axis();axis.setName("Week");
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (float i=0.0f; i<mModel.getDailyStatistics().getWeekCost().size(); i++){
            AxisValue axisValue = new AxisValue(i);
            axisValue.setLabel(mModel.getDailyStatistics().getWeekCost().get((int)i).getWeekString());
            axisValues.add(axisValue);
        }
        axis.setValues(axisValues);

        columnChartData.setAxisXBottom(axis.setHasLines(true));
        columnChartData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));


        chartView.setColumnChartData(columnChartData);

        chartView.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                generateLineData(subcolumnValue.getColor(), mModel.getDailyStatistics().getWeekCost().get(i).getValues());
            }

            @Override
            public void onValueDeselected() {
                generateLineData(Color.BLUE, null);
            }
        });

        chartView.setValueSelectionEnabled(true);
    }

    private void initMonthChart(){
        ColumnChartView chartView = (ColumnChartView)this.findViewById(R.id.chartMonth);
        ColumnChartData columnChartData = new ColumnChartData();

        List<Column> columns = new ArrayList<Column>();
        for (int i=0; i<mModel.getDailyStatistics().getMonthCost().size() ;i++){
            List<SubcolumnValue> values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(mModel.getDailyStatistics().getMonthCost().get(i).getValue(), Color.BLUE));

            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);
        }
        columnChartData.setColumns(columns);

        Axis axis = new Axis();//axis.setName("Axis X");
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (float i=0.0f; i<mModel.getDailyStatistics().getMonthCost().size(); i++){
            AxisValue axisValue = new AxisValue(i);
            axisValue.setLabel(mModel.getDailyStatistics().getMonthCost().get((int)i).getMonthString());
            axisValues.add(axisValue);
        }
        axis.setValues(axisValues);

        columnChartData.setAxisXBottom(axis);

        chartView.setColumnChartData(columnChartData);
    }

    private void generateLineData(int color,List<Float> values) {
        LineChartView chartView = (LineChartView)this.findViewById(R.id.chartLine);

        // Cancel last animation if not finished.
        chartView.cancelDataAnimation();

        // Modify data targets
        Line line = ((LineChartData)chartView.getChartData()).getLines().get(0);// For this example there is always only one line.
        line.setColor(color);

        int i=0;
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            if (values != null)
                value.setTarget(value.getX(), values.get(i));
            else
                value.setTarget(value.getX(), 0.0f);
            i++;
        }

        // Start new data animation with 300ms duration;
        chartView.startDataAnimation(300);

    }
}
