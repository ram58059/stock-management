package com.example.stock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class showItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        ListView showListView = findViewById(R.id.showListView);
        showListView.setAdapter(MainActivity.arrayAdapter);
    }
}