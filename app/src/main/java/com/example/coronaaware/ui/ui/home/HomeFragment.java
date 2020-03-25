package com.example.coronaaware.ui.ui.home;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        new GetCovid19().execute();
        return root;
    }


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
                    CovidTrack covidTrack = new CovidTrack();
                    covidTrack.setConfirmed(latest.getInt("confirmed"));
                    covidTrack.setDeaths(latest.getInt("deaths"));
                    covidTrack.setRecovered(latest.getInt("recovered"));
                    covidTrack.setCountouryName("world wide");
                    covidTrack.setConutryCode("WW");
                    covidTrackArrayList.add(covidTrack);


                    List<SliceValue> pieData = new ArrayList<>();
                    pieData.add(new SliceValue(latest.getInt("confirmed"), Color.BLUE).setLabel("confirmed : " + latest.getInt("confirmed")));
                    pieData.add(new SliceValue(latest.getInt("recovered"), Color.GREEN).setLabel("recovered : " + latest.getInt("recovered")));
                    pieData.add(new SliceValue(latest.getInt("deaths"), Color.RED).setLabel("deaths :" + latest.getInt("deaths")));

                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartData.setHasLabels(true);
                    pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                    pieChartData.setHasCenterCircle(true).setCenterText1("COVID-19 World wide").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                    pieChartView.setPieChartData(pieChartData);
                    titleHome.setText("COVID-19 World wide");
                    titleConfirmed.setText("Confirmed : " + latest.getInt("confirmed"));
                    titleConfirmed.setTextColor(Color.BLUE);
                    titleRecovered.setText("Recovered : " + latest.getInt("recovered"));
                    titleRecovered.setTextColor(Color.GREEN);
                    titleDeath.setText("Deaths : " + latest.getInt("deaths"));
                    titleDeath.setTextColor(Color.RED);
                    JSONObject confirmed = jsonObject.getJSONObject("confirmed");
                    JSONArray jsonArray = confirmed.getJSONArray("locations");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CovidTrack covidTrack1 = new CovidTrack();
                        covidTrack1.setConutryCode(jsonObject1.getString("country_code"));
                        covidTrack1.setCountouryName(jsonObject1.getString("country"));
                        covidTrack1.setConfirmed(jsonObject1.getInt("latest"));
                        JSONObject location = jsonObject1.getJSONObject("coordinates");
                        covidTrack1.setLat(location.getString("lat"));
                        covidTrack1.setLng(location.getString("long"));

                        JSONObject deaths = jsonObject.getJSONObject("deaths");
                        JSONArray deathsJson = deaths.getJSONArray("locations");
                        JSONObject deathsobject = deathsJson.getJSONObject(i);
                        covidTrack1.setDeaths(deathsobject.getInt("latest"));

                        JSONObject recovered = jsonObject.getJSONObject("recovered");
                        JSONArray recoveredJson = recovered.getJSONArray("locations");
                        JSONObject recoveredobject = recoveredJson.getJSONObject(i);
                        covidTrack1.setDeaths(recoveredobject.getInt("latest"));
                        covidTrackArrayList.add(covidTrack1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();
            }
        }

    }
}
