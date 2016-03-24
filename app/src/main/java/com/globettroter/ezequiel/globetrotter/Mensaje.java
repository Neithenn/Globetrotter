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
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

//HOME CLASS
public class Mensaje extends FragmentActivity implements View.OnClickListener {

    private Button button;
    String id;
    private TextView score;
    private TextView title;
    private ImageView picture_score;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ProgressDialog pDialog;

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

        id = getIntent().getStringExtra("id");
        final String email = getIntent().getStringExtra("email");
        final String name = getIntent().getStringExtra("name");
        final String users_score = getIntent().getStringExtra("score");
        String img_name = getIntent().getStringExtra("file_name");
        String users_title = getIntent().getStringExtra("title");
        //Profile picture url user
        String profilePicUrl = getIntent().getStringExtra("profilePicUrl");

        TextView facebookname = (TextView) findViewById(R.id.textView2);
        facebookname.setText(name);
        score = (TextView) findViewById(R.id.score_txt_result);
        score.setText(users_score);
        title = (TextView) findViewById(R.id.title_user);
        title.setText(users_title);

        picture_score = (ImageView) findViewById(R.id.picture_score);

        if (img_name != null) {
            int resId = getResources().getIdentifier(img_name, "mipmap", getPackageName());
            picture_score.setImageResource(resId);
        }

        //Imagebuttons set on click listener
        ImageButton globetrotter_bt = (ImageButton) findViewById(R.id.globtrotter_icon);
        globetrotter_bt.setOnClickListener(this);
        ImageButton adveturer_bt = (ImageButton) findViewById(R.id.adventure);
        adveturer_bt.setOnClickListener(this);
        ImageButton wanderlust_bt = (ImageButton) findViewById(R.id.wanderlust_icon);
        wanderlust_bt.setOnClickListener(this);
        ImageButton traveler_bt = (ImageButton) findViewById(R.id.traveler_icon);
        traveler_bt.setOnClickListener(this);
        ImageButton backpacker_bt = (ImageButton) findViewById(R.id.backpacker_icon);
        backpacker_bt.setOnClickListener(this);
        ImageButton beginner_bt = (ImageButton) findViewById(R.id.beginner_icon);
        beginner_bt.setOnClickListener(this);

        // profile picture ImageView
        ImageView profileimageview = (ImageView) findViewById(R.id.imageView2);
        new Profile_picture(profileimageview).execute(profilePicUrl);

        //DATA FACEBOOK FRIENDS WITH THE APP
        list_view = (ListView) findViewById(R.id.list_view);
        new facebook_friends(this, list_view).friends_get_points(AccessToken.getCurrentAccessToken());


        //CHECK INTERNAL MEMORY IF THERE IS ANY LOCATION SAVED BEFORE
        new get_internal_memory(Mensaje.this, score).memory_access(id, users_score, email, name);


        //GET THE LOCACION IF IT HAS CHANGED
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Double lat = location.getLatitude();
                Double lon = location.getLongitude();

                String latitude = String.valueOf(lat);
                String longitude = String.valueOf(lon);

                locationManager.removeUpdates(this);
                pDialog.dismiss();

