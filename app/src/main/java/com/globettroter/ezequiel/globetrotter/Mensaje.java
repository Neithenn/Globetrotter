package com.globettroter.ezequiel.globetrotter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//HOME CLASS
public class Mensaje extends AppCompatActivity {

    private Button button;
    private TextView textview;
    private TextView score;
    private TextView title;
    private TextView facebookname;
    private ImageView profileimageview;
    private ImageView picture_score;
    private LocationManager locationManager;
    private LocationListener locationListener;
    ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        Resources res = getResources();
        //SETTING TABS - HOME FRIENDS AND MARKET
        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("hometab");
        spec.setContent(R.id.hometab);
        spec.setIndicator("Home",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("placestab");
        spec.setContent(R.id.placetab);
        spec.setIndicator("Friends",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("markettab");
        spec.setContent(R.id.markettab);
        spec.setIndicator("Market",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        button = (Button) findViewById(R.id.locacion);

        final String id = getIntent().getStringExtra("id");
        final String email = getIntent().getStringExtra("email");
        final String name = getIntent().getStringExtra("name");
        String users_score = getIntent().getStringExtra("score");
        String users_title = getIntent().getStringExtra("title");
        //Profile picture url user
        String profilePicUrl = getIntent().getStringExtra("profilePicUrl");

        facebookname = (TextView) findViewById(R.id.textView2);
        facebookname.setText(name);
        score = (TextView) findViewById(R.id.score_txt_result);
        score.setText("Score: "+users_score);
        title = (TextView) findViewById(R.id.title_user);
        title.setText(users_title);

        picture_score = (ImageView) findViewById(R.id.picture_score);
        //picture_score.setImageResource(R.mipmap.ic_beginner);

        int resId = getResources().getIdentifier("ic_beginner", "mipmap", getPackageName());
        picture_score.setImageResource(resId);

        // profile picture ImageView
        profileimageview = (ImageView) findViewById(R.id.imageView2);
        new Profile_picture(profileimageview).execute(profilePicUrl);

        //DATA FACEBOOK FRIENDS WITH THE APP
        list_view = (ListView) findViewById(R.id.list_view);
        new facebook_friends(this, list_view).friends_get_points(AccessToken.getCurrentAccessToken());

        //GET THE LOCACION IF IT HAS CHANGED
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                ProgressDialog pDialog = new ProgressDialog(Mensaje.this);
                pDialog.setMessage("Getting your location, hold on!");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                Double lat = location.getLatitude();
                Double lon = location.getLongitude();

                String latitude = String.valueOf(lat);
                String longitude = String.valueOf(lon);

                pDialog.dismiss();
                locationManager.removeUpdates(this);

                //POINT OF INTEREST - CHECK IF YOU ARE NEAR TO A POINT OF INTEREST
                new Point_of_interest(Mensaje.this).execute(id);
                //Country - Continent - city // CHECK IF YOU ARE IN A NEW COUNTRY OR CONTINENT OR CITY
                new EnviarDatos(Mensaje.this).execute(id,email, name,latitude, longitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
              requestPermissions(new String[]{
                      Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
              }, 10);
            return;
            }else{
                configureButton();
            }
        };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;

        }
    }

    //BUTTON SET ON CLICK LISTENER
    private void configureButton(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

            }
        });

            }
    }
    // SEND LOCATION TO THE SERVER. RETRIEVES JSON WITH POINTS EARNED AND A MESSAGE
    class EnviarDatos extends AsyncTask<String, String, JSONObject>{
        private Context context;

        public EnviarDatos(Context context){
            this.context=context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

           return  enviarPost(params[0], params[1],params[2],params[3],params[4]);
        }

        protected void onPostExecute(JSONObject result){
            //receive msg
            //show up on the screen

            if (result==null){
                Log.e("ERROR", "Error al traer el objeto json");
            }else{

                try {
                    Intent intent = new Intent(context, Popup.class);
                    if (!"".equals(result.getString("message3"))){

                        intent = new Intent(context, Popup.class);
                        intent.putExtra("msg", result.getString("message3"));
                        context.startActivity(intent);

                    }
                    if (!"".equals(result.getString("message3"))){

                        intent = new Intent(context, Popup.class);
                        intent.putExtra("msg", result.getString("message2"));
                        context.startActivity(intent);

                    }

                    if (!"".equals(result.getString("message1"))){
                    intent.putExtra("msg", result.getString("message1"));
                    context.startActivity(intent);}


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



        public JSONObject enviarPost(String id, String email, String name, String latitude, String longitude) {
            try {
                HttpRequest con = new HttpRequest("http://192.168.0.114:80/android/Globetrotter/getpoints.php");

                HashMap<String, String> params=new HashMap<>();
                params.put("id", id);
                params.put("email", email);
                params.put("latitude", latitude);
                params.put("longitude", longitude);

             JSONObject object = con.preparePost().withData(params).sendAndReadJSON();
                return object;


            }catch (Exception e) {
                e.printStackTrace();
                return null;

            }


        }

    }


