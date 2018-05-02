package edu.cmu.ecobin;

/**
 * Created by danqihuang on 2/22/18.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> itemname;
    private final List<Bitmap> imgid;
    private final String[] rank;
    private final List<Float> numbers;

    public CustomListAdapter(Activity context, List<String> itemname, List<Bitmap> imgid, List<Float> numbers) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.numbers = numbers;

        rank = new String[itemname.size()];
        for (int i = 0; i < rank.length; i++) {
            rank[i] = String.valueOf(i + 1);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        // ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        CircleImageView imageView = (CircleImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        TextView number = (TextView) rowView.findViewById(R.id.textView2);

        txtTitle.setText(itemname.get(position));
//        imageView.setImageResource(imgid[position]);
        imageView.setImageBitmap(imgid.get(position));
        extratxt.setText(rank[position]);
        number.setText(String.valueOf(numbers.get(position)) + "%");
        return rowView;

    };
}


//
//package edu.cmu.ecobin;
//
///**
// * Created by danqihuang on 2/22/18.
// */
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class CustomListAdapter extends ArrayAdapter<String> {
//
//    private final Activity context;
//    private final String[] itemname;
//    private final Integer[] imgid;
//    private final String[] rank;
//    private final int[] numbers;
//
//    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
//        super(context, R.layout.mylist, itemname);
//        // TODO Auto-generated constructor stub
//
//        this.context=context;
//        this.itemname=itemname;
//        this.imgid=imgid;
//
//        rank = new String[itemname.length];
//        for (int i = 0; i < rank.length; i++) {
//            rank[i] = String.valueOf(i + 1);
//        }
//
//        numbers = new int[itemname.length];
//        for (int i = 0; i < numbers.length; i++) {
//            numbers[i] = 40 - 2 * i;
//        }
//    }
//
//    public View getView(int position, View view, ViewGroup parent) {
//        LayoutInflater inflater=context.getLayoutInflater();
//        View rowView=inflater.inflate(R.layout.mylist, null,true);
//
//        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
//        // ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//        CircleImageView imageView = (CircleImageView) rowView.findViewById(R.id.icon);
//        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
//
//        TextView number = (TextView) rowView.findViewById(R.id.textView2);
//
//        txtTitle.setText(itemname[position]);
////        imageView.setImageResource(imgid[position]);
//        imageView.setImageResource(imgid[position]);
//        extratxt.setText(rank[position]);
//        number.setText(String.valueOf(numbers[position]) + "%");
//        return rowView;
//
//    };
//}