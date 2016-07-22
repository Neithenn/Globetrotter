package com.globettroter.ezequiel.globetrotter;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ezequiel on 07/03/2016.
 */
public class background_check_points extends AsyncTask <String, String, JSONObject> {

    private String lat_string;
    private String long_string;
    private Context context;
    private String API_KEY = "AIzaSyBJXHrI4ECKaPELHLufRjTiDw1NVUIS93A";

    public background_check_points(String lat_string, String long_string, Context context) {
        this.lat_string = lat_string;
        this.long_string = long_string;
        this.context = context;
    }


    @Override
    protected JSONObject doInBackground(String[] params) {

        try{
            Log.i("Background", "Checking point of interest"+lat_string+","+long_string);
            HttpRequest con = new HttpRequest("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat_string+","+long_string+"&radius=100&key="+API_KEY);

            JSONObject json = con.prepare().sendAndReadJSON();
            Log.i("json result", json.toString());
            return json;

        }catch (Exception e){
            e.printStackTrace();

            return null;
        }

    }

    @Override
    protected void onPostExecute(JSONObject o) {

        if (o == null){

            Log.i("RESULT", "RESULT IS NULL");
        }else{

        try {
            JSONArray arr = o.getJSONArray("results");
            double rating_low = 4.3;
            for (int i=0; i < arr.length(); i++ ){

                Log.i("type", arr.getJSONObject(i).getString("types"));

                if (arr.getJSONObject(i).has("rating") && check_types(arr.getJSONObject(i))) {

                    if (rating_low <= arr.getJSONObject(i).getDouble("rating")) {

                        String name_poi = arr.getJSONObject(i).getString("name");

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        builder
                                .setSmallIcon(R.mipmap.ic_adventure)
                                .setContentTitle("Globetrotter");
                        builder.setContentText(name_poi + "? Get your points now!");

                        Intent notificationIntent = new Intent(context, MainActivity.class);

                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        Notification notification = builder.build();
                        NotificationManagerCompat.from(context).notify(0, notification);

                        //POINTS
                    }
                else{
                        //CHECK NEW CITY?
                        Log.i("POSTEXECUTE", "NO POINTS");
                    }

                }}
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }}



    public boolean check_types(JSONObject obj){

        String[] types_values = {"food", "restaurant", "bank", "cafe", "gym"};
        int i = 0;
        boolean check = true;
        try {

            String string_types = obj.getString("types");

            while(check==true){
                if (string_types.contains(types_values[i])){
                    check = false;
                    Log.i("point of interest", "return false containts "+types_values[i]);
                }
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return check;
    }
}
