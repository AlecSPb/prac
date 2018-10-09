package com.example.user.dprac.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.dprac.R;
import com.example.user.dprac.models.NavbarList;

import java.util.ArrayList;

public class NavbarListAdapter extends BaseAdapter {
    Activity mainActivitymain;
    ArrayList<NavbarList> arrayListmain;
    TextView title;
    ImageView imageView;
    LayoutInflater inflater;


    public NavbarListAdapter(Activity mainActivity, ArrayList<NavbarList> arrayList){
        mainActivitymain = mainActivity;
        arrayListmain=arrayList;
        inflater = (LayoutInflater)mainActivitymain.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayListmain.size();
    }

    @Override
    public NavbarList getItem(int position) {
        return arrayListmain.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.drawer_list_item,null);
        title = (TextView)convertView.findViewById(R.id.navbar_title);
        imageView = (ImageView) convertView.findViewById(R.id.navbar_image);
        NavbarList listModel =getItem(position);

        title.setText(listModel.getTitle());
        imageView.setImageResource(listModel.getImage());
        return convertView;


    }


}
