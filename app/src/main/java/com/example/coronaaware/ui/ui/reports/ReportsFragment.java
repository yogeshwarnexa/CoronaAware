package com.example.coronaaware.ui.ui.reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ReportsFragment extends Fragment {
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    private View root;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

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

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = ProgressDialog.show(getActivity(),
                "", "Please Wait!");
        showList();

        return root;
    }

    private void showList() {

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<PatientRegisterModel>().
                        setQuery(reference, PatientRegisterModel.class).build();

        FirebaseRecyclerAdapter<PatientRegisterModel, UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<PatientRegisterModel, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull final PatientRegisterModel model) {
                        progressDialog.dismiss();
                        holder.userName.setText("Name : " + model.getUsername());
                        holder.age.setText("Age : " + model.getAge());
                        holder.mobile.setText("Mobile No : " + model.getContact_no());
                        holder.listDistrict.setText("District : " + model.getDistrict());
                        holder.listAadhar.setText("AADHAR No: " + model.getAadhaar_no());
                        holder.bloodgroup.setText("Blood Group : " + model.getBloodGroup());
                        //Picasso.get().load(model.getImageId()).error(R.mipmap.ic_launcher_round).into(holder.imageView);
                        holder.mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getContact_no()));
                                startActivity(intent);
                            }
                        });

                        Log.d("Images", "images into loaded");
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round);

                        if (!model.getImageId().equals("empty")) {
                            Glide.with(getContext())
                                    .load(model.getImageId())
                                    .apply(options)
                                    .into(holder.imageView);
                        } else if (!model.getAadharImage().equals("empty")) {
                            Glide.with(getContext())
                                    .load(model.getAadharImage())
                                    .apply(options)
                                    .into(holder.imageView);
                        } else {
                            Glide.with(getContext())
                                    .load("empty")
                                    .apply(options)
                                    .into(holder.imageView);
                        }


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
