package com.example.coronaaware.ui.ui.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
                    List<SliceValue> pieData = new ArrayList<>();
                    pieData.add(new SliceValue(latest.getInt("confirmed"), Color.BLUE).setLabel("confirmed : " + latest.getInt("confirmed")));
                    pieData.add(new SliceValue(latest.getInt("recovered"), Color.GREEN).setLabel("recovered : " + latest.getInt("recovered")));
                    pieData.add(new SliceValue(latest.getInt("deaths"), Color.RED).setLabel("deaths :" + latest.getInt("deaths")));
                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartData.setHasLabels(true);
                    pieChartData.setHasLabels(true).setValueLabelTextSize(15);
                    pieChartData.setHasCenterCircle(true).setCenterText1("COVID-19 World wide").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0097A7"));
                    pieChartView.setPieChartData(pieChartData);
                    titleHome.setText("COVID-19 World wide");
                    titleConfirmed.setText("Confirmed : " + latest.getInt("confirmed"));
                    titleConfirmed.setTextColor(Color.BLUE);
                    titleRecovered.setText("Recovered : " + latest.getInt("recovered"));
                    titleRecovered.setTextColor(Color.GREEN);
                    titleDeath.setText("Deaths : " + latest.getInt("deaths"));
                    titleDeath.setTextColor(Color.RED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();
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
            String countryUrl = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country_code=" + params[0];
            String response;

            try {
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
                    String tiltle = "COVID-19 " + country;

                    List<SliceValue> pieData = new ArrayList<>();
                    pieData.add(new SliceValue(latest.getInt("confirmed"), Color.BLUE).setLabel("confirmed : " + latest.getInt("confirmed")));
                    pieData.add(new SliceValue(latest.getInt("recovered"), Color.GREEN).setLabel("recovered : " + latest.getInt("recovered")));
                    pieData.add(new SliceValue(latest.getInt("deaths"), Color.RED).setLabel("deaths :" + latest.getInt("deaths")));

                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartData.setHasLabels(true);
                    pieChartData.setHasLabels(true).setValueLabelTextSize(15);
                    pieChartData.setHasCenterCircle(true).setCenterText1(tiltle).setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0097A7"));
                    pieChartView.setPieChartData(pieChartData);
                    titleHome.setText(tiltle);
                    titleConfirmed.setText("Confirmed : " + latest.getInt("confirmed"));
                    titleConfirmed.setTextColor(Color.BLUE);
                    titleRecovered.setText("Recovered : " + latest.getInt("recovered"));
                    titleRecovered.setTextColor(Color.GREEN);
                    titleDeath.setText("Deaths : " + latest.getInt("deaths"));
                    titleDeath.setTextColor(Color.RED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();
            }
        }

    }
}
