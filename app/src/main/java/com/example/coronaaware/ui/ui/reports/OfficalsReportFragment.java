package com.example.coronaaware.ui.ui.reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaaware.R;
import com.example.coronaaware.model.UserRegisterModel;
import com.example.coronaaware.ui.ui.adapter.OfficalsAdaper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OfficalsReportFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs";
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    ArrayList<UserRegisterModel> userRegisterModelArrayList;
    OfficalsAdaper reportAdaper;
    FirebaseAuth mAuth;
    private View root;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRegisterModelArrayList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerview);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = ProgressDialog.show(getActivity(),
                "", "Please Wait!");
        getList();

        return root;
    }

    private void getList() {
        reference = firebaseDatabase.getReference("Officials");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        UserRegisterModel userRegisterModel = dataSnapshot1.getValue(UserRegisterModel.class);
                        userRegisterModelArrayList.add(userRegisterModel);
                    }
                    progressDialog.dismiss();
                    reportAdaper = new OfficalsAdaper(userRegisterModelArrayList, getActivity());
                    recyclerView.setAdapter(reportAdaper);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Data is not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
