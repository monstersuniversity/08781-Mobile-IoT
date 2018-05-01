package edu.cmu.ecobin;


import android.app.Activity;
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

/**
 * Created by Onedollar on 2/22/18.
 */

public class Score extends Fragment {

    ListView list;
    ImageView user_image;
    TextView user_name, user_rank, user_number;
    User user = User.getInstance();

    String[] itemname ={
            "Joey",
    };

    Integer[] imgid={
            R.drawable.joey,
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Scoreboard");
        Log.v("Score", user.getFacebookID());
        String fid = "/" + user.getFacebookID() + "/friends";

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
                            Log.v("", friendFid);

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
        View rootView = inflater.inflate(R.layout.score, container, false);


        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.score);

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }


}
