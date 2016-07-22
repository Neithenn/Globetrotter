package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ezequiel on 05/03/2016.
 */

// SEND LOCATION TO THE SERVER. RETRIEVES JSON WITH POINTS EARNED AND A MESSAGE
public class EnviarDatos extends AsyncTask<String, String, JSONObject> {
    private Context context;
    String user_points;
    TextView score_tx;
    private String id;
    private String continent_name;
    private String country_name;
    private String country_code;
    private String city_name;
    private String geonameId;
    private String population;
    private String latitude;
    private String longitude;

    private String message_poi;

    //CONSTRUCTOR
    public EnviarDatos(Context context, String user_points, TextView score_tx, String id, String continent_name,
                        String country_name, String country_code, String city_name, String geonameId, String population,
                       String latitude, String longitude, String message_poi ){
        this.context=context;
        this.user_points=user_points;
        this.score_tx=score_tx;
        this.id = id;
        this.continent_name = continent_name;
        this.country_name = country_name;
        this.country_code = country_code;
        this.city_name = city_name;
        this.geonameId = geonameId;
        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message_poi = message_poi;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        return  enviarPost(id);
    }

    protected void onPostExecute(JSONObject result){
        //receive msg
        //show up on the screen
        Intent intent = new Intent(context, Popup.class);
        if (result==null){
            Log.e("ERROR", "Error al traer el objeto json");

            intent = new Intent(context, Popup.class);
            intent.putExtra("msg", "Nothing new!");
            context.startActivity(intent);
        }else{

            try {
                if (message_poi == null || message_poi == ""){

                    if ("0".equals(result.getString("points"))){

                        intent = new Intent(context, Popup.class);
                        intent.putExtra("msg", "Nothing new!");
                        context.startActivity(intent);
                    }

                }else{
                    intent = new Intent(context, Popup.class);
                    intent.putExtra("msg", message_poi);
                    context.startActivity(intent);

                }


                if (!"".equals(result.getString("message3"))){

                    intent = new Intent(context, Popup.class);
                    intent.putExtra("msg", result.getString("message3"));
                    context.startActivity(intent);

                }
                if (!"".equals(result.getString("message2"))){

                    intent = new Intent(context, Popup.class);
                    intent.putExtra("msg", result.getString("message2"));
                    context.startActivity(intent);

                }

                if (!"".equals(result.getString("message1"))){
                    intent.putExtra("msg", result.getString("message1"));
                    context.startActivity(intent);}

                //POINTS
                if (!"0".equals(result.getString("points"))){
                    int aux;
                    int sum;
                    if (user_points != null){
                        aux = Integer.parseInt(user_points);
                        sum = Integer.parseInt(result.getString("points")) + aux;
                        user_points = String.valueOf(sum);
                        score_tx.setText(user_points);

                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public JSONObject enviarPost(String id) {
        try {
            HttpRequest con = new HttpRequest("http://globetrotterdomain.co.nf/getpoints.php");

            HashMap<String, String> params=new HashMap<>();
            params.put("id", id);
            //params.put("email", email);;
            params.put("city_name", city_name);
            params.put("latitude", latitude);
            params.put("longitude", longitude);
            params.put("country_code", country_code);
            params.put("geonameId", geonameId);
            params.put("country_name", country_name);
            params.put("continent", continent_name);
            params.put("population", population);

            JSONObject object = con.preparePost().withData(params).sendAndReadJSON();
            return object;


        }catch (Exception e) {
            e.printStackTrace();
            return null;

        }


    }

}
