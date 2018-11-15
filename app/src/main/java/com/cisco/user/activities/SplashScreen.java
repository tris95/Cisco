package com.cisco.user.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cisco.user.R;
import com.cisco.user.utils.Utilities;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    Utilities util = new Utilities();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                if (!util.isLogin(SplashScreen.this)) {
                    startActivity(new Intent(SplashScreen.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    finish();
                }
            }

        }, 1500);
    }
}
