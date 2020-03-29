package com.example.coronaaware.ui.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.coronaaware.R;
import com.example.coronaaware.model.PatientRegisterModel;

import java.util.ArrayList;

public class ReportAdaper extends RecyclerView.Adapter<ReportAdaper.MyViewHolder> {

    Context mContext;
    private ArrayList<PatientRegisterModel> moviesList;

    public ReportAdaper(ArrayList<PatientRegisterModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PatientRegisterModel model = moviesList.get(position);


        holder.userName.setText("Name : " + model.getUsername());
        holder.age.setText("Age : " + model.getAge());
        holder.mobile.setText("Mobile No : " + model.getContact_no());
        holder.listDistrict.setText("District : " + model.getDistrict());
        holder.listAadhar.setText("AADHAR No: " + model.getAadhaar_no());
        holder.bloodgroup.setText("Blood Group : " + model.getBloodGroup());
        //Picasso.get().load(model.getImageId()).error(R.mipmap.ic_launcher_round).into(holder.imageView);
        holder.mobile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getContact_no()));
                mContext.startActivity(intent);
            }
        });

        Log.d("Images", "images into loaded");
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        if (!model.getImageId().equals("empty")) {
            Glide.with(mContext)
                    .load(model.getImageId())
                    .apply(options)
                    .into(holder.imageView);

        } else if (!model.getAadharImage().equals("empty")) {
            Glide.with(mContext)
                    .load(model.getAadharImage())
                    .apply(options)
                    .into(holder.imageView);
        } else {
            Glide.with(mContext)
                    .load("empty")
                    .apply(options)
                    .into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, mobile, age, listDistrict, listAadhar, bloodgroup;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            userName = view.findViewById(R.id.name);
            mobile = view.findViewById(R.id.mobile);
            age = view.findViewById(R.id.age);
            listDistrict = view.findViewById(R.id.listDistrict);
            listAadhar = view.findViewById(R.id.listAadhar);
            userName = view.findViewById(R.id.name);
            bloodgroup = view.findViewById(R.id.bloodgroup);
            imageView = view.findViewById(R.id.imageView);
        }
    }
}
