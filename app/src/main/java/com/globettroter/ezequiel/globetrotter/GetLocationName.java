package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Ezequiel on 19/03/2016.
 */
public class GetLocationName extends AsyncTask<String, String, List<Geoname>> {

    private Context context;
    private String user_points;
    private TextView score;
    private String id;
    private String latitude;
    private String longitude;

    private String message_poi;


    public GetLocationName(Context context, String user_points, TextView score, String id, String latitude, String longitude, String message_poi) {
        this.context = context;
        this.user_points = user_points;
        this.score = score;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message_poi = message_poi;
    }

    //GETTING GEONAMES XML
    @Override
    protected List<Geoname> doInBackground(String... params) {
        String url = "http://api.geonames.org/extendedFindNearby?lat=35.693674&lng=139.694992&username=ezequiel91";
        try {
            List<Geoname> lista = parsearXmlDeUrl(url);
            return lista;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onPostExecute(List<Geoname> geonames) {

        String cont = "";
        String country_code = "";
        String country_name = "";
        String geoname_id = "";
        String city_name = "";
        String new_lat = "";
        String new_lng = "";


        if (geonames!=null){

            for(int i=0;i< geonames.size();i++){

                switch (geonames.get(i).getFcode()){
                    case "CONT":
                        cont = geonames.get(i).getName();
                        break;
                    case "PCLI":
                        country_code = geonames.get(i).getCountryCode();
                        country_name = geonames.get(i).getName();
                        break;
                    case "PPLC":
                    case "PPLA":
                    case "PPL":
                        geoname_id = geonames.get(i).getGeonameId();
                        city_name = geonames.get(i).getName();
                        new_lat = geonames.get(i).getLat();
                        new_lng = geonames.get(i).getLng();
                        break;
                }

            }
            //GET POPULATION

            //CALL SERVER
            new GetPopulation(context, user_points,score,id,new_lat, new_lng, cont, country_name,
                    country_code, city_name, geoname_id, message_poi).execute();

        }else{

            Log.e("GETLOCATIONNAME", "GET LOCATION NAME ERROR");

        }






    }



    private List<Geoname> parsearXmlDeUrl(String urlString)
            throws XmlPullParserException, IOException {
        InputStream stream = null;
        ParserXml parserXml = new ParserXml();
        List<Geoname> entries = null;

        try {
            stream = descargarContenido(urlString);
            entries = parserXml.parsear(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return entries;
    }

    private InputStream descargarContenido(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Iniciar la petición
        conn.connect();
        return conn.getInputStream();
    }



}


class GetPopulation extends AsyncTask<String,String,String>{

    private static final String API_KEY = "ezequiel91";

    private Context context;
    String user_points;
    TextView score_tx;
    private String id;
    private String latitude;
    private String longitude;
    private String continent_name;
    private String country_name;
    private String country_code;
    private String city_name;
    private String geonameId;

    private String message_poi;

    //CONSTRUCTOR
    public GetPopulation(Context context, String user_points, TextView score_tx, String id, String latitude, String longitude, String continent_name,
                       String country_name, String country_code, String city_name, String geonameId, String message_poi ){
        this.context=context;
        this.user_points=user_points;
        this.score_tx=score_tx;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.continent_name = continent_name;
        this.country_name = country_name;
        this.country_code = country_code;
        this.city_name = city_name;
        this.geonameId = geonameId;
        this.message_poi = message_poi;
    }



    @Override
    protected String doInBackground(String... params) {

        String url = "http://api.geonames.org/findNearbyPlaceNameJSON?lat="+latitude+"&lng="+longitude+"&username="+API_KEY;
        try {
            HttpRequest con = new HttpRequest(url);

            JSONObject result = con.prepare().sendAndReadJSON();

            int population  = result.getJSONArray("geonames").getJSONObject(0).getInt("population");
            String population_string = String.valueOf(population);
            return population_string;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String population) {

        if (population != null){
        //CALL SERVER
        new EnviarDatos(context, user_points,score_tx,id, continent_name, country_name,
                country_code, city_name, geonameId, population, latitude, longitude, message_poi).execute();}

    }
}