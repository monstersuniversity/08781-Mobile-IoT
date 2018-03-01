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

/**
 * Created by Onedollar on 2/22/18.
 */

public class Score extends Fragment {

    ListView list;
    ImageView user_image;
    TextView user_name, user_rank, user_number;


    String[] itemname ={
            "Joey",
            "Chandler",
            "Rachel",
            "Monica",
            "Me",
            "Ross",
            "Tracy",
            "Andrew"
    };

    Integer[] imgid={
            R.drawable.joey,
            R.drawable.chandler,
            R.drawable.phoebe,
            R.drawable.monica,
            R.drawable.rachel,
            R.drawable.ross,
            R.drawable.andrew,
            R.drawable.large_eagle_black,
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Scoreboard");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.score, container, false);


        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.score);

        CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid);


        user_image = (ImageView)rootView.findViewById(R.id.user_image);
        user_image.setImageResource(imgid[4]);
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
