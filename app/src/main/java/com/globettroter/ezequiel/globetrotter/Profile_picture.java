package com.globettroter.ezequiel.globetrotter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ezequiel on 23/01/2016.
 * FILL UP THE IMAGE VIEW of MENSAJE.JAVA
 */
public class Profile_picture extends AsyncTask <String, String, Bitmap> {

    private ImageView img;

    public Profile_picture(ImageView img){
        this.img = img;

    }


    protected Bitmap doInBackground(String... params) {


            if (params[0] != null) {

                try {

                    Log.i("facebook pic url", params[0]);
                URL url = new URL(params[0]);


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;

            }catch(MalformedURLException e){
                e.printStackTrace();
                return null;
            }catch(IOException e){
                e.printStackTrace();
                return null;
            }

        }else{
        Log.e("ERROR", "No URL en Profile picture");
            return null;
        }

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        img.setImageBitmap(bitmap);


    }
}
