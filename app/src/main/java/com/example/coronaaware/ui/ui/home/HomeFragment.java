package com.example.coronaaware.ui.ui.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coronaaware.R;
import com.example.coronaaware.model.CovidTrack;
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

    ArrayList<CovidTrack> covidTrackArrayList;

    PieChartView pieChartView;

    ProgressDialog progressDialog;

    TextView titleConfirmed, titleRecovered, titleDeath, titleHome;

    EditText searchTextview;

    ImageView searchCountry;

    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences pref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        pieChartView = root.findViewById(R.id.chart);
        covidTrackArrayList = new ArrayList<>();
        titleConfirmed = root.findViewById(R.id.titleConfirmed);
        titleRecovered = root.findViewById(R.id.titleRecovered);
        titleDeath = root.findViewById(R.id.titleDeath);
        titleHome = root.findViewById(R.id.titleHome);
        searchTextview = root.findViewById(R.id.searchText);
        searchCountry = root.findViewById(R.id.searchCountry);

        pref = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        new GetCovid19().execute();
        searchCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = searchTextview.getText().toString();
                if (countryCode.isEmpty()) {
                    searchTextview.setError("Required Country code");
                    searchTextview.requestFocus();
                    return;
                } else {
                    new GetCovid19Country().execute(countryCode);
                }
            }
        });
        return root;
    }

    private void setPieChart() {
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(pref.getInt(getString(R.string.ConfirmedCasesValue), 0), Color.BLUE));
        pieData.add(new SliceValue(pref.getInt(getString(R.string.RecoveredCasesValue), 0), Color.GREEN));
        pieData.add(new SliceValue(pref.getInt(getString(R.string.DeathCasesValue), 0), Color.RED));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabels(true).setValueLabelTextSize(15);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);
        titleHome.setText(pref.getString(getString(R.string.covid19TitleValue), getString(R.string.covid19TitleValue) + "World wide"));
        titleConfirmed.setText(getString(R.string.ConfirmedCases) + ":" + pref.getInt(getString(R.string.ConfirmedCasesValue), 0));
        titleConfirmed.setTextColor(Color.BLUE);
        titleRecovered.setText(getString(R.string.RecoveredCases) + ":" + pref.getInt(getString(R.string.RecoveredCasesValue), 0));
        titleRecovered.setTextColor(Color.GREEN);
        titleDeath.setText(getString(R.string.DeathCases) + ":" + pref.getInt(getString(R.string.DeathCasesValue), 0));
        titleDeath.setTextColor(Color.RED);
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
            Log.e("Code", params[0]);
            String code = params[0];
            String countryUrl = null;
            if (code.length() == 2) {
                countryUrl = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country_code=" + code;
            } else {
                countryUrl = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country=" + code;
            }

            String response;

            try {
                Log.e("URl", countryUrl);
                Request request = new Request.Builder().url(countryUrl).get().build();
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
                    CovidTrack covidTrack = new CovidTrack();
                    covidTrack.setConfirmed(latest.getInt("confirmed"));
                    covidTrack.setDeaths(latest.getInt("deaths"));
                    covidTrack.setRecovered(latest.getInt("recovered"));

                    JSONArray jsonArray = jsonObject.getJSONArray("locations");
                    String country = null, countryCode = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        country = jsonObject1.getString("country");
                        countryCode = jsonObject1.getString("country_code");
                    }


                    covidTrack.setCountouryName(country);
                    covidTrack.setConutryCode(countryCode);
                    String tiltle = getString(R.string.covid19Title) + "-" + country;

                    pref.edit().putString(getString(R.string.covid19TitleValue), tiltle).apply();
                    pref.edit().putInt(getString(R.string.ConfirmedCasesValue), latest.getInt("confirmed")).apply();
                    pref.edit().putInt(getString(R.string.RecoveredCasesValue), latest.getInt("recovered")).apply();
                    pref.edit().putInt(getString(R.string.DeathCasesValue), latest.getInt("deaths")).apply();
                    setPieChart();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();
            }
        }

    }
}
