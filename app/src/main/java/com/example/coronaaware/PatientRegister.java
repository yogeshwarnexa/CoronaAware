package com.example.coronaaware;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatientRegister extends AppCompatActivity {
    EditText name, age, bloodGroup, aad_no, phone_number, district;
    TextView aadhaarText, patient_imageText;
    ImageView img_aadhaar, img_patient;
    Button send_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        name = findViewById(R.id.username);
        phone_number = findViewById(R.id.phonenumber);
        age = findViewById(R.id.age);
        bloodGroup = findViewById(R.id.bloodgrp);
        district = findViewById(R.id.district);
        aad_no = findViewById(R.id.aad_no);

        aadhaarText = findViewById(R.id.patient_aadhaar_photo);
        patient_imageText = findViewById(R.id.patient_photo_name);

        img_aadhaar = findViewById(R.id.aadhar_photo);
        img_patient = findViewById(R.id.patient_photo_img);

        send_details = findViewById(R.id.submit_patient_details);

        img_aadhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        send_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    private void validateFields() {
        String pt_name = name.getText().toString();
    }
}
