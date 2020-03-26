package com.example.coronaaware.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coronaaware.R;
import com.example.coronaaware.model.UserRegisterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
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
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "userName";
    SharedPreferences sharedpreferences;
    private static final String KEY_PENDING_EMAIL = "key_pending_email";
    String email = "ityogesh5@gmail.com";
    String email_link, pending_email;
    String adminEmail = "ityogesh5@gmail.com";
    String TAG = "EMAIL_AUTH";
    private SharedPreferences pref;

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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);

        //Reading preferences if it?s already been set
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        pending_email = pref.getString(KEY_PENDING_EMAIL, null);

        //Checking for pending email?.
        if (pending_email != null) {
            adminEmail = pending_email;
            Log.d(TAG, "Getting Shared Preferences" + pending_email);
        }

        //Creating intent for catching the link
        /*Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            email_link = intent.getData().toString();
            Log.d(TAG, "got an intent: " + email_link);
        }
        // Confirm the link is a sign-in with email link.
        if (mAuth.isSignInWithEmailLink(email_link)) {
            startActivity(new Intent(getApplicationContext(), OTPAuthentication.class));
            finish();
        }
*/
        if (mAuth.getCurrentUser() != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("User");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            UserRegisterModel userRegisterModel = dataSnapshot1.getValue(UserRegisterModel.class);
                            if (userRegisterModel.getUid() != null) {
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

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, name);
        editor.apply();

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
                    //sendVerificationMail(adminEmail);

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

    private void sendVerificationMail(final String email) {
        //Save Email Address using SharedPreference Editor.
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_PENDING_EMAIL, email);
        editor.apply();
        Log.d(TAG, "Email sending...");

        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder().setAndroidPackageName(getPackageName(), false,
                null).setHandleCodeInApp(true).setUrl("https://com.example.trackcovid19/?https://trackcovid19.page.link/ZCg5").build();

        mAuth.sendSignInLinkToEmail(email, actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Email Sent..." + email);
                    //Toast.makeText(getApplicationContext(), "Email Sent For Approval", Toast.LENGTH_LONG).show();

                } else {
                    Log.d(TAG, "Email sending failed...");
                }

            }
        });


    }
}