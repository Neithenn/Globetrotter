package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ezequiel on 24/01/2016.
 * CALL TO THE LOCAL SERVER, SENDING THE USER FACEBOOK ID
 * RESPONSE: POINTS.
 */
public class Get_score_user extends AsyncTask <String, String, String[]>{

    private Context context;
    private String id, email, name, profilePicUrl;


    public Get_score_user(Context context, String id_facebook, String email, String name, String profilePicUrl){

        this.context = context;
        this.id = id_facebook;
        this.email = email;
        this.name = name;
        this.profilePicUrl = profilePicUrl;

    }


    @Override
    protected String[] doInBackground(String... params) {

            String[] array;

            try {
                HttpRequest con = new HttpRequest("http://globetrotterdomain.co.nf/get_data_user.php");
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("id_facebook", id);

                JSONObject obj = con.preparePost().withData(parameter).sendAndReadJSON();

                if (obj==null){

                    Log.e("error", "error al traer datos de usuario get score user");
                    return null;
                }else{

                    array = new String[]{obj.optString("score"), obj.optString("title"), obj.optString("file_name")};
                    return array;
                }


            }catch (Exception e){
                e.printStackTrace();
                return array = new String[] {"ERROR"};
            }
    }

    @Override
    protected void onPostExecute(String[] array) {
        super.onPostExecute(array);

        Log.i(" poINTS", "GETTING USER SCORE");

        if (array[0] != "ERROR"){

            Intent intent = new Intent(context, Mensaje.class);
            intent.putExtra("id", id);
            intent.putExtra("name",name);
            intent.putExtra("email", email);
            intent.putExtra("profilePicUrl", profilePicUrl);
            if (array != null){
                intent.putExtra("score", array[0]);
                intent.putExtra("title", array[1]);
                intent.putExtra("file_name", array[2]);

            }
            context.startActivity(intent);}
        else{
         Log.e("ERROR","CANT CONNECT TO LOCAL SERVER");
            Toast.makeText(context, "We couldn't connect to server", Toast.LENGTH_SHORT).show();
            }
        }

    }


