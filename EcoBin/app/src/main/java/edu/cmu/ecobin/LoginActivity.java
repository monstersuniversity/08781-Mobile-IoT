package edu.cmu.ecobin;

import android.app.Activity;
import android.content.Intent;
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

import java.util.Arrays;

public class LoginActivity extends Activity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";

    private CallbackManager mCallbackManager;
    private TextView email_textview;
    private TextView name_textview;
    private TextView token_textview;
    String name;
    String email;


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

        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("myTag", "on success");


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
            startActivity(mainActivityIntent);
        }
    }
}