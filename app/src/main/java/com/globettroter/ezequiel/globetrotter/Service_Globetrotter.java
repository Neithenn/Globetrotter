package com.globettroter.ezequiel.globetrotter;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by Ezequiel on 07/03/2016.
 */
public class Service_Globetrotter extends Service {


    final class Service_thread implements Runnable {

        int service_id;
        private LocationManager locationManager;
        private LocationListener locationListener;

        Service_thread(int service_id) {
            this.service_id = service_id;

        }

        @Override
        public void run() {

            Log.i("SERVICE", "Running Thread");
//GET THE LOCACION IF IT HAS CHANGED
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    Double lat = location.getLatitude();
                    Double lon = location.getLongitude();

                    String latitude = String.valueOf(lat);
                    String longitude = String.valueOf(lon);

                    String coordinates = latitude + "/" + longitude + ",";
                    Log.i("SERVICE", "Lat and Long changed " + coordinates);


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                    builder
                            .setSmallIcon(R.mipmap.ic_paris)
                            .setContentTitle("Globetrotter");
                    builder.setContentText(coordinates);

                    Notification notification = builder.build();
                    NotificationManagerCompat.from(getApplicationContext()).notify(0, notification);

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }


            };

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("SERVICE", "On Start Command Service");
        Thread thread = new Thread(new Service_thread(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
