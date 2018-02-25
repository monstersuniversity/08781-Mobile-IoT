package edu.cmu.ecobin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

/**
 * Created by Onedollar on 2/22/18.
 */

public class Home extends Fragment {

    org.eazegraph.lib.charts.PieChart mPieChart;

//    private static String TAG = "MainActivity";
//
//    private float[] yData = {20f, 80f};
//    private String[] xData = {"Trash", "Recycle"};
//    PieChart pieChart;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

    }

//    private void addDataSet() {
//        Log.d(TAG, "addDataSet started");
//        ArrayList<PieEntry> yEntrys = new ArrayList<>();
//        ArrayList<String> xEntrys = new ArrayList<>();
//
//        for(int i = 0; i < yData.length; i++){
//            yEntrys.add(new PieEntry(yData[i] , i));
//        }
//
//        for(int i = 1; i < xData.length; i++){
//            xEntrys.add(xData[i]);
//        }
//
//        //create the data set
//        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Trash and Recycle");
//        pieDataSet.setSliceSpace(2);
//        pieDataSet.setValueTextSize(12);
//
//        //add colors to dataset
//        ArrayList<Integer> colors = new ArrayList<>();
//        colors.add(Color.argb(100, 138,43,226));
//        colors.add(Color.argb(100, 230,230,250));
//
//        pieDataSet.setColors(colors);
//
//        //add legend to chart
//        Legend legend = pieChart.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
//
//        //create pie data object
//        PieData pieData = new PieData(pieDataSet);
//        pieChart.setData(pieData);
//        pieChart.invalidate();
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);


        mPieChart = rootView.findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Recycle Percentage", 32, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Trash Percentage", 68, Color.parseColor("#56B7F1")));
        mPieChart.startAnimation();

//        Log.d(TAG, "onCreate: starting to create chart");
//
//        pieChart = (PieChart)rootView.findViewById(R.id.idPieChart);
//
//        Description des = new Description();
//        des.setText("Your Recycle rate");
//        pieChart.setDescription(des);
//        //pieChart.setDescription("Sales by employee (In Thousands $)");
//        pieChart.setRotationEnabled(true);
//        //pieChart.setUsePercentValues(true);
//        //pieChart.setHoleColor(Color.BLUE);
//        //pieChart.setCenterTextColor(Color.BLACK);
//        pieChart.setHoleRadius(25f);
//        pieChart.setTransparentCircleAlpha(0);
//        pieChart.setCenterText("Recycle Rate");
//        pieChart.setCenterTextSize(10);
//        //pieChart.setDrawEntryLabels(true);
//        //pieChart.setEntryLabelTextSize(20);
//        //More options just check out the documentation!
//
//        addDataSet();

        return rootView;
    }
}
