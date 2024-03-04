package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class piechart extends AppCompatActivity {
    private List<DataEntry> monthData;
private TextView tvYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        tvYear = findViewById(R.id.year);

        Pie pie = AnyChart.pie();
        // Retrieve data passed from MainDetail activity
        Intent intent = getIntent();
        String year = intent.getStringExtra("year");
        tvYear.setText(year);
        if (year != null) {
            int currentYear = Integer.parseInt(year);
            getData(currentYear);
        } else {
        }
    }

    private void getData(int currentYear) {
        monthData = new ArrayList<>(); // Initialize the instance variable here

        for (int i = 1; i <= 12; i++) {
            final int index = i;

            HttpUtils.getMonthRecentYears(currentYear, i, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    try {
                        double revenue = Double.parseDouble(response);
                        Log.d("API_CALL"+revenue, "API call successful");
                        monthData.add(new ValueDataEntry(String.valueOf(index), revenue));
                        if (monthData.size() == 12) {
                            drawPieChart();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    Log.d("API_CALL", "API call successful");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("API_CALL", "API call fail");
                    error.printStackTrace();
                }
            });
        }
    }
    private void drawPieChart() {
        Pie pie = AnyChart.pie();
        pie.data(monthData);
        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }
}
