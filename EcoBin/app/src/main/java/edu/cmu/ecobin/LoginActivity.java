package edu.cmu.ecobin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends Activity {
    private static final String EMAIL = "email";

    private CallbackManager mCallbackManager;
    LoginResult fbLoginResult;
    public static final String USERID = "userId";
    SharedPreferences userIdPref;
    AccessTokenTracker accessTokenTracker;
    private static final int LOGIN = 1;
    User fbUser;
    String name;
    String email;
    String TAG = "LoginActivity";
    TextView textview_profile_email;
    private boolean directUserToLogout = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        LoginButton mLoginButton = findViewById(R.id.login_button);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        mCallbackManager = CallbackManager.Factory.create();
        final Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra(MainActivity.LOGOUTUSER)){
            directUserToLogout = receivedIntent.getBooleanExtra(MainActivity.LOGOUTUSER, false);
        }

        userIdPref = this.getPreferences(Context.MODE_PRIVATE);
        Log.v("userIdPref.contains", String.valueOf(userIdPref.getAll()));


        if (userIdPref.contains(USERID) && !directUserToLogout){
            fbUser = User.getInstance();
            fbUser.setUserID(userIdPref.getString(USERID, null));
            Log.v("user singleton", "should" +
                    "" +
                    "/ be set to" + fbUser.getUserID());
            startActivityForResult(myIntent, LOGIN);

        } else {
            // Register a callback to respond to the user
            mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    Log.d(TAG, "on success");

                    fbUser = User.getInstance();
                    // App code
                    Log.i("UserToken", loginResult.getAccessToken().getToken());
                    Log.i("userID", loginResult.getAccessToken().getUserId());
                    Log.v(TAG, "1");
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.v("LoginActivity", response.toString());

                                    // Application code
                                    try {
                                        LoginActivity.this.name = object.getString("name");
                                        LoginActivity.this.email = object.getString("email");
                                        Log.i("LoginActivityName", LoginActivity.this.name);
                                        Log.i("LoginActivityEmail", LoginActivity.this.email);

                                        setUser(loginResult);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            });
                    Log.v(TAG, "2");
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                    Log.v("calling setUser now", "true");
                    Log.v("FB returned User ID", "should be set to:" + loginResult.getAccessToken().getUserId());
                    //myIntent.putExtra("key", value); //Optional parameters
                    LoginActivity.this.startActivity(myIntent);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "on cancel");

                }

                @Override
                public void onError(FacebookException e) {
                    Log.d(TAG, e.toString());

                    // Handle exception

                }
            });

        }
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //clear user singleton
                    if(fbUser != null)
                        fbUser.clearUserID();

                    //clear sharedPref
                    SharedPreferences.Editor editor = userIdPref.edit();
                    editor.remove(USERID);
                    editor.apply();
                }
            }
        };


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }



    private void setUser(LoginResult loginResult){

        if(name == null)
            Log.v("name", "set to null");
        else
            Log.v("name ", "should be set");


        fbLoginResult = loginResult;
        String requestBody = buildSetUserSessionRequestBody();
        new sendUserInfo(requestBody).execute();
    }

    private class sendUserInfo extends AsyncTask<String, String, JSONObject> {
        String body;

        sendUserInfo(String body) {
            this.body = body;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            // Create URL
            try {
                URL myEndpoint = new URL("http://ec2-52-87-198-206.compute-1.amazonaws.com:3000/setUserSession");

                HttpURLConnection myConnection
                        = (HttpURLConnection) myEndpoint.openConnection();

                myConnection.setRequestMethod("POST");


                myConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                myConnection.setRequestProperty("Accept", "application/json");
                myConnection.setDoOutput(true);
                myConnection.setDoInput(true);

                byte[] outputInBytes = this.body.getBytes("UTF-8");
                OutputStream os = myConnection.getOutputStream();
                os.write(outputInBytes);
                os.close();


                if (myConnection.getResponseCode() == 200) {

                    // Read data from response.
                    StringBuilder builder = new StringBuilder();
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                    String line = responseReader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = responseReader.readLine();
                    }
                    String responseString = builder.toString();
                    Log.v(getClass().getName(), "Response String: " + responseString);
                    JSONObject responseJson = new JSONObject(responseString);
                    // Close connection and return response code.
                    myConnection.disconnect();

                    return responseJson;
                } else {
                    // Error handling code goes here
                    String a = "" + myConnection.getResponseCode();
                    Log.i("Failure- Status Code", a);
                    Log.i("Failure- StatusMessage", myConnection.getResponseMessage());

                }
                myConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(JSONObject responseJson) {
            if(responseJson!= null && responseJson.has("status") ) {
                try {
                    String result = responseJson.getString("status");
                    Log.v("result in postexecute", result);
                    if (result.equals("success")){
                        Log.v("session API success", "result");
                        Log.v(TAG, "responseJson = " + responseJson.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(responseJson!= null && responseJson.has("userId")){
                try {
                    Log.v("set sharedPref", "coz not found in sharedPreferences");
                    fbUser.setUserID(responseJson.get("userId").toString());
                    SharedPreferences.Editor editor = userIdPref.edit();
                    editor.putString(LoginActivity.USERID, responseJson.get("userId").toString());
                    editor.commit();
                    Log.v(LoginActivity.USERID, responseJson.get("userId").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String buildSetUserSessionRequestBody(){
        String body = "{"
                + "\"name\": \"" + this.name + "\""
                + ",\"emailId\": \"" + this.email + "\""
                + ",\"sessionToken\": \"" + this.fbLoginResult.getAccessToken().getToken() + "\""
                + "}";
        Log.v("setuserreq body", body);
        return body;
    }
}