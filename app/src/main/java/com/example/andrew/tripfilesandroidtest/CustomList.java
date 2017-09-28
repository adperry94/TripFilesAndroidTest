package com.example.andrew.tripfilesandroidtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 9/27/2017.
 */
//custom list adapter to allow images to be used
public class CustomList extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    //creates constructor similar to simpleadapter
    public CustomList(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    //gets data from hashmap and adds it into the list
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.image_layout, null);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        TextView text = (TextView)vi.findViewById(R.id.labelDetail);
        String name = (String) data.get("date");
        text.setText(name);
        TextView days = (TextView)vi.findViewById(R.id.day);
        String day = (String) data.get("day");
        days.setText(day.toUpperCase());
        ImageView image=(ImageView)vi.findViewById(R.id.imgDetail);
        String image_url = (String) data.get("imageTemplate");
        Picasso.with(mContext).load(image_url).into(image);
        TextView comment = (TextView)vi.findViewById(R.id.comments);
        String comments = (String) data.get("comments");
        comment.setText(comments);
        TextView like = (TextView)vi.findViewById(R.id.likes);
        String likes = (String) data.get("likes");
        like.setText(likes);
        return vi;
    }
}
