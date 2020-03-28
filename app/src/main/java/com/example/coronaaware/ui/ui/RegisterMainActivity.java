package com.example.coronaaware.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coronaaware.R;

public class RegisterMainActivity extends AppCompatActivity {
    Button doctor, officials;
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        doctor = findViewById(R.id.button_doctor);
        officials = findViewById(R.id.button_officials);
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().putString(getString(R.string.userType), "user").apply();
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra("value", "doctors");
                startActivity(intent);
            }
        });

        officials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().putString(getString(R.string.userType), "officals").apply();
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra("value", "officials");
                startActivity(intent);
            }
        });
    }
}
