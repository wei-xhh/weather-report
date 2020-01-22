package com.example.weixhhweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SelectCityActivity extends AppCompatActivity {
    private ListView lvCityList;
    private ArrayAdapter<String> adapter;
    private String[] cityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        lvCityList = findViewById(R.id.lv_city_list);
        cityList = getResources().getStringArray(R.array.city_list);
        adapter=new ArrayAdapter<>(SelectCityActivity.this,android.R.layout.simple_list_item_1,cityList);
        lvCityList.setAdapter(adapter);

        lvCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cityName = cityList[i];
                Toast.makeText(SelectCityActivity.this,cityName,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("cityName",cityName);
                setResult(1002, intent);
                finish();
            }
        });

    }
}
