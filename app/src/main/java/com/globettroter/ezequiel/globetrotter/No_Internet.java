package com.globettroter.ezequiel.globetrotter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class No_Internet extends AppCompatActivity {

    private Button save_loc_bt;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView coords_tx;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        coords_tx = (TextView) findViewById(R.id.lat_long_tx);
        save_loc_bt = (Button) findViewById(R.id.save_loc_bt);



                //GET THE LOCACION IF IT HAS CHANGED
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        locationManager.removeUpdates(this);

                        Double lat = location.getLatitude();
                        Double lon = location.getLongitude();

                        String latitude = String.valueOf(lat);
                        String longitude = String.valueOf(lon);

                        String coordinates = latitude+"/"+longitude+",";
                        Log.i("LAT", coordinates);


                        try {
                            FileOutputStream fos = openFileOutput("saved_locations.txt", getApplicationContext().MODE_APPEND);
                            fos.write(coordinates.getBytes());
                            fos.close();
                            Toast.makeText(No_Internet.this,"Location saved!",Toast.LENGTH_SHORT).show();
                            coords_tx.setText(coordinates);
                            pDialog.dismiss();


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

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
        save_loc_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new ProgressDialog(No_Internet.this);
                pDialog.setMessage("Getting your location!!");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

            }
        });

    }
}
