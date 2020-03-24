package com.example.coronaaware;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 2500;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()!=null){
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this,
                            PatientRegister.class);

                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this,
                            OTPAuthentication.class);

                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_TIME);
    }

}

