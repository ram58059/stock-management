package com.example.stock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class deleteItemActivity extends AppCompatActivity {

    EditText delProdName, changeProdName, changeProdQty, changeProdPrice;
    Button delButton, changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);

        delProdName = findViewById(R.id.deleteProductName);
        changeProdName = findViewById(R.id.changeProductName);
        changeProdQty = findViewById(R.id.changeProductQty);
        changeProdPrice = findViewById(R.id.changeProductPrice);

        delButton = findViewById(R.id.deleteButton);
        changeButton = findViewById(R.id.changeButton);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!delProdName.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(deleteItemActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete " + delProdName.getText().toString())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int pos = MainActivity.items.indexOf(delProdName.getText().toString());

                                    MainActivity.myDb.execSQL("DELETE FROM items WHERE name = '" + delProdName.getText().toString() + "'");
                                    if(MainActivity.selectedItems.contains(delProdName.getText().toString())) {
                                        MainActivity.myDb.execSQL("DELETE FROM selectedItems WHERE name = '" + delProdName.getText().toString() + "'");
                                        int pos2 = MainActivity.selectedItems.indexOf(delProdName.getText().toString());
                                        MainActivity.selectedItems.remove(pos2);
                                        MainActivity.selectedItemsQty.remove(pos2);
                                    }

                                    MainActivity.items.remove(delProdName.getText().toString());
                                    MainActivity.itemsQty.remove(pos);
                                    MainActivity.itemsPrice.remove(pos);

                                    MainActivity.arrayAdapter.notifyDataSetChanged();
                                    Toast.makeText(deleteItemActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    Toast.makeText(deleteItemActivity.this, "Enter product name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pName = changeProdName.getText().toString();
                final String pQty = changeProdQty.getText().toString();
                final String pPrice = changeProdPrice.getText().toString();

                if((!pName.isEmpty() && !pQty.isEmpty()) && !pPrice.isEmpty()) {
                    new AlertDialog.Builder(deleteItemActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to change the details?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int pos = MainActivity.items.indexOf(pName);

                                    SQLiteStatement statement = MainActivity.myDb.compileStatement("UPDATE items SET qty = (?), price = (?) WHERE name = '"
                                                    + pName + "'");
                                    statement.bindString(1, pQty);
                                    statement.bindString(2, pPrice);
                                    statement.execute();

                                    MainActivity.itemsQty.set(pos, Integer.parseInt(pQty));
                                    MainActivity.itemsPrice.set(pos, Integer.parseInt(pPrice));
                                    Toast.makeText(deleteItemActivity.this, "Details Changed", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    Toast.makeText(deleteItemActivity.this, "Fill all 3 details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}