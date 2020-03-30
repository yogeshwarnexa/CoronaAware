package com.example.coronaaware.ui.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaaware.R;
import com.example.coronaaware.model.CovidTrackState;
import com.example.coronaaware.ui.ui.home.StateChartReport;

import java.util.ArrayList;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    Context mContext;
    private ArrayList<CovidTrackState> moviesList;

    public StateAdapter(ArrayList<CovidTrackState> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.state_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CovidTrackState movie = moviesList.get(position);
        holder.title.setText(movie.getStateName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StateChartReport.class);
                intent.putExtra("StateName", movie.getStateName());
                intent.putExtra("Indian", Integer.parseInt(movie.getConfirmedCasesIndian()));
                intent.putExtra("Foreign", Integer.parseInt(movie.getConfirmedCasesForeign()));
                intent.putExtra("discharged", Integer.parseInt(movie.getDischarged()));
                intent.putExtra("deaths", Integer.parseInt(movie.getDeaths()));
                mContext.startActivity(intent);
            }
        });


      /*  holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());*/
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }
}