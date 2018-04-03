package edu.cmu.ecobin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
    private static final String USER_POSTS = "user_posts";

    private CallbackManager mCallbackManager;
    private TextView email_textview;
    private TextView name_textview;
    private TextView token_textview;
    private LoginResult fbLoginResult;
    String name;
    String email;
    String tokenSession;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mCallbackManager = CallbackManager.Factory.create();
        email_textview = (TextView) findViewById(R.id.fbemail);
        name_textview = (TextView) findViewById(R.id.fbname);
        token_textview = (TextView) findViewById(R.id.fbtoken);
        LoginButton mLoginButton = findViewById(R.id.login_button);

        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL));
        if (isLoggedIn()) {
            Log.d("LoginActivity", "already login");
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            // startActivity(mainActivityIntent);
        }

        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("LoginActivity", "on success");


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
                                    email_textview.setText(LoginActivity.this.email);
                                    name_textview.setText(LoginActivity.this.name);

                                    token_textview.setText(loginResult.getAccessToken().getToken());
                                    LoginActivity.this.tokenSession = loginResult.getAccessToken().getToken();

                                    String requestBody = buildSetUserSessionRequestBody();
                                    new sendUserInfo(requestBody).execute();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("myTag", "on cancel");
                email_textview.setText("cancel");

            }

            @Override
            public void onError(FacebookException e) {
                Log.d("myTag", e.toString());
                email_textview.setText("error");

                // Handle exception

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            // startActivity(mainActivityIntent);

        }
    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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
                Log.i("LoginActivityEmail", "!!!!!!!!!!!!!!!!!!!!!");

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
                        Log.v("responseJson", responseJson.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(responseJson!= null && responseJson.has("userId")){
                try{
                    String id = responseJson.getString("userId");
                    Log.v("LoginActivity", id);
                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String buildSetUserSessionRequestBody(){
        String body = "{"
                + "\"name\": \"" + this.name + "\""
                + ",\"email\": \"" + this.email + "\""
                + ",\"sessionToken\": \"" + this.tokenSession + "\""
                + "}";
        Log.v("setuserreq body", body);
        return body;
    }
}