package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainDetail extends AppCompatActivity {
    private ArrayList<Product> productList;
    private ListView listViewProduct;
    private  Button btnPieChart, btnBarChart;
    private ArrayList<DataEntry> monthData;
    private String selectedYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);
        setControl();
        Intent intent = getIntent();
        selectedYear = intent.getStringExtra("selected_year");
        setEvent();
        // Sử dụng năm đã chọn để lấy dữ liệu
        if (selectedYear != null) {
            int currentYear = Integer.parseInt(selectedYear);
            getData(currentYear);
        } else {
            // Xử lý khi không nhận được năm
        }

        Button backButton = findViewById(R.id.btnback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc activity và quay lại trang trước
                finish();
            }
        });
    }

    private void setControl() {

        listViewProduct = findViewById(R.id.listproduct);
        btnPieChart = findViewById(R.id.btnpiechart);
        btnBarChart = findViewById(R.id.btnbarchart);
    }
    private void setEvent(){
        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDetail.this, piechart.class);
                intent.putExtra("year", selectedYear);
                startActivity(intent);
            }
        });
        btnBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDetail.this, barchart.class);
                intent.putExtra("year", selectedYear);
                startActivity(intent);
            }
        });
    }
    private void getData(int currentYear) {
        productList = new ArrayList<>();
        final int[] successfulRequests = {0};
        monthData = new ArrayList<>(); // Initialize the instance variable here

        for (int i = 1; i <= 12; i++) {
            final int index = i;

            HttpUtils.getMonthRecentYears(currentYear, i, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    try {
                        double revenue = Double.parseDouble(response);
                        productList.add(new Product(String.valueOf(index), String.valueOf(index), String.valueOf(revenue)));
                        successfulRequests[0]++; // Tăng biến đếm khi có yêu cầu thành công
                        monthData.add(new ValueDataEntry(String.valueOf(index), revenue));

                        // Nếu tất cả các yêu cầu đã hoàn thành, hiển thị dữ liệu lên ListView
                        if (index>=4) {
                            ProductAdapter adapter = new ProductAdapter(getApplicationContext(), productList);
                            listViewProduct.setAdapter(adapter);
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


}
