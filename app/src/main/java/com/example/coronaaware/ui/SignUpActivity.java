package com.example.coronaaware.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coronaaware.R;
import com.example.coronaaware.model.UserRegisterModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextEmail, editTextName, editTextMobile, editTextAddress1, editTextAddress2, editTextOccupation, editTextDistrict, editTextState, editTextPincode;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.registration);
        }


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextAddress1 = findViewById(R.id.editTextAddress);
        editTextOccupation = findViewById(R.id.editTextOccupation);
        editTextAddress2 = findViewById(R.id.editTextAddress2);
        editTextDistrict = findViewById(R.id.editTextDistrict);
        editTextState = findViewById(R.id.editTextState);
        editTextPincode = findViewById(R.id.editTextPincode);


        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        if (mAuth.getCurrentUser() != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("User");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            UserRegisterModel userRegisterModel = dataSnapshot1.getValue(UserRegisterModel.class);
                            if(userRegisterModel.getUid() != null  ) {
                                if (mAuth.getUid().equals(userRegisterModel.getUid())) {
                                    editTextAddress1.setText(userRegisterModel.getAddress());
                                    editTextAddress2.setText(userRegisterModel.getAddress2());
                                    editTextDistrict.setText(userRegisterModel.getDistrict());
                                    editTextEmail.setText(userRegisterModel.getEmail());
                                    editTextMobile.setText(userRegisterModel.getMobile());
                                    editTextOccupation.setText(userRegisterModel.getOccupation());
                                    editTextPincode.setText(userRegisterModel.getPincode());
                                    editTextState.setText(userRegisterModel.getState());
                                    editTextName.setText(userRegisterModel.getName());
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }



    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String name = editTextName.getText().toString();
        final String mobile = editTextMobile.getText().toString();
        final String occupation = editTextOccupation.getText().toString();
        final String address = editTextAddress1.getText().toString();
        final String address2 = editTextAddress2.getText().toString();
        final String district = editTextDistrict.getText().toString();
        final String state = editTextState.getText().toString();
        final String pincode = editTextPincode.getText().toString();
        if (name.isEmpty() || name.length() < 2) {
            editTextName.setError(getString(R.string.firebase_namerequired));
            editTextName.requestFocus();
            return;
        }
        if (mobile.isEmpty() || mobile.length() < 10) {
            editTextMobile.setError(getString(R.string.firebase_mobolerequired));
            editTextMobile.requestFocus();
            return;
        }
        if (occupation.isEmpty() || occupation.length() < 3) {
            editTextOccupation.setError(getString(R.string.firebase_occupation_required));
            editTextOccupation.requestFocus();
            return;
        }
        if (address.isEmpty() || address.length() < 6) {
            editTextAddress1.setError(getString(R.string.firebase_Addressrequired));
            editTextAddress1.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.firebase_emailrequired));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(mobile).matches()) {
            editTextMobile.setError(getString(R.string.firebase_mobileValidation));
            editTextMobile.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.firebase_emailValidation));
            editTextEmail.requestFocus();
            return;
        }

        if (address2.isEmpty() || address2.length() < 4) {
            editTextAddress2.setError(getString(R.string.firebase_Addressrequired));
            editTextAddress2.requestFocus();
            return;
        }
        if (district.isEmpty() || district.length() < 4) {
            editTextDistrict.setError("District is required! ");
            editTextDistrict.requestFocus();
            return;
        }
        if (state.isEmpty() || state.length() < 4) {
            editTextDistrict.setError("State is required! ");
            editTextDistrict.requestFocus();
            return;
        }

        if (pincode.isEmpty() || pincode.length() < 4) {
            editTextDistrict.setError("State is required! ");
            editTextDistrict.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        UserRegisterModel userRegisterModel = new UserRegisterModel();
        userRegisterModel.setName(name);
        userRegisterModel.setMobile(mobile);
        userRegisterModel.setAddress(address);
        userRegisterModel.setAddress2(address2);
        userRegisterModel.setEmail(email);
        userRegisterModel.setOccupation(occupation);
        userRegisterModel.setDistrict(district);
        userRegisterModel.setState(state);
        userRegisterModel.setPincode(pincode);
        userRegisterModel.setUid(mAuth.getUid());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        myRef.push().setValue(userRegisterModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                    Toast.makeText(SignUpActivity.this, "Data could not be saved", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Data saved successfully.");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Data saved successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;
        }
    }
}