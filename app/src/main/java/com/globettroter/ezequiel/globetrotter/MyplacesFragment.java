package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//import android.app.Activity;

public class MyplacesFragment extends Fragment {

    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myplaces, container, false);
        list =(ListView) view.findViewById(R.id.myplaces_id);
        Mensaje activity = (Mensaje) getActivity();
        String id = activity.getMyData();
//        String id = getArguments().getString("id");
        if (id != null){
        Log.i("ID FACEBOOK", id);
        new get_places(list,id,getActivity().getApplicationContext()).execute();}
        return view;
    }
}

class get_places extends AsyncTask<String, String, JSONObject> {

    private ListView list;
    private ListViewAdapter_myplaces adapter;
    private String id;
    private Context context;

    public get_places(ListView list, String id, Context context){
        this.id=id;
        this.list=list;
        this.context=context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            HttpRequest con = new HttpRequest("http://globetrotterdomain.co.nf/my_places.php");
            Log.i("ID FACEBOOK", id);
            HashMap<String, String> data=new HashMap<>();
            data.put("id_facebook", id);
            JSONObject result =  con.preparePost().withData(data).sendAndReadJSON();

            return result;
    }catch (Exception e){
        e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        if (result != null){

            JSONObject obj;
            try {
                JSONArray jarray = result.getJSONArray("values");

                adapter = new ListViewAdapter_myplaces(context, R.layout.listview_myplaces);
                list.setAdapter(adapter);

                for (int i=0; i < jarray.length(); i++){

                    obj = jarray.getJSONObject(i);

                    DataContainer_MyPlaces datos = new DataContainer_MyPlaces(obj.optString("name_place"),
                                obj.optString("id_google_place"),
                                obj.optString("geonameid"),
                                obj.optString("id_region"),
                                obj.optString("name_country"),
                                obj.optString("name_city"),
                                obj.optString("date"),
                                obj.optString("points"));
                    adapter.add(datos);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }







        }

    }


}