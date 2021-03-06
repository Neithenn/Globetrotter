package com.globettroter.ezequiel.globetrotter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton = null;
    private CallbackManager callbackManager;
    private ProgressDialog pDialog;
    private TextView no_internet;
    private TextView globetrotter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        Typeface tf  = Typeface.createFromAsset(getAssets(), "fonts/cour.ttf");
        globetrotter = (TextView) findViewById(R.id.globetrotter_title);
        globetrotter.setTypeface(tf);


// IF USER IS ALREADY LOGGED IN
        if (isFacebookLoggedIn()) {
            Log.i("TAG", "logged in already");

            //prepare fields with email
            // App code
            pDialog = new ProgressDialog(MainActivity.this);

            pDialog.setMessage("Aguarde por favor");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


// RETRIEVING NAME/ID/EMAIL/PROFILE PICTURE FROM FACEBOOK
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            pDialog.dismiss();

                            if (response.getError() != null) {
                                // handle error
                                Log.e("Error", "Error graph data facebook");

                            } else {

                                Log.d("Response", response.getJSONObject().toString());
                                String email = object.optString("email");
                                String id = object.optString("id");
                                String name = object.optString("name");
                                String profilePicUrl = "";

                                // GET PROFILE PICTURE FACEBOOK URL
                                if (object.has("picture")){

                                    try {

                                        profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }


                                // GET SCORE USER PLUS INTENT TO NEW ACTIVITY
                                 new Get_score_user(MainActivity.this, id, email, name, profilePicUrl).execute();

                                                           }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();




        }else{

        Log.i("LOG IN", "Not already logged in");
        loginButton = (LoginButton)findViewById(R.id.login_button);
            // FACEBOOK PERMISSIONS
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override


            //ON LOG IN SUCCESSFUL
            public void onSuccess(LoginResult loginResult) {

                Log.i("TAG", "success");


                // App code
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Please Wait..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                pDialog.dismiss();
                                Log.d("Response", response.getJSONObject().toString());
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String email = object.optString("email");
                                    String id = object.optString("id");
                                    String name = object.optString("name");
                                    String birthday = object.optString("birthday");
                                    String gender = object.optString("gender");
                                    String profilePicUrl = "";

                                    //ADD USER INTO DATABASE IN CASE DOESNT EXIST
                                    new Check_user().execute(id, email, name, birthday, gender);


                                    if (object.has("picture")) {

                                        try {

                                            profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    //get points, title and image file path
                                    new Get_score_user(MainActivity.this, id, email, name, profilePicUrl).execute();

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();



              /*  Intent intent = new Intent(getApplicationContext(), Mensaje.class);
                                startActivity(intent);
                    */

            }

            @Override
            public void onCancel() {
                Log.i("TAG", "cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("TAG", "Error");
            }
        });

    }
        // Text View NO INTERNET CONNECTION
        no_internet = (TextView) findViewById(R.id.noInternet_tx);
        no_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this, No_Internet.class);
                startActivity(intent);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onresume", "facebookon resume");
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onpause", "facebookon pause");
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }
}

    class Check_user extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... params) {
            //SEND DATA TO LOCAL SERVER
            Enviar_user_data(params[0],params[1],params[2],params[3],params[4]);
            return null;

        }


        public void Enviar_user_data(String id, String email, String name, String birthday, String gender){
            //API REST CALL
            try {
                HttpRequest con = new HttpRequest("http://globetrotterdomain.co.nf/usercheck.php");
                HashMap<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("email",email);
                params.put("name",name);
                params.put("birthday",birthday);
                params.put("gender",gender);

                con.preparePost().withData(params).send();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