                //POINT OF INTEREST - CHECK IF YOU ARE NEAR TO A POINT OF INTEREST
                new Point_of_interest(Mensaje.this,users_score,score,id, latitude, longitude).execute();
                //Country - Continent - city // CHECK IF YOU ARE IN A NEW COUNTRY OR CONTINENT OR CITY
               // new GetLocationName(Mensaje.this, users_score, score,id,email,name, latitude, longitude).execute();

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

    }//end of onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;

        }
    }


    public String getMyData(){

        return id = getIntent().getStringExtra("id");
    }

    //BUTTON SET ON CLICK LISTENER
    private void configureButton(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(Mensaje.this);
                pDialog.setMessage("Getting your location!!");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

            }
        });

            }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ONSTOP", "START SERVICE");
        Intent intent2 = new Intent(this, LocationListener_Service.class);
        startService(intent2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,LocationListener_Service.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Intent intent2 = new Intent(this, LocationListener_Service.class);
        //startService(intent2);

    }

    @Override
    public void onClick(View v) {

        String value = title.getText().toString();
        int int_score = Integer.parseInt(score.getText().toString());

        switch (v.getId()){
            case R.id.globtrotter_icon:
                if (int_score >= 150000 && value != "THE GLOBETROTTER" ){
                    //Todo ok
                    Toast.makeText(this, "Congratz!! YOU ARE THE GLOBETROTTER!!", Toast.LENGTH_SHORT).show();
                    int resId = getResources().getIdentifier("ic_globetrotter", "mipmap", getPackageName());
                    picture_score.setImageResource(resId);
                    title.setText("THE GLOBETROTTER");

                }else if (value == "THE GLOBETROTTER"){
                    //have it
                    Toast.makeText(this, "Got it!", Toast.LENGTH_SHORT).show();

                }else{
                    //too low
                    Log.i("TAG", "Boton funciona! 2");
                    Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.adventure:
                if (int_score >= 100000 && int_score < 150000 && value != "ADVENTURER"){
                    //Todo ok
                    Toast.makeText(this, "Congratz!! Now you are an adventurer!!", Toast.LENGTH_SHORT).show();
                    int resId = getResources().getIdentifier("ic_adventure", "mipmap", getPackageName());
                    picture_score.setImageResource(resId);
                    title.setText("ADVENTURER");

                }else if(int_score > 150000){
                    //too high
                    Toast.makeText(this, "get the next title!!", Toast.LENGTH_SHORT).show();
                }else if(value == "ADVENTURER"){

                    //have it
                    Toast.makeText(this, "Got it!", Toast.LENGTH_SHORT).show();

                }else{
                    //too low
                    Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.wanderlust_icon:
                if (int_score >= 75000 && int_score < 100000 && value != "WANDERLUST" ){
                    //Todo ok
                    Toast.makeText(this, "Congratz!! Now you are the wanderlust!!", Toast.LENGTH_SHORT).show();
                    int resId = getResources().getIdentifier("ic_wanderlust", "mipmap", getPackageName());
                    picture_score.setImageResource(resId);
                    title.setText("WANDERLUST");

                }else if(int_score > 100000){
                    //too high
                    Toast.makeText(this, "get the next title!!", Toast.LENGTH_SHORT).show();

                }else if (value == "WANDERLUST"){
                    //have it
                     Toast.makeText(this, "Got it!", Toast.LENGTH_SHORT).show();

                }else{
                    //too low
                    Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.traveler_icon:
                if (int_score >= 50000 && int_score < 75000 && value != "Unseasable traveler"){
                    //Todo ok
                    Toast.makeText(this, "Congratz!! Now you are an unseasable traveler!!", Toast.LENGTH_SHORT).show();
                    int resId = getResources().getIdentifier("ic_traveler", "mipmap", getPackageName());
                    picture_score.setImageResource(resId);
                    title.setText("Unseasable traveler");

                }else if(int_score > 75000){
                    //too high
                    Toast.makeText(this, "get the next title!!", Toast.LENGTH_SHORT).show();
                }else if (value == "Unseasable traveler"){
                    //have it
                    Toast.makeText(this, "Got it!", Toast.LENGTH_SHORT).show();

                }else{
                    //too low
                    Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.backpacker_icon:
                if (int_score >= 30000 && int_score < 50000 && value != "BACKPACKER"){
                    //Todo ok
                    Toast.makeText(this, "Congratz!! Now you are a backpacker!!", Toast.LENGTH_SHORT).show();
                    int resId = getResources().getIdentifier("ic_backpacker", "mipmap", getPackageName());
                    picture_score.setImageResource(resId);
                    title.setText("BACKPACKER");

                }else if(int_score > 50000){
                    //too high
                    Toast.makeText(this, "get the next title!!", Toast.LENGTH_SHORT).show();
                }else if (value == "BACKPACKER"){
                    //have it
                    Toast.makeText(this, "Got it!", Toast.LENGTH_SHORT).show();

                }else{
                    //too low
                    Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.beginner_icon:

                if (int_score < 30000){

                    Toast.makeText(this, "welcome to the world of travelers!! Move out to get points!", Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }
}

