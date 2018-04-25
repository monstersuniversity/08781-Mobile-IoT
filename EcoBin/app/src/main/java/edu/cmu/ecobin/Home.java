package edu.cmu.ecobin;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import android.preference.PreferenceManager;

import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Onedollar on 2/22/18.
 */

public class Home extends Fragment {

    org.eazegraph.lib.charts.PieChart mPieChart;
    RoundCornerProgressBar progress;
    String TAG = "Home";
    public static final String USERID = "userId";
    SharedPreferences userIdPref;
    TextView view;
    User user = User.getInstance();;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");
        Log.v(TAG, user.getUserID());

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        view = (TextView)rootView.findViewById(R.id.textView8);
        view.setText(user.getUserID());
        mPieChart = rootView.findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("Recycle Percentage", 32, Color.parseColor("#FE6DA8")));
        mPieChart.addPieSlice(new PieModel("Trash Percentage", 68, Color.parseColor("#56B7F1")));
        mPieChart.startAnimation();

        progress = (RoundCornerProgressBar) rootView.findViewById(R.id.progress);
        progress.setProgressColor(Color.parseColor("#00FF7F"));
        progress.setProgressBackgroundColor(Color.parseColor("#8FBC8F"));
        progress.setMax(70);
        progress.setProgress(50);


        return rootView;
    }
}
