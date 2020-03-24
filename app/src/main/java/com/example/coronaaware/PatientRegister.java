package com.example.coronaaware;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coronaaware.model.PatientRegisterModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class PatientRegister extends AppCompatActivity {
    private static int CAMERA_REQUEST_CODE = 1;
    DatabaseReference reference;
    PatientRegisterModel patientRegisterModel;
    EditText name, age, bloodGroup, aad_no, phone_number, district;
    TextView aadhaarText, patient_imageText;
    ImageView img_aadhaar, img_patient;
    Button send_details;
    ProgressBar progressBar;
    private StorageReference mstorageReference;
    private StorageTask uploadTask;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.patient_registration);
        }

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

        progressBar = findViewById(R.id.progressbar);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mstorageReference = FirebaseStorage.getInstance().getReference("Images");
        reference = FirebaseDatabase.getInstance().getReference().child("Patient");

        patientRegisterModel = new PatientRegisterModel();


        img_aadhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
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
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(PatientRegister.this, "Upload in progress", Toast.LENGTH_SHORT).show();

                } else {
                    registerPatient();
                }

            }
        });
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void registerPatient() {
        String imageId = System.currentTimeMillis() + "." + getExtension(imageUri);
        String uname = name.getText().toString().trim();
        String p_age = age.getText().toString().trim();
        String p_phone_number = phone_number.getText().toString().trim();
        String p_blood_grp = bloodGroup.getText().toString().trim();
        String p_district = district.getText().toString().trim();
        String p_aad_no = aad_no.getText().toString().trim();

        if (uname.isEmpty() || uname.length() < 2) {
            name.setError("UserName required");
            name.requestFocus();
            return;
        }
        if (p_age.isEmpty()) {
            age.setError("UserName required");
            age.requestFocus();
            return;
        }
        if (p_phone_number.isEmpty() || p_phone_number.length() < 10) {
            phone_number.setError("Contact Number required");
            phone_number.requestFocus();
            return;
        }
        if (p_blood_grp.isEmpty()) {
            bloodGroup.setError("BloodGroup required");
            bloodGroup.requestFocus();
            return;
        }
        if (p_district.isEmpty()) {
            district.setError("District required");
            district.requestFocus();
            return;
        }
        if (p_aad_no.isEmpty() || p_aad_no.length() < 12) {
            aad_no.setError("Aadhaar Number required");
            aad_no.requestFocus();
            return;
        }

        PatientRegisterModel patientRegisterModel = new PatientRegisterModel();
        patientRegisterModel.setUsername(uname);
        patientRegisterModel.setAge(p_age);
        patientRegisterModel.setAadhaar_no(p_aad_no);
        patientRegisterModel.setContact_no(p_phone_number);
        patientRegisterModel.setDistrict(p_district);
        patientRegisterModel.setBloodGroup(p_blood_grp);
        patientRegisterModel.setImageId(imageId);

        reference.push().setValue(patientRegisterModel);
        StorageReference ref = mstorageReference.child(imageId);

        uploadTask = ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PatientRegister.this, "Datas Uploaded Successfully...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
// BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            // Set the image in imageview for display
            img_patient.setImageBitmap(photo);
        }
    }
}
