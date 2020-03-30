package com.example.coronaaware.ui.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaaware.R;
import com.example.coronaaware.model.UserRegisterModel;
import com.example.coronaaware.ui.ui.ApprovalActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OfficalsAdaper extends RecyclerView.Adapter<OfficalsAdaper.MyViewHolder> {

    Context mContext;
    private ArrayList<UserRegisterModel> moviesList;

    public OfficalsAdaper(ArrayList<UserRegisterModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_officals_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserRegisterModel model = moviesList.get(position);


        holder.userName.setText("Name : " + model.getName());
        holder.mobile.setText("Mobile No: " + model.getMobile());
        holder.listDistrict.setText("District  : " + model.getDistrict());
        holder.status.setText("Status : " + model.getStatus());
        //Picasso.get().load(model.getImageId()).error(R.mipmap.ic_launcher_round).into(holder.imageView);
        holder.mobile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getMobile()));
                mContext.startActivity(intent);
            }
        });
        if (model.getStatus()) {
            holder.status.setText("Approved");
            holder.status.setTextColor(Color.BLUE);
        } else {
            holder.status.setText("Waiting for Approval");
            holder.status.setTextColor(Color.RED);
        }
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ApprovalActivity.class);
                intent.putExtra("uid", moviesList.get(position).getUid());
                mContext.startActivity(intent);
            }
        });


       /* holder.switchApproved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   holder.switchApproved.setChecked(isChecked);
                   holder.status.setText("Approved");
                   FirebaseDatabase database = FirebaseDatabase.getInstance();
                   DatabaseReference myRef = database.getReference("Officials").child(model.getUid()).child("status");
                   callUpdate(myRef,true);
               }else{
                   holder.switchApproved.setChecked(false);
                   holder.status.setText("Waiting for Approval");
                   FirebaseDatabase database = FirebaseDatabase.getInstance();
                   DatabaseReference myRef = database.getReference("Officials").child(model.getUid()).child("status");
                   callUpdate(myRef,false);
               }

            }
        });*/

    }

    private void callUpdate(final DatabaseReference myRef, final boolean b) {
        UserRegisterModel userRegisterModel = new UserRegisterModel();
        userRegisterModel.setStatus(true);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRef.setValue(b);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, mobile, status, listDistrict;


        public MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.Officalsname);
            mobile = view.findViewById(R.id.Officalsmobile);
            listDistrict = view.findViewById(R.id.OfficalslistDistrict);
            status = view.findViewById(R.id.OfficalslistStats);

        }
    }
}
