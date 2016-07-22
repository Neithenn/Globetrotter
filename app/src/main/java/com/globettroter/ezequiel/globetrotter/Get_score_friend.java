package com.globettroter.ezequiel.globetrotter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ezequiel on 01/03/2016.
 */
public class Get_score_friend extends AsyncTask<String, String, String[]> {


    private String id, name;
    private ListViewAdapter adapter;


    public Get_score_friend( String id_facebook, String name,ListViewAdapter adapter){

        this.id = id_facebook;
        this.name = name;
        this.adapter=adapter;
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

                array = new String[]{obj.optString("title"), obj.optString("score")};
                return array;
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] array) {
        super.onPostExecute(array);

        if (array != null) {
            FacebookDataConteiner facebook_obj = new FacebookDataConteiner(name, id,array[0], array[1]);
            adapter.add(facebook_obj);}

        }

    }

