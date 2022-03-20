package com.example.stock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class cartActivity extends AppCompatActivity {

    public int calcTotalCost() {
        int total=0;
        for(String item: MainActivity.selectedItems) {
            int pos = MainActivity.items.indexOf(item);
            int pos2 = MainActivity.selectedItems.indexOf(item);
            total += (MainActivity.itemsPrice.get(pos) * MainActivity.selectedItemsQty.get(pos2));
        }
        return total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ListView itemsListView = findViewById(R.id.selectedItemListView);
        final TextView itemsTextView = findViewById(R.id.itemsTextView);
        final TextView costTextView = findViewById(R.id.costTextView);
        Button placeOrderButton = findViewById(R.id.placeOrderButton);

        MainActivity.selectedItems.clear();
        MainActivity.selectedItemsQty.clear();
        try {
            Cursor c = MainActivity.myDb.rawQuery("SELECT * FROM selectedItems", null);
            int nameIndex = c.getColumnIndex("name");
            int qtyIndex = c.getColumnIndex("qty");
            c.moveToFirst();

            while (c != null) {
                MainActivity.selectedItems.add(c.getString(nameIndex));
                MainActivity.selectedItemsQty.add(Integer.parseInt(c.getString(qtyIndex)));
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MainActivity.selectedItems);
        itemsListView.setAdapter(arrayAdapter);
        itemsTextView.setText("Number of items: " + String.valueOf(MainActivity.selectedItems.size()));
        costTextView.setText("Total Cost: " + String.valueOf(calcTotalCost()));


        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(cartActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
            }
        });

        itemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(cartActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure")
                        .setMessage("Do you want to remove " + MainActivity.selectedItems.get(position) + " from cart")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pos = MainActivity.items.indexOf(MainActivity.selectedItems.get(position));

                                SQLiteStatement statement = MainActivity.myDb.compileStatement("UPDATE items SET qty = (?) WHERE name = '"+MainActivity.items.get(pos) +"'");
                                statement.bindString(1,String.valueOf(MainActivity.itemsQty.get(pos) + MainActivity.selectedItemsQty.get(position)));
                                statement.execute();

                                MainActivity.myDb.execSQL("DELETE FROM selectedItems WHERE name = '" + MainActivity.selectedItems.get(position) + "'");

                                MainActivity.itemsQty.set(pos, MainActivity.itemsQty.get(pos) + MainActivity.selectedItemsQty.get(position));
                                MainActivity.selectedItems.remove(position);
                                MainActivity.selectedItemsQty.remove(position);
                                itemsTextView.setText("Number of items: " + String.valueOf(MainActivity.selectedItems.size()));
                                costTextView.setText("Total Cost: " + String.valueOf(calcTotalCost()));
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}