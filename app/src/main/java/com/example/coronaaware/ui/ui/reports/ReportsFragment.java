package com.example.coronaaware.ui.ui.reports;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.coronaaware.R;
import com.example.coronaaware.model.PatientRegisterModel;
import com.example.coronaaware.model.UserRegisterModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;


public class ReportsFragment extends Fragment {
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    private View root;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String doctors = "doctor_pref";
    public static final String official = "official_pref";
    SharedPreferences preferences;
    String prefDoctors, prefOfficial;
    private FirebaseAuth mAuth;


    public ReportsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reports, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("PatientRegister");
        recyclerView = root.findViewById(R.id.recyclerview);
        preferences = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = ProgressDialog.show(getActivity(),
                "", "Please Wait!");
        prefDoctors = preferences.getString(doctors, "doctor_pref");
        prefOfficial = preferences.getString(official, "official_pref");
        showList();

        return root;
    }

    private void showList() {
        if (mAuth.getCurrentUser() != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = null;
            if (prefDoctors.equals("doctor_pref")) {
                myRef = database.getReference("User");
            }
            assert myRef != null;
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            UserRegisterModel userRegisterModel = dataSnapshot1.getValue(UserRegisterModel.class);
                            if (userRegisterModel.getUid() != null) {
                                if (mAuth.getUid().equals(userRegisterModel.getUid())) {

                                    FirebaseRecyclerOptions options =
                                            new FirebaseRecyclerOptions.Builder<PatientRegisterModel>().
                                                    setQuery(reference, PatientRegisterModel.class).build();

                                    FirebaseRecyclerAdapter<PatientRegisterModel, UserViewHolder> adapter =
                                            new FirebaseRecyclerAdapter<PatientRegisterModel, UserViewHolder>(options) {
                                                @Override
                                                protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull PatientRegisterModel model) {
                                                    progressDialog.dismiss();
                                                    holder.userName.setText("Name : " + model.getUsername());
                                                    holder.age.setText("Age : " + model.getAge());
                                                    holder.mobile.setText("Mobile No : " + model.getContact_no());
                                                    holder.listDistrict.setText("District : " + model.getDistrict());
                                                    holder.listAadhar.setText("AADHAR No: " + model.getAadhaar_no());
                                                    holder.bloodgroup.setText("Blood Group : " + model.getBloodGroup());

                                                    Log.d("Images", "images into loaded");
                                                    RequestOptions options = new RequestOptions()
                                                            .centerCrop()
                                                            .placeholder(R.mipmap.ic_launcher_round)
                                                            .error(R.mipmap.ic_launcher_round);

                                                    Glide.with(getContext())
                                                            .load(model.getImageId())
                                                            .apply(options)
                                                            .into(holder.imageView);

                                                    //Picasso.get().load(model.getImageId()).error(R.mipmap.ic_launcher_round).into(holder.imageView);


                                                }

                                                @NonNull
                                                @Override
                                                public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

                                                    return new UserViewHolder(view);
                                                }
                                            };
                                    adapter.startListening();
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                }

                            }


                        }

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName, mobile, age, listDistrict, listAadhar, bloodgroup;
        ImageView imageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            age = itemView.findViewById(R.id.age);
            listDistrict = itemView.findViewById(R.id.listDistrict);
            listAadhar = itemView.findViewById(R.id.listAadhar);
            userName = itemView.findViewById(R.id.name);
            bloodgroup = itemView.findViewById(R.id.bloodgroup);
            imageView = itemView.findViewById(R.id.imageView);


        }


    }

}
