package com.globettroter.ezequiel.globetrotter;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ezequiel on 02/03/2016.
 */
public class get_internal_memory {

    private Context context;
    private TextView score;
    private String[] old_coordinates;

    public get_internal_memory(Context context, TextView score) {
        this.context=context;
        this.score=score;
    }

    public void memory_access(String id, String users_score, String email, String name) {

    try {
        BufferedReader fin =
                new BufferedReader(
                        new InputStreamReader(
                                context.openFileInput("saved_locations.txt")));

            String value = fin.readLine();
            Log.i("file", value);
            if (value != null) {
                String[] coordinates = value.split(",");

                Toast.makeText(context,"Getting your saved location...hold on!",Toast.LENGTH_LONG).show();

                for (int i=0; i < coordinates.length; i++){

                    String[] aux = coordinates[i].split("/");

                    if (old_coordinates == null) {
                        //POINT OF INTEREST - CHECK IF YOU ARE NEAR TO A POINT OF INTEREST
                        new Point_of_interest(context,users_score,score,id, aux[0], aux[1]).execute();
                        //Country - Continent - city // CHECK IF YOU ARE IN A NEW COUNTRY OR CONTINENT OR CITY
                       // new GetLocationName(context, users_score, score,id,email,name, aux[0], aux[1]).execute();

                    }else if(Check_difference(aux, old_coordinates)){
                        //POINT OF INTEREST - CHECK IF YOU ARE NEAR TO A POINT OF INTEREST
                        new Point_of_interest(context,users_score,score,id, aux[0], aux[1]).execute();
                        //Country - Continent - city // CHECK IF YOU ARE IN A NEW COUNTRY OR CONTINENT OR CITY
                        //new GetLocationName(context, users_score, score,id,email,name, aux[0], aux[1]).execute();

                    }else{

                        Log.i("FAIL", "Same log and lat in the file");
                    }

                    old_coordinates = aux;
                }


            }


        fin.close();

        context.deleteFile("saved_locations.txt");

    } catch (Exception ex) {
        Log.e("Ficheros", "No hay fichero");
    }
}



    public Boolean Check_difference (String[] new_coordinates, String[] old_coordinates){

        float new_lat = Float.valueOf(new_coordinates[0]);
        float new_long = Float.valueOf(new_coordinates[1]);
        float old_lat = Float.valueOf(old_coordinates[0]);
        float old_long = Float.valueOf(old_coordinates[1]);

        if (((new_lat - old_lat) <= 0.001) &&((new_lat - old_lat) >= -0.001)){

            if ((new_long - old_long) <=0.001 && (new_long - old_long) >= -0.001){
                //Same position
                return false;

            }else{
                //There are different
                return true;
            }
        }else{
            //There are different
            return  true;
        }

    }
}
