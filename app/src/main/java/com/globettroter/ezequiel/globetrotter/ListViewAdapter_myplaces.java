package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ezequiel on 29/02/2016.
 */
public class ListViewAdapter_myplaces extends ArrayAdapter {

    List list = new ArrayList();

    public ListViewAdapter_myplaces(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler{
        TextView place_name_tx;
        TextView date_tx;
        TextView points_tx;
        TextView achiev_title;

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
            row = inflater.inflate(R.layout.listview_myplaces,parent,false);
            handler = new DataHandler();
            handler.place_name_tx = (TextView)row.findViewById(R.id.place_name_tx);
            handler.date_tx = (TextView)row.findViewById(R.id.date_tx);
            handler.achiev_title = (TextView)row.findViewById(R.id.achiev_title);
            handler.points_tx = (TextView)row.findViewById(R.id.points_tx);
            row.setTag(handler);

        }else{
            handler = (DataHandler)row.getTag();
        }
        Typeface tf_verdana  = Typeface.createFromAsset(getContext().getAssets(), "fonts/Verdana.ttf");
        Typeface tf_cour = Typeface.createFromAsset(getContext().getAssets(), "fonts/cour.ttf");

        DataContainer_MyPlaces dataConteiner;
        dataConteiner = (DataContainer_MyPlaces) this.getItem(position);
        handler.place_name_tx.setTypeface(tf_verdana);

        if (dataConteiner.getId_google_place()!= "null"){

            handler.place_name_tx.setText(dataConteiner.getName_place());
            handler.achiev_title.setText(" ");
        }else if(dataConteiner.getGeonameid() != "null"){

            if(dataConteiner.getCountry_name()=="null"){
               handler.place_name_tx.setText(dataConteiner.getContinent());
                handler.achiev_title.setText("New continent!");
            } else if (dataConteiner.getCountry_name()!= "null" && dataConteiner.getCity_name()=="null"
                    ){

                handler.place_name_tx.setText(dataConteiner.getCountry_name());
                handler.achiev_title.setText("New country!");

            }else if (dataConteiner.getCity_name() != "null"){

                handler.place_name_tx.setText(dataConteiner.getCity_name());

            }


        }

        handler.date_tx.setTypeface(tf_verdana);
        handler.date_tx.setText(dataConteiner.getDate());
        handler.points_tx.setTypeface(tf_cour);
        handler.points_tx.setText(dataConteiner.getPoints());


        return row;
    }
}

