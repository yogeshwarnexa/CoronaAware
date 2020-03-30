package com.example.coronaaware.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class ApprovalActivity extends AppCompatActivity {

    Button ApprovedBtn;
    TextView textViewName, textViewMobile, textViewEmail, textViewOccupation, textViewAddress, textViewAddress2, textViewDistrict, textViewState, textViewPincode;
    Boolean status;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        setActionBar("Govt.Officals Details");
        ApprovedBtn = findViewById(R.id.ApprovedBtn);
        textViewName = findViewById(R.id.textViewName);
        textViewMobile = findViewById(R.id.textViewMobile);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewOccupation = findViewById(R.id.textViewOccupation);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewAddress2 = findViewById(R.id.textViewAddress2);
        textViewDistrict = findViewById(R.id.textViewDistrict);
        textViewState = findViewById(R.id.textViewState);
        textViewPincode = findViewById(R.id.textViewPincode);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        final String uid = intent.getStringExtra("uid");
        Log.e("UID", uid);
        if (mAuth.getCurrentUser() != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Officials").child(uid);
            getDatainfo(myRef);

        }

        ApprovedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    updateStatus(false, uid);
                } else {
                    updateStatus(true, uid);
                }
            }
        });

    }

    private void updateStatus(final boolean b, final String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Officials").child(uid).child("status");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRef.setValue(b);
                updateMapping(b, uid);
                // CallMainActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateMapping(final boolean b, final String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Mapping").child(uid).child("status");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRef.setValue(b);
                if (b) {
                    Toast.makeText(getApplicationContext(), "Approved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Approval Refuseed successfully", Toast.LENGTH_SHORT).show();
                }
                CallMainActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CallMainActivity() {
        Intent intent = new Intent(ApprovalActivity.this, AdminMainActivity.class);
        startActivity(intent);
    }

    private void getDatainfo(DatabaseReference myRef) {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserRegisterModel userRegisterModel = dataSnapshot.getValue(UserRegisterModel.class);
                    textViewAddress.setText(userRegisterModel.getAddress());
                    textViewAddress2.setText(userRegisterModel.getAddress2());
                    textViewDistrict.setText(userRegisterModel.getDistrict());
                    textViewEmail.setText(userRegisterModel.getEmail());
                    textViewMobile.setText(userRegisterModel.getMobile());
                    textViewOccupation.setText(userRegisterModel.getOccupation());
                    textViewPincode.setText(userRegisterModel.getPincode());
                    textViewState.setText(userRegisterModel.getState());
                    textViewName.setText(userRegisterModel.getName());
                    status = userRegisterModel.getStatus();
                    if (userRegisterModel.getStatus()) {
                        ApprovedBtn.setText("Approved");
                    } else {
                        ApprovedBtn.setText("Waiting for approval");
                    }
                } else {
                    Log.e("Data not exitst", "Failed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }
}
