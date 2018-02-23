package edu.cmu.ecobin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

/**
 * Created by Onedollar on 2/22/18.
 */

public class History extends Fragment {
    BarChart mBarChart1;
    BarChart mBarChart2;
    PieChart mPieChart;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("History");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        mBarChart1 = view.findViewById(R.id.barchart);

        mBarChart1.addBar(new BarModel(2.3f, 0xFF123456));
        mBarChart1.addBar(new BarModel(2.f,  0xFF343456));
        mBarChart1.addBar(new BarModel(3.3f, 0xFF563456));
        mBarChart1.addBar(new BarModel(1.1f, 0xFF873F56));
        mBarChart1.addBar(new BarModel(2.7f, 0xFF56B7F1));
        mBarChart1.addBar(new BarModel(2.f,  0xFF343456));
        mBarChart1.addBar(new BarModel(0.4f, 0xFF1FF4AC));
        mBarChart1.addBar(new BarModel(4.f,  0xFF1BA4E6));

        mBarChart1.startAnimation();


//        mBarChart2 = view.findViewById(R.id.barchart2);
//
//        mBarChart2.addBar(new BarModel(2.3f, 0xFF123456));
//        mBarChart2.addBar(new BarModel(2.f,  0xFF343456));
//        mBarChart2.addBar(new BarModel(3.3f, 0xFF563456));
//        mBarChart2.addBar(new BarModel(1.1f, 0xFF873F56));
//        mBarChart2.addBar(new BarModel(2.7f, 0xFF56B7F1));
//        mBarChart2.addBar(new BarModel(2.f,  0xFF343456));
//        mBarChart2.addBar(new BarModel(0.4f, 0xFF1FF4AC));
//        mBarChart2.addBar(new BarModel(4.f,  0xFF1BA4E6));
//
//        mBarChart2.startAnimation();

        mPieChart = view.findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("Work", 35, Color.parseColor("#CDA67F")));
        mPieChart.addPieSlice(new PieModel("Eating", 9, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();



        return view;
    }
}

