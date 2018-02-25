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
    BarChart mBarChart_month;
    BarChart mBarChart_year;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("History");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        mBarChart_month = view.findViewById(R.id.barchart_month);

        mBarChart_month.addBar(new BarModel("week 12", 43.3f, 0xFFEF25E8));
        mBarChart_month.addBar(new BarModel("week 13", 45.f,  0xFF9768E8));
        mBarChart_month.addBar(new BarModel("week 14", 24.3f, 0xFF8A9CF4));
        mBarChart_month.addBar(new BarModel("week 15", 34.1f, 0xFF719DEF));
        mBarChart_month.addBar(new BarModel("week 16", 25.7f, 0xFF82C0AC));
        mBarChart_month.addBar(new BarModel("week 17", 45.f,  0xFFB4DB84));
        mBarChart_month.addBar(new BarModel("week 18", 35.4f, 0xFFE5EF9F));
        mBarChart_month.addBar(new BarModel("week 19", 47.f,  0xFFFBF4A2));

        mBarChart_month.startAnimation();


        mBarChart_year = view.findViewById(R.id.barchart_year);

        mBarChart_year.addBar(new BarModel("Jan", 45.f,  0xFFFB9289));
        mBarChart_year.addBar(new BarModel("Feb", 35.f,  0xFFFAAA80));
        mBarChart_year.addBar(new BarModel("Mar", 46.3f, 0xFFF9C576));
        mBarChart_year.addBar(new BarModel("Apr", 64.1f, 0xFFFAE494));
        mBarChart_year.addBar(new BarModel("May", 34.7f, 0xFFFBF4A2));
        mBarChart_year.addBar(new BarModel("June", 45.f, 0xFFE5EF9F));
        mBarChart_year.addBar(new BarModel("Jul", 36.4f, 0xFFB4DB84));
        mBarChart_year.addBar(new BarModel("Aug", 57.f,  0xFF82C0AC));
        mBarChart_year.addBar(new BarModel("Sept", 57.f, 0xFF719DEF));
        mBarChart_year.addBar(new BarModel("Oct", 57.f,  0xFF8A9CF4));
        mBarChart_year.addBar(new BarModel("Nov", 57.f,  0xFF9768E8));
        mBarChart_year.addBar(new BarModel("Dec", 57.f,  0xFFEF25E8));


        mBarChart_year.startAnimation();



        return view;
    }
}

