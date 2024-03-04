package com.example.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.time.Month;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Product> productList;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ListView listViewProduct;

private MenuItem homeItem, monthItem, yearItem, dayItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các thành phần giao diện
        setControl();
        setEvent();

        getData();


    }

    private void getData() {
        productList = new ArrayList<>();
        int currentYear = 2024;
        final int[] successfulRequests = {0};

        for (int i = 0; i < 5; i++) {
            final int year = currentYear - i;
            final int index = i;

            HttpUtils.getFiveRecentYears(year, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    try {
                        double revenue = Double.parseDouble(response);
                        productList.add(new Product(String.valueOf(index), String.valueOf(year), String.valueOf(revenue)));
                        successfulRequests[0]++; // Tăng biến đếm khi có yêu cầu thành công

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


    private void setControl() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar2);
        listViewProduct = findViewById(R.id.listproduct);

    }


    private void setEvent() {

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product clickedProduct = productList.get(position);
                String year = productList.get(position).getName();

                // Chuyển sang MainDetail và truyền năm
                Intent intent = new Intent(MainActivity.this, MainDetail.class);
                intent.putExtra("selected_year", year);
                startActivity(intent);
            }
        });
    }


}
