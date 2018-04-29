package edu.cmu.ecobin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.share.Share;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EMAIL = "email";
    private static final String NAME = "name";
    public static final String LOGOUTUSER = "logoutUser";
    ImageView user_picture;
    SharedPreferences userIdPref;
    String TAG = "MainActivity(Menu)";
    public static final String USERID = "userId";
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        userIdPref = this.getPreferences(Context.MODE_PRIVATE);
        Log.v(TAG, "ON CREATE");
        if (userIdPref.contains(USERID)) {
            Log.v(TAG, user.getUserID());
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayMenuActivity(R.id.nav_home);


        View headerView = navigationView.getHeaderView(0);
        TextView drawerUsername = (TextView) headerView.findViewById(R.id.profile_name);
        TextView drawerAccount = (TextView) headerView.findViewById(R.id.profile_email);
        drawerUsername.setText(user.getUserName());
        drawerAccount.setText(user.getUserEmail());


        user_picture=(ImageView)headerView.findViewById(R.id.profile_pic);
        try {
            String urlStr = "https://graph.facebook.com/"+user.getFacebookID()+"/picture?width=150&height=150";
            Log.v("profile pic link", urlStr);
            URL img_value = new URL(urlStr);
            Bitmap prof_icon = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
            if (prof_icon == null) {
                Log.v(TAG, "null");
            }

            user_picture.setImageBitmap(prof_icon);
//            user_picture.setImageResource(R.drawable.profilepic);
        } catch(IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.voice) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayMenuActivity(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                fragment = new Home();
                break;
            case R.id.nav_score:
                fragment = new Score();
                break;
            case R.id.nav_history:
                fragment = new History();
                break;
            case R.id.nav_tree:
                fragment = new Tree();
                break;
            case R.id.nav_signout:
                LoginManager.getInstance().logOut();
                Log.i(TAG,"Logout");
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra(LOGOUTUSER, true);
                startActivity(i);

                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displayMenuActivity(id);
        return true;
    }

}
