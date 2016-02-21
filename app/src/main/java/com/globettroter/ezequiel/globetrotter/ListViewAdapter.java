package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ezequiel on 16/02/2016.
 */
public class ListViewAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ListViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler{
        ImageView facebook_picture;
        TextView name;
        TextView points;

    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
            row = convertView;
        DataHandler handler;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listviewlayout,parent,false);
            handler = new DataHandler();
            handler.facebook_picture = (ImageView)row.findViewById(R.id.facebook_pic);
            handler.name = (TextView)row.findViewById(R.id.name);
            handler.points = (TextView)row.findViewById(R.id.points);
            row.setTag(handler);

        }else{
            handler = (DataHandler)row.getTag();
        }

        FacebookDataConteiner dataConteiner;
        dataConteiner = (FacebookDataConteiner) this.getItem(position);
        handler.name.setText(dataConteiner.getName());
        handler.points.setText(dataConteiner.getId());
        //IMAGE FACEBBOK HANDLER
        new Profile_picture(handler.facebook_picture).execute(dataConteiner.getUrl_picture());




        return row;
    }
}
