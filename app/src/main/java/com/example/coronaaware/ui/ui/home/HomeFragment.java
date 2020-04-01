package com.example.coronaaware.ui.ui.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coronaaware.R;
import com.example.coronaaware.model.CovidTrackState;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeFragment extends Fragment {

    String url = "https://coronavirus-tracker-api.herokuapp.com/all";

    public static ArrayList<CovidTrackState> covidTrackArrayList;

    PieChartView pieChartView;

    ProgressDialog progressDialog;

    TextView titleHome, titleTotal, titleConfirmedCasesIndian, titleConfirmedCasesForeign, titleDischarged, titleDeaths;

    Button buttonState;

    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences pref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        pieChartView = root.findViewById(R.id.chart);
        covidTrackArrayList = new ArrayList<>();
        titleTotal = root.findViewById(R.id.titleTotal);
        titleConfirmedCasesIndian = root.findViewById(R.id.confirmedCasesIndian);
        titleConfirmedCasesForeign = root.findViewById(R.id.confirmedCasesForeign);
        titleDischarged = root.findViewById(R.id.discharged);
        titleDeaths = root.findViewById(R.id.deaths);
        titleHome = root.findViewById(R.id.titleHome);
        buttonState = root.findViewById(R.id.buttonState);

        buttonState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (covidTrackArrayList.size() > 0) {
                    Intent intent = new Intent(getActivity(), StateActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        pref = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        new GetCovid19Country().execute("IN");
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setPieChart() {
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(pref.getInt(getString(R.string.total), 0), Color.BLUE));

        pieData.add(new SliceValue(pref.getInt(getString(R.string.confirmedCasesIndian), 0), Color.MAGENTA));

        pieData.add(new SliceValue(pref.getInt(getString(R.string.confirmedCasesForeign), 0), Color.GREEN));

        pieData.add(new SliceValue(pref.getInt(getString(R.string.discharged), 0), Color.DKGRAY));

        pieData.add(new SliceValue(pref.getInt(getString(R.string.deaths), 0), Color.RED));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabels(true).setValueLabelTextSize(15);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);
        titleHome.setText("Corona virus outbreak in India");
        titleTotal.setText("Total :" + pref.getInt(getString(R.string.total), 0));
        titleTotal.setTextColor(Color.BLUE);

        titleConfirmedCasesIndian.setText("Confirmed Cases Indian:" + pref.getInt(getString(R.string.confirmedCasesIndian), 0));
        titleConfirmedCasesIndian.setTextColor(Color.MAGENTA);

        titleConfirmedCasesForeign.setText("Confirmed Cases Foreign:" + pref.getInt(getString(R.string.confirmedCasesForeign), 0));
        titleConfirmedCasesForeign.setTextColor(Color.GREEN);

        titleDischarged.setText("Recovered:" + pref.getInt(getString(R.string.discharged), 0));
        titleDischarged.setTextColor(Color.DKGRAY);

        titleDeaths.setText("Deaths:" + pref.getInt(getString(R.string.deaths), 0));
        titleDeaths.setTextColor(Color.RED);
    }

    @SuppressLint("StaticFieldLeak")
    private class GetCovid19 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "", "Please Wait!");

        }

        @Override
        protected String doInBackground(String... params) {

            String response;

            try {
                Request request = new Request.Builder().url(url).get().build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Response response1 = okHttpClient.newCall(request).execute();
                if (!response1.isSuccessful()) {
                    return null;
                }
                ResponseBody body = response1.body();
                response = body.string();
                return response;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject latest = jsonObject.getJSONObject("latest");
                    pref.edit().putString(getString(R.string.covid19TitleValue), getString(R.string.covid19Title) + "-" + "World wide").apply();
                    pref.edit().putInt(getString(R.string.ConfirmedCasesValue), latest.getInt("confirmed")).apply();
                    pref.edit().putInt(getString(R.string.RecoveredCasesValue), latest.getInt("recovered")).apply();
                    pref.edit().putInt(getString(R.string.DeathCasesValue), latest.getInt("deaths")).apply();
                    setPieChart();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                setPieChart();

            }
        }

    }

    private class GetCovid19Country extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "", "Please Wait!");

        }

        @Override
        protected String doInBackground(String... params) {
            String response;

            try {
                Log.e("URl", getString(R.string.API));
                Request request = new Request.Builder().url(getString(R.string.API)).get().build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Response response1 = okHttpClient.newCall(request).execute();
                if (!response1.isSuccessful()) {
                    return null;
                }
                ResponseBody body = response1.body();
                response = body.string();
                return response;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject latest = jsonObject.getJSONObject("data");
                    JSONObject summary = latest.getJSONObject("summary");


                    pref.edit().putString(getString(R.string.covid19TitleValue), "Covid-19 in India").apply();
                    pref.edit().putInt(getString(R.string.total), summary.getInt("total")).apply();
                    pref.edit().putInt(getString(R.string.confirmedCasesIndian), summary.getInt("confirmedCasesIndian")).apply();
                    pref.edit().putInt(getString(R.string.confirmedCasesForeign), summary.getInt("confirmedCasesForeign")).apply();
                    pref.edit().putInt(getString(R.string.discharged), summary.getInt("discharged")).apply();
                    pref.edit().putInt(getString(R.string.deaths), summary.getInt("deaths")).apply();
                    JSONArray jsonArray = latest.getJSONArray("regional");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CovidTrackState covidTrackState = new CovidTrackState(jsonObject1.getString("loc"),
                                jsonObject1.getString("confirmedCasesIndian"), jsonObject1.getString("confirmedCasesForeign"),
                                jsonObject1.getString("discharged"), jsonObject1.getString("deaths"));
                        covidTrackArrayList.add(covidTrackState);

                    }
                    setPieChart();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();
                setPieChart();
            }
        }

    }
}
