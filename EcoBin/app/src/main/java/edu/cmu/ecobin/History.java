package edu.cmu.ecobin;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Onedollar on 2/22/18.
 */

public class History extends Fragment {
    BarChart mBarChart_month;
    BarChart mBarChart_year;
    User user = User.getInstance();
    volatile int counter;
    float[] array = new float[8];
//    float week_1 = 0;
//    float week_2 = 0;
//    float week_3 = 0;
//    float week_4 = 0;
//    float week_5 = 0;
//    float week_6 = 0;
//    float week_7 = 0;
//    float week_8 = 0;

//    float month_1 = 0;
//    float month_2 = 0;
//    float month_3 = 0;
//    float month_4 = 0;
//    float month_5 = 0;
//    float month_6 = 0;
//    float month_7 = 0;
//    float month_8 = 0;
//    float month_9 = 0;
//    float month_10 = 0;
//    float month_11 = 0;
//    float month_12 = 0;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("History");
        Log.v("History", user.getUserID());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        mBarChart_month = view.findViewById(R.id.barchart_month);


        String requestBody1 = fetchWeekRequestBody(7);
        new fetchWeekData(requestBody1).execute();

        String requestBody2 = fetchWeekRequestBody(14);
        new fetchWeekData(requestBody2).execute();

        String requestBody3 = fetchWeekRequestBody(21);
        new fetchWeekData(requestBody3).execute();

        String requestBody4 = fetchWeekRequestBody(28);
        new fetchWeekData(requestBody4).execute();

        String requestBody5 = fetchWeekRequestBody(35);
        new fetchWeekData(requestBody5).execute();

        String requestBody6 = fetchWeekRequestBody(42);
        new fetchWeekData(requestBody6).execute();

        String requestBody7 = fetchWeekRequestBody(49);
        new fetchWeekData(requestBody7).execute();

        String requestBody8 = fetchWeekRequestBody(56);
        new fetchWeekData(requestBody8).execute();




//        mBarChart_month.startAnimation();


        mBarChart_year = view.findViewById(R.id.barchart_year);


//        mBarChart_year.addBar(new BarModel("Feb", month_2,  0xFFFAAA80));
//        mBarChart_year.addBar(new BarModel("Mar", month_3, 0xFFF9C576));
//        mBarChart_year.addBar(new BarModel("Apr", month_4, 0xFFFAE494));
//        mBarChart_year.addBar(new BarModel("May", month_5, 0xFFFBF4A2));
//        mBarChart_year.addBar(new BarModel("June", month_6, 0xFFE5EF9F));
//        mBarChart_year.addBar(new BarModel("Jul", month_7, 0xFFB4DB84));
//        mBarChart_year.addBar(new BarModel("Aug", month_8,  0xFF82C0AC));
//        mBarChart_year.addBar(new BarModel("Sept", month_9, 0xFF719DEF));
//        mBarChart_year.addBar(new BarModel("Oct", month_10,  0xFF8A9CF4));
//        mBarChart_year.addBar(new BarModel("Nov", month_11,  0xFF9768E8));
//        mBarChart_year.addBar(new BarModel("Dec", month_12,  0xFFEF25E8));

        int year = Calendar.getInstance().get(Calendar.YEAR);
        String lastYear = String.valueOf(year - 1);
        String requestBodym1 = fetchMonthRequestBody(1,2,lastYear);
        new fetchMonthData(requestBodym1).execute();
        String requestBodym2 = fetchMonthRequestBody(2,3,lastYear);
        new fetchMonthData(requestBodym2).execute();
        String requestBodym3 = fetchMonthRequestBody(3,4,lastYear);
        new fetchMonthData(requestBodym3).execute();
        String requestBodym4 = fetchMonthRequestBody(4,5,lastYear);
        new fetchMonthData(requestBodym4).execute();
        String requestBodym5 = fetchMonthRequestBody(5,6,lastYear);
        new fetchMonthData(requestBodym5).execute();
        String requestBodym6 = fetchMonthRequestBody(6,7,lastYear);
        new fetchMonthData(requestBodym6).execute();
        String requestBodym7 = fetchMonthRequestBody(7,8,lastYear);
        new fetchMonthData(requestBodym7).execute();
        String requestBodym8 = fetchMonthRequestBody(8,9,lastYear);
        new fetchMonthData(requestBodym8).execute();
        String requestBodym9 = fetchMonthRequestBody(9,10,lastYear);
        new fetchMonthData(requestBodym9).execute();
        String requestBodym10 = fetchMonthRequestBody(10,11,lastYear);
        new fetchMonthData(requestBodym10).execute();
        String requestBodym11 = fetchMonthRequestBody(11,12,lastYear);
        new fetchMonthData(requestBodym11).execute();
        String requestBodym12 = fetchMonthRequestBody(12,13,lastYear);
        new fetchMonthData(requestBodym12).execute();






//        Log.v("History", fetchMonthRequestBody(1,2,"2017"));
//        Log.v("History", fetchWeekRequestBody(7));



        return view;
    }

    private class fetchWeekData extends AsyncTask<String, String, JSONObject> {
        String body;

        fetchWeekData(String body) {
            this.body = body;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            // Create URL
            try {
                URL myEndpoint = new URL("http://ec2-52-87-198-206.compute-1.amazonaws.com:3000/find_by_week");

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
                    float answer = Float.parseFloat(responseJson.get("answer").toString());
                    int number = Integer.parseInt(responseJson.get("number").toString());
                    Log.v("fetch data success", String.valueOf(answer));
                    Log.v("fetch data success", String.valueOf(number));
                    array[number/7 - 1] = answer;

                    mBarChart_month.addBar(new BarModel(String.valueOf(number/7), answer, 0xFFEF25E8));

                    mBarChart_month.startAnimation();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class fetchMonthData extends AsyncTask<String, String, JSONObject> {
        String body;

        fetchMonthData(String body) {
            this.body = body;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            // Create URL
            try {
                URL myEndpoint = new URL("http://ec2-52-87-198-206.compute-1.amazonaws.com:3000/find_by_month");

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
                    float answer = Float.parseFloat(responseJson.get("answer").toString());
                    int m = Integer.parseInt(responseJson.get("m").toString());
                    Log.v("fetch data success", String.valueOf(answer));


                    mBarChart_year.addBar(new BarModel(getMonth(m), answer,  0xFFFB9289));

                    mBarChart_year.startAnimation();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String fetchMonthRequestBody(int s, int e, String y){
        String body = "{"
                + "\"year\": \"" + y + "\""
                + ",\"smonth\":" + s
                + ",\"emonth\":" + e
                + ",\"id\": \"" + user.getUserID() + "\""
                + "}";
        Log.v("fetch data body", body);
        return body;
    }
    private String fetchWeekRequestBody(int days){
        String body = "{"
                + "\"id\": \"" + user.getUserID() + "\""
                + ",\"n\":"  + days
                + "}";
        Log.v("fetch data body", body);
        return body;
    }
    private String getMonth(int n){
        if (n == 1) {
            return "Jan";
        } else if (n == 2) {
            return "Feb";
        } else if (n == 3) {
            return "Mar";
        } else if (n == 4) {
            return "Apr";
        } else if (n == 5) {
            return "May";
        } else if (n == 6) {
            return "Jun";
        } else if (n == 7) {
            return "Jul";
        } else if (n == 8) {
            return "Aug";
        } else if (n == 9) {
            return "Sep";
        } else if (n == 10) {
            return "Oct";
        } else if (n == 11) {
            return "Nov";
        } else {
            return "Dec";
        }
    }
}

