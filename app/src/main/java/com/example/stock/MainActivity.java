package com.example.stock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> items = new ArrayList<>();
    static ArrayList<Integer> itemsPrice = new ArrayList<>();
    static ArrayList<Integer> itemsQty = new ArrayList<>();
    static ArrayList<String> selectedItems = new ArrayList<>();
    static ArrayList<Integer> selectedItemsQty = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static SQLiteDatabase myDb;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.add_options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(R.id.cart == item.getItemId()) {
            Intent intent = new Intent(getApplicationContext(), cartActivity.class);
            startActivity(intent);
            return true;
        } else if(R.id.seller_account == item.getItemId()) {
            Intent intent = new Intent(getApplicationContext(), sellerActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        try {

            myDb = this.openOrCreateDatabase("Items", MODE_PRIVATE, null);
            myDb.execSQL("CREATE TABLE IF NOT EXISTS selectedItems (name VARCHAR, qty INT)");
            myDb.execSQL("CREATE TABLE IF NOT EXISTS items (name VARCHAR, qty INT, price INT)");
            myDb.execSQL("DELETE FROM items");
            myDb.execSQL("INSERT INTO items (name, qty, price) VALUES ('pens', 100, 5)");
            myDb.execSQL("INSERT INTO items (name, qty, price) VALUES ('shampoo', 50, 2)");
            myDb.execSQL("INSERT INTO items (name, qty, price) VALUES ('balls', 25, 30)");
            myDb.execSQL("INSERT INTO items (name, qty, price) VALUES ('paste', 200, 20)");
            myDb.execSQL("INSERT INTO items (name, qty, price) VALUES ('cake', 10, 20)");
            myDb.execSQL("INSERT INTO items (name, qty, price) VALUES ('juice', 30, 40)");
            myDb.execSQL("INSERT INTO items (name, qty, price) VALUES ('apples', 250, 80)");

            Cursor c = myDb.rawQuery("SELECT * FROM items", null);
            int nameIndex = c.getColumnIndex("name");
            int qtyIndex = c.getColumnIndex("qty");
            int priceIndex = c.getColumnIndex("price");
            c.moveToFirst();

            items.clear();
            itemsQty.clear();
            itemsPrice.clear();
            while (c != null) {
                items.add(c.getString(nameIndex));
                itemsQty.add(c.getInt(qtyIndex));
                itemsPrice.add(c.getInt(priceIndex));
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        items.add("pens");
        itemsQty.add(100);
        itemsPrice.add(5);
        items.add("shampoo");
        itemsQty.add(50);
        itemsPrice.add(2);
        items.add("balls");
        itemsQty.add(25);
        itemsPrice.add(30);
        items.add("paste");
        itemsQty.add(200);
        itemsPrice.add(50);
        items.add("cake");
        itemsQty.add(10);
        itemsPrice.add(20);
        items.add("bun");
        itemsQty.add(10);
        itemsPrice.add(10);
        items.add("juice");
        itemsQty.add(30);
        itemsPrice.add(40);
        items.add("apples");
        itemsQty.add(250);
        itemsPrice.add(80);  */

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, items);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), itemActivity.class);
                intent.putExtra("itemId",position);
                startActivity(intent);
            }
        });
    }
}