package com.example.coronaaware.ui.ui;

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
    public static final String doctors = "doctor_pref";
    public static final String official = "official_pref";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        doctor = findViewById(R.id.button_doctor);
        officials = findViewById(R.id.button_officials);
        pref = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);


        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra("value", "doctors");
                //shared preference
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(doctors, "doctor_pref");
                editor.apply();
                startActivity(intent);
            }
        });

        officials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra("value", "officials");
                //shared preference
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(official, "official_pref");
                editor.apply();
                startActivity(intent);
            }
        });
    }
}
