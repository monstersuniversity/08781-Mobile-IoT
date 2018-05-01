package edu.cmu.ecobin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Onedollar on 2/22/18.
 */

public class Home extends Fragment {

    org.eazegraph.lib.charts.PieChart mPieChart;
    String TAG = "Home";
    TextView view;
    TextView pieChartCaptionLabel;
    User user = User.getInstance();
    Double todayPercent;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        view = (TextView)rootView.findViewById(R.id.textView8);
        pieChartCaptionLabel = (TextView)rootView.findViewById(R.id.PieChartCaptionLabel);
        view.setText(user.getUserID() + "   " + user.getUserEmail() + " " + user.getUserName() + " " + user.getFacebookID());
        mPieChart = rootView.findViewById(R.id.piechart);


        String requestBody = fetchDataRequestBody();
        new fetchData(requestBody).execute();



        return rootView;
    }
    private class fetchData extends AsyncTask<String, String, JSONObject> {
        String body;

        fetchData(String body) {
            this.body = body;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            // Create URL
            try {
                URL myEndpoint = new URL("http://ec2-52-87-198-206.compute-1.amazonaws.com:3000/find_by_time");

                HttpURLConnection myConnection
                        = (HttpURLConnection) myEndpoint.openConnection();

                myConnection.setRequestMethod("POST");


                myConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setDoOutput(true);
                myConnection.setDoInput(true);

                byte[] outputInBytes = this.body.getBytes("UTF-8");
                OutputStream os = myConnection.getOutputStream();
                os.write(outputInBytes);
                os.close();


                if (myConnection.getResponseCode() == 200) {

                    // Read data from response.
                    StringBuilder builder = new StringBuilder();
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                    String line = responseReader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = responseReader.readLine();
                    }
                    String responseString = builder.toString();
                    Log.v(getClass().getName(), "Response String: " + responseString);
                    JSONObject jsonResponse = new JSONObject(responseString);
                    // Close connection and return response code.
                    myConnection.disconnect();

                    return jsonResponse;
                } else {
                    // Error handling code goes here
                    String a = "" + myConnection.getResponseCode();
                    Log.i("Failure- Status Code", a);
                    Log.i("Failure- StatusMessage", myConnection.getResponseMessage());

                }
                myConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject responseJson) {
            if (responseJson != null) {
                try {
                    Log.v("fetch data success", responseJson.getClass().getName());
                    Log.v("fetch data success", "get data from object");
                    Log.v("====================", responseJson.get("answer").toString());
                    Double answer = Double.parseDouble(responseJson.get("answer").toString());
                    Home.this.todayPercent = answer;
                    Log.v("====================", String.valueOf(todayPercent.intValue()));
                    mPieChart.addPieSlice(new PieModel("Recycle Percentage", todayPercent.intValue(), Color.parseColor("#FE6DA8")));
                    mPieChart.addPieSlice(new PieModel("Trash Percentage", 100 - todayPercent.intValue(), Color.parseColor("#56B7F1")));
                    mPieChart.startAnimation();
                    pieChartCaptionLabel.setText("Your recycle/trash ratio is " + String.valueOf(answer) + "%, see how your friends did in scoreboard.");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private String fetchDataRequestBody(){
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        // TODO: userid
        String body = "{"
                + "\"id\": \"" + user.getUserID() + "\""
                + ",\"time\": \"" + nowAsISO + "\""
                + "}";
        Log.v("fetch data body", body);
        return body;
    }
}
