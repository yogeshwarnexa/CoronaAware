package com.example.coronaaware.ui.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coronaaware.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        PieChartView pieChartView = root.findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(500, Color.BLUE).setLabel("India: 500"));
        pieData.add(new SliceValue(5000, Color.GRAY).setLabel("China: 5000"));
        pieData.add(new SliceValue(60000, Color.RED).setLabel("Italy: 60000"));
        pieData.add(new SliceValue(1500, Color.MAGENTA).setLabel("USA: 1500"));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("COVID-19");
        pieChartData.setHasCenterCircle(true).setCenterText1("COVID-19").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);
        return root;
    }
}
