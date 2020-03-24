package com.example.coronaaware;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

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

    Uri filePath;
    String aadharImage="",patientImage="";
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.registration);
        }
//Firebase init
        storage = FirebaseStorage.getInstance();
        mstorageReference = storage.getReference();
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
        img_aadhaar.setImageResource(R.drawable.aadhar);
        img_patient.setImageResource(R.drawable.person);
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
               clickpic("AADHAR");
            }
        });

        img_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickpic("IMAGE");
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
        if (p_phone_number.isEmpty() || p_phone_number.length() < 9 || p_phone_number.length()>10) {
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
        patientRegisterModel.setContact_no("+91"+p_phone_number);
        patientRegisterModel.setDistrict(p_district);
        patientRegisterModel.setBloodGroup(p_blood_grp);
        patientRegisterModel.setUid(mAuth.getUid());
        patientRegisterModel.setImageId(patientImage);
        patientRegisterModel.setAadharImage(aadharImage);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PatientRegister");
        myRef.push().setValue(patientRegisterModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                    Toast.makeText(PatientRegister.this, "Data could not be saved", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Data saved successfully.");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PatientRegister.this, "Data saved successfully.", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    age.setText("");
                    bloodGroup.setText("");
                    phone_number.setText("");
                    district.setText("");
                    aad_no.setText("");
                    img_aadhaar.setImageResource(R.drawable.aadhar);
                    img_patient.setImageResource(R.drawable.person);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_aadhaar.setImageBitmap(photo);
            filePath = getImageUri(getApplicationContext(), photo);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(filePath));
            upload("AADHAR");
            //System.out.println(mImageCaptureUri);
        }else if(requestCode == 101 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_patient.setImageBitmap(photo);
            filePath = getImageUri(getApplicationContext(), photo);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(filePath));
            upload("IMAGE");
            //System.out.println(mImageCaptureUri);
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        Log.e("file",path);
        return path;
    }

    private void upload(final String aadar) {
        if(filePath != null){
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Uploading....");
            progress.show();

            StorageReference ref= mstorageReference.child(aadar+"/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(PatientRegister.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    String imageUrl = taskSnapshot.getUploadSessionUri().toString();
                    if(aadar.equals("AADHAR")){
                        aadharImage="";
                        aadharImage=imageUrl;
                    }else{
                        patientImage="";
                        patientImage=imageUrl;
                    }
                    Log.e("URI",imageUrl);
                    //Picasso.with(getBaseContext()).load(imageUrl).into(imgFirebase);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(PatientRegister.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progres_time = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progress.setMessage("Uploaded "+(int)progres_time+" %");
                }
            });
        }

    }

    private void clickpic(String image) {
        // Check Camera

        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            // Open default camera
            if(image.equals("AADHAR")){
                imageUri = null;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                // start the image capture Intent
                startActivityForResult(intent, 100);
            }else{
                imageUri = null;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                // start the image capture Intent
                startActivityForResult(intent, 101);
            }


        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.Menu_AboutUs:
                //About US
                break;

            case R.id.Menu_LogOutMenu:
                //Do Logout
                mAuth.signOut();
                finish();
                startActivity(new Intent(PatientRegister.this, OTPAuthentication.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
