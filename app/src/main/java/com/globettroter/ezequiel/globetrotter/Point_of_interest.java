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
    private TextView score_tx;


    public Point_of_interest(Context context, TextView score_tx){
        this.context=context;
        this.score_tx=score_tx;
    }
    @Override
    protected JSONObject doInBackground(String... params) {


        try{

            HttpRequest con = new HttpRequest("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.748076,-73.985957&radius=100&key=AIzaSyCMHc3JEDmypbLBcN2dJVoOSQYeK7E6TNU");

            JSONObject json = con.prepare().sendAndReadJSON();
            id_facebook = params[0];
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

                    if (arr.getJSONObject(i).has("rating")){

                        if (rating < arr.getJSONObject(i).getDouble("rating")){

                            switch (String.valueOf(arr.getJSONObject(i).getDouble("rating"))){
                                case "4.3": points = 50;
                                    name_poi = arr.getJSONObject(i).getString("name");
                                    rating = Float.valueOf(arr.getJSONObject(i).getString("rating"));
                                    place_id = arr.getJSONObject(i).getString("place_id");
                                    break;
                                case "4.4": points = 100;
                                    name_poi = arr.getJSONObject(i).getString("name");
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

                    new Send_points(context).execute(id_facebook,place_id,name_poi,string_points);

                }else{

                    System.out.println("Sin puntos");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


}


class Send_points extends AsyncTask<String, String, String>{

    private Context context;

    public Send_points(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            HttpRequest con2 = new HttpRequest("http://192.168.0.114:80/android/Globetrotter/sendpoi.php");
            HashMap<String, String> params2 = new HashMap<>();
            params2.put("id_facebook", params[0]);
            params2.put("place_id", params[1]);
            params2.put("name", params[2]);
            params2.put("points", params[3]);

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
            Intent intent = new Intent(context, Popup.class);
            intent.putExtra("msg", result);
            context.startActivity(intent);
        }



    }
}