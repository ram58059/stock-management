package com.example.stock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class itemActivity extends AppCompatActivity {
    int itemId;
    EditText editText;
    Button confirmButton,addToCartButton,buyButton;
    int qtySelected;
    boolean ensureConfirmButtonPressed = false;
    boolean ensureCartButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId",0);

        editText = findViewById(R.id.editTextNumber);
        confirmButton = findViewById(R.id.qtyConfirm);
        addToCartButton = findViewById(R.id.addToCartButton);
        buyButton = findViewById(R.id.BuyButton);
        TextView itemTextView = findViewById(R.id.itemTextView);
        final TextView costTextView = findViewById(R.id.costTextView);
        final TextView textView = findViewById(R.id.stockTextView);

        itemTextView.setText(MainActivity.items.get(itemId) + "(Price: " + MainActivity.itemsPrice.get(itemId) + ")");
        textView.setText("In-stock: " + String.valueOf(MainActivity.itemsQty.get(itemId)));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                costTextView.setText("Total Cost: " + String.valueOf(Integer.parseInt(String.valueOf(s)) * MainActivity.itemsPrice.get(itemId)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensureConfirmButtonPressed = true;
                qtySelected = Integer.parseInt(editText.getText().toString());
                if(qtySelected > MainActivity.itemsQty.get(itemId)) {
                    Toast.makeText(getApplicationContext(), "Not available in stock", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteStatement statement = MainActivity.myDb.compileStatement("UPDATE items SET qty = (?) WHERE name = '"+MainActivity.items.get(itemId) +"'");
                    statement.bindString(1,String.valueOf(MainActivity.itemsQty.get(itemId) - qtySelected));
                    statement.execute();

                    MainActivity.itemsQty.set(itemId, MainActivity.itemsQty.get(itemId) - qtySelected);
                    Toast.makeText(getApplicationContext(), String.valueOf(MainActivity.itemsQty.get(itemId)), Toast.LENGTH_SHORT).show();
                    textView.setText("In-stock: " + String.valueOf(MainActivity.itemsQty.get(itemId)));
                }
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensureCartButtonPressed = true;
                qtySelected = Integer.parseInt(editText.getText().toString());
                if(qtySelected > MainActivity.itemsQty.get(itemId)) {
                    Toast.makeText(getApplicationContext(), "Not available in stock", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteStatement statement = MainActivity.myDb.compileStatement("INSERT INTO selectedItems (name, qty) VALUES (?, ?)");
                    statement.bindString(1, MainActivity.items.get(itemId));
                    statement.bindString(2, String.valueOf(editText.getText()));
                    statement.execute();

                    statement = MainActivity.myDb.compileStatement("UPDATE items SET qty = (?) WHERE name = '"+MainActivity.items.get(itemId) +"'");
                    statement.bindString(1,String.valueOf(MainActivity.itemsQty.get(itemId) - qtySelected));
                    statement.execute();

//                    MainActivity.selectedItems.add(MainActivity.items.get(itemId));
//                    MainActivity.selectedItemsQty.add(Integer.parseInt(editText.getText().toString()));
                    MainActivity.itemsQty.set(itemId, MainActivity.itemsQty.get(itemId) - Integer.parseInt(editText.getText().toString()));
                    textView.setText("In-stock: " + String.valueOf(MainActivity.itemsQty.get(itemId)));
                    Toast.makeText(itemActivity.this, "Added to your cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ensureCartButtonPressed || ensureConfirmButtonPressed) {
                    Toast.makeText(itemActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(itemActivity.this, "Press confirm button", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}