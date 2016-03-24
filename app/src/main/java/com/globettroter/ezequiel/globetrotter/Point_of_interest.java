package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by Ezequiel on 19/01/2016.
 */
public class Point_of_interest extends AsyncTask<String, String, JSONObject> {


    private Context context;
    private String id_facebook;
    private String user_points;
    private String latitude;
    private String longitude;
    private TextView score_tx;
    private static final String API_KEY = "AIzaSyBJXHrI4ECKaPELHLufRjTiDw1NVUIS93A";


    public Point_of_interest(Context context,String user_points, TextView score_tx, String id, String latitude, String longitude){
        this.context=context;
        this.score_tx=score_tx;
        this.id_facebook=id;
        this.user_points=user_points;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    @Override
    protected JSONObject doInBackground(String... params) {


        try{

            HttpRequest con = new HttpRequest("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-34.5091290,-58.4802880&radius=100&key="+API_KEY);

            JSONObject json = con.prepare().sendAndReadJSON();
            return json;

        }catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }

    protected void onPostExecute(JSONObject result){

        if (result == null){

            Log.i("erro", "json null");

        }else{

            try {
                JSONArray arr = result.getJSONArray("results");
                int points = 0;
                String name_poi = "";
                String place_id = "";
                float rating = 0;
                for (int i=0; i < arr.length(); i++ ){

                    Log.i("type", arr.getJSONObject(i).getString("types"));

                    if (arr.getJSONObject(i).has("rating") && check_types(arr.getJSONObject(i))){

                        if (rating < arr.getJSONObject(i).getDouble("rating")){

                            switch (String.valueOf(arr.getJSONObject(i).getDouble("rating"))){
                                case "4.3": points = 50;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "4.4": points = 100;

                                    name_poi = arr.getJSONObject(i).getString("name");
                                    Log.i("point of interest", arr.getJSONObject(i).getString("type"));
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "4.5": points = 150;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "4.6": points = 200;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "4.7": points = 250;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "4.8": points = 300;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "4.9": points = 350;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "5.0": points = 400;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                            }
                        }

                    }
                }

                if (points > 0){
                    //envio puntos y nombre del lugar? como hago el registro del lugar? como verifico que ya estuvo ahi?
                    System.out.println(points);
                    System.out.println(name_poi);
                    String string_points = String.valueOf(points);

                    String aux = score_tx.getText().toString();
                    int old_score = Integer.parseInt(aux);
                    int new_score = old_score + points;
                    score_tx.setText(String.valueOf(new_score));

                    new Send_points(context, user_points, score_tx, id_facebook, latitude,longitude).execute(place_id,name_poi,string_points);

                }else{
                    /*Intent intent = new Intent(context, Popup.class);
                    intent.putExtra("msg", "Not very interesting to me...");
                    context.startActivity(intent);*/
                    new GetLocationName(context,user_points, score_tx,id_facebook, latitude, longitude,null).execute();

                    // Toast.makeText(context, "Not very interesting place to me...",Toast.LENGTH_SHORT).show();
                    //System.out.println("Sin puntos, nada nuevo descubierto");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

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

} //end class


class Send_points extends AsyncTask<String, String, String>{

    private Context context;
    private String id_facebook;
    private String user_points;
    private String latitude;
    private String longitude;
    private TextView score_tx;

    public Send_points(Context context,String user_points, TextView score_tx, String id, String latitude, String longitude){
        this.context=context;
        this.score_tx=score_tx;
        this.id_facebook=id;
        this.user_points=user_points;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            HttpRequest con2 = new HttpRequest("http://globetrotterdomain.co.nf/sendpoi.php");
            HashMap<String, String> params2 = new HashMap<>();
            params2.put("id_facebook", id_facebook);
            params2.put("place_id", params[0]);
            params2.put("name", params[1]);
            params2.put("points", params[2]);

            try {
                String resultRest =  con2.preparePost().withData(params2).sendAndReadString();
                return resultRest;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    protected void onPostExecute (String result){

        if (!"".equals(result)){

            new GetLocationName(context,user_points, score_tx,id_facebook, latitude, longitude,result).execute();

        }else if (result.contains("already been here")){

            Intent intent = new Intent(context, Popup.class);
             intent.putExtra("msg", result);
             context.startActivity(intent);

        }else{

            new GetLocationName(context,user_points, score_tx,id_facebook, latitude, longitude,null).execute();

        }



    }
}