package com.suchalak.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suchalak.R;
import com.suchalak.Utils;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    SharedPreferences sharedPreferences;
    String login_session,login_role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        login_session = sharedPreferences.getString("first_time_login", "def-val");
       // Toast.makeText(this, ""+login_session, Toast.LENGTH_SHORT).show();
        login_role = sharedPreferences.getString("role", "def-val");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_splash_screen);
        //this will bind your MainActivity.class file with activity_main.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(login_session.equalsIgnoreCase("yes")){
                    Intent i=new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    if (login_role.equalsIgnoreCase("User")) {
                        Intent i = new Intent(SplashScreenActivity.this, UserDashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashScreenActivity.this, DriverDashboardActivity.class);
                        startActivity(i);
                        finish();
                    }
                }


            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}