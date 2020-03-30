package com.example.coronaaware.ui.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coronaaware.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class StateChartReport extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    PieChartView pieChartView;
    TextView titleName, StateConfirmedCasesIndian, StateConfirmedCasesForeign, stateDischarged, stateDeaths;
    Integer CasesIndian, CasesForeign, Discharged, Deaths;
    String stateName;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_chart_report);
        Intent intent = getIntent();
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (intent.getExtras() != null) {
            stateName = intent.getStringExtra("StateName");
            setActionBar(stateName);
            CasesIndian = intent.getIntExtra("Indian", 0);
            CasesForeign = intent.getIntExtra("Foreign", 0);
            Discharged = intent.getIntExtra("discharged", 0);
            Deaths = intent.getIntExtra("deaths", 0);
        }

        titleName = findViewById(R.id.titleName);
        StateConfirmedCasesIndian = findViewById(R.id.StateConfirmedCasesIndian);
        StateConfirmedCasesForeign = findViewById(R.id.StateConfirmedCasesForeign);
        stateDischarged = findViewById(R.id.stateDischarged);
        stateDeaths = findViewById(R.id.stateDeaths);
        pieChartView = findViewById(R.id.StateChart);
        setupPieChart();

    }

    private void setActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    private void setupPieChart() {
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(CasesIndian, Color.BLUE));

        pieData.add(new SliceValue(CasesForeign, Color.MAGENTA));

        pieData.add(new SliceValue(Discharged, Color.GREEN));

        pieData.add(new SliceValue(Deaths, Color.RED));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabels(true).setValueLabelTextSize(15);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);
        titleName.setText("Corona virus outbreak in " + stateName);

        StateConfirmedCasesIndian.setText("Confirmed Cases Indian:" + CasesIndian);
        StateConfirmedCasesIndian.setTextColor(Color.BLUE);

        StateConfirmedCasesForeign.setText("Confirmed Cases Foreign:" + CasesForeign);
        StateConfirmedCasesForeign.setTextColor(Color.MAGENTA);

        stateDischarged.setText("Discharged:" + Discharged);
        stateDischarged.setTextColor(Color.GREEN);

        stateDeaths.setText("Deaths:" + Deaths);
        stateDeaths.setTextColor(Color.RED);


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
