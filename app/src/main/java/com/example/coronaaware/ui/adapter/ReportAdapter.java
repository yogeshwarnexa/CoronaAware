package com.example.coronaaware.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaaware.R;
import com.example.coronaaware.model.PatientRegisterModel;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private ArrayList<PatientRegisterModel> patientRegisterModelArrayList;

    // RecyclerView recyclerView;
    public ReportAdapter(ArrayList<PatientRegisterModel> patientRegisterModelArrayList) {
        this.patientRegisterModelArrayList = patientRegisterModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PatientRegisterModel myListData = patientRegisterModelArrayList.get(position);
        Log.d("ListSize", String.valueOf(patientRegisterModelArrayList.size()));

        Log.e("Name", myListData.getUsername());
        holder.textView.setText(myListData.getUsername());
    }


    @Override
    public int getItemCount() {
        return patientRegisterModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textView = itemView.findViewById(R.id.name);

        }
    }
}
