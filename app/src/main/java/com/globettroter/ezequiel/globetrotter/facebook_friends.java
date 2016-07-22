package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Ezequiel on 29/01/2016.
 * RETRIEVE FACEBOOK FRIENDS INFORMATION - NAME - ID - POINTS - PROFILE PICTURE
 */
public class facebook_friends {

    ListViewAdapter adapter;
    Context context;
    ListView list_view;

    public facebook_friends(Context context, ListView list_view) {
        this.context = context;
        this.list_view = list_view;
    }

    public void friends_get_points(final AccessToken token){

        final String[] names = new String[20];
        final String[] ids = new String[20];
        final String[] urls = new String[20];

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */


                        if (response.getError() != null){

                         //ERROR
                            Log.i("TAG", "error facebook friends");
                        }else {

                            JSONArray jsonarray = null;
                            try {
                                jsonarray = response.getJSONObject().getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("response", response.toString());



                            for (int i = 0; i < jsonarray.length(); i++) {

                                try {
                                    names[i] = jsonarray.getJSONObject(i).optString("name");
                                    ids[i] = jsonarray.getJSONObject(i).optString("id");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            int i = 0;
                            // CREATE A NEW ADAPTER FOR A LIST VIEW CALLING PROPER LAYOUT
                            adapter = new ListViewAdapter(context, R.layout.listviewlayout);
                            list_view.setAdapter(adapter);

                            for (String name : names) {

                                if  (name != "" && name != null){

                                new Get_score_friend(ids[i],name, adapter).execute();
                                }
                                i++;

                            }

                        }
                    }
                }
        ).executeAsync();


    }



}



