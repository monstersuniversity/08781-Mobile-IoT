package edu.cmu.ecobin;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TimeZone;

/**
 * Created by Onedollar on 2/22/18.
 */

public class Score extends Fragment {

    ListView list;
    ImageView user_image;
    TextView user_name, user_rank, user_number;
    User user = User.getInstance();
    String friendFid;
    Map<String, Friend> map;
    Queue<Friend> queue;
    View rootView;
    CustomListAdapter adapter;
    String[] itemname ={
            "Joey",
    };

    Integer[] imgid={
            R.drawable.joey,
    };


    List<String> names;
    List<Bitmap> imgs;
    List<Float> numbers;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Scoreboard");
        Log.v("Score", user.getFacebookID());
        String fid = "/" + user.getFacebookID() + "/friends";
        map = new HashMap<>();
        queue = new PriorityQueue<>();
        numbers = new ArrayList<>();
        Friend me = new Friend(user.getFacebookID());
        map.put(user.getFacebookID(), me);
        me.setUserid(user.getUserID());
        me.setName(user.getUserName());
        me.setPercent(user.getPercent());
        me.setPic(user.getPic());
        Log.v("my infomation", map.get(user.getFacebookID()).toString());

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                fid,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject object = response.getJSONObject();
                            JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
                            String friendName = arrayOfUsersInFriendList.getJSONObject(0).get("name").toString();

                            String friendFid = arrayOfUsersInFriendList.getJSONObject(0).get("id").toString();

                            Friend f = new Friend(friendFid);
                            map.put(friendFid, f);
                            f.setName(friendName);
                            Score.this.friendFid = friendFid;
                            Log.v("", friendFid);

                            String requestBody = buildGetIdRequestBody();
                            new getFriendID(requestBody).execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("field", "friends");
        request.setParameters(parameters);
        request.executeAsync();



    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.score, container, false);


        super.onCreate(savedInstanceState);
        CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid);


        user_image = (ImageView)rootView.findViewById(R.id.user_image);
        user_image.setImageResource(imgid[0]);
        user_name = (TextView) rootView.findViewById(R.id.user_name);
        user_name.setText("Me");
        user_rank = (TextView) rootView.findViewById(R.id.user_rank);
        user_rank.setText("Rank: 5");
        user_number = (TextView) rootView.findViewById(R.id.user_number);
        user_number.setText("32%");

        list=(ListView)rootView.findViewById(R.id.list);

        list.setAdapter(adapter);


        return rootView;
    }

    private class getFriendID extends AsyncTask<String, String, JSONObject> {
        String body;

        getFriendID(String body) {
            this.body = body;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            // Create URL
            try {
                URL myEndpoint = new URL("http://ec2-52-87-198-206.compute-1.amazonaws.com:3000/getFriendId");

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
                    JSONObject responseJson = new JSONObject(responseString);
                    // Close connection and return response code.
                    myConnection.disconnect();

                    return responseJson;
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
            if(responseJson!= null && responseJson.has("status") ) {
                try {
                    String result = responseJson.getString("status");
                    Log.v("result in postexecute", result);
                    if (result.equals("success")){
                        Log.v("session API success", "result");
                        Log.v("Score Activity", "responseJson = " + responseJson.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(responseJson!= null && responseJson.has("userId")){
                try {
                    String uid = responseJson.get("userId").toString();
                    String fid = responseJson.get("facebookId").toString();
                    map.get(fid).setUserid(uid);
                    Log.v("get friend id", uid);
                    String reqBody = fetchDataRequestBody(uid, fid);
                    new fetchFriendData(reqBody).execute();

                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String buildGetIdRequestBody(){
        String body = "{"
                + "\"facebookid\" : \"" + this.friendFid + "\""
                + "}";
        Log.v("getidreq body", body);
        return body;
    }

    private String fetchDataRequestBody(String fid, String ffid){
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        String body = "{"
                + "\"id\": \"" + fid + "\""
                + ",\"time\": \"" + nowAsISO + "\""
                + ",\"fid\": \"" + ffid + "\""
                + "}";
        Log.v("fetch data body", body);
        return body;
    }



    private class fetchFriendData extends AsyncTask<String, String, JSONObject> {
        String body;

        fetchFriendData(String body) {
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
                try{

                    String friend_percent = responseJson.get("answer").toString();
                    String friend_fid = responseJson.get("fid").toString();

                    map.get(friend_fid).setPercent(Float.parseFloat(friend_percent));


                    try {
                        String urlStr = "https://graph.facebook.com/"+friend_fid+"/picture?width=150&height=150";
                        Log.v("profile pic link", urlStr);
                        URL img_value = new URL(urlStr);
                        Bitmap icon = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
                        map.get(friend_fid).setPic(icon);

                    } catch(IOException e) {
                        e.printStackTrace();
                    }

                    for (Friend friend : map.values()) {
                        queue.add(friend);
                        Log.v("iter", friend.toString());
                    }
                    int rank = setFriendsInList(queue);
                    Log.v("my rank is ", String.valueOf(rank));
                    

                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private int setFriendsInList(Queue<Friend> queue){
        int rank = -1;
        int counter = 0;
        if (queue.size() > 0) {
            names = new ArrayList<>();
            imgs = new ArrayList<>();
            while (!queue.isEmpty()) {
                Friend f = queue.poll();
                counter++;
                if (f.getFacebookid().equals(user.getFacebookID())) {
                    rank = counter;
                }
                names.add(f.getName());
                imgs.add(f.getPic());
                numbers.add(f.getPercent());
            }
        }
        return rank;
    }

}
