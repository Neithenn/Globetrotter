package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

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
        TextView title_friend;

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
            handler.title_friend = (TextView)row.findViewById(R.id.user_title_tx);
            row.setTag(handler);

        }else{
            handler = (DataHandler)row.getTag();
        }

        FacebookDataConteiner dataConteiner;
        dataConteiner = (FacebookDataConteiner) this.getItem(position);
        handler.name.setText(dataConteiner.getName());
        handler.points.setText(dataConteiner.getPoints());
        handler.title_friend.setText(dataConteiner.getTitle());

        get_fc_profile_pic_friends(AccessToken.getCurrentAccessToken(), dataConteiner.getId(), handler.facebook_picture);

        //IMAGE FACEBBOK HANDLER
        //new Profile_picture(handler.facebook_picture).execute(get_fc_profile_pic_friends(AccessToken.getCurrentAccessToken(), dataConteiner.getId()));




        return row;
    }

    public void get_fc_profile_pic_friends (AccessToken token, String id_facebook, final ImageView facebook_pic){


        String params = "/"+id_facebook+"/picture?type=normal&";
        Bundle param = new Bundle();
        param.putBoolean("redirect", false);

        new GraphRequest(token,
                params,
                param,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        try {
                            String url2 = response.getJSONObject().getJSONObject("data").getString("url");
                            new Profile_picture(facebook_pic).execute(url2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).executeAsync();

    }}