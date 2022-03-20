package com.example.stock;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final EditText productName = findViewById(R.id.deleteProductName);
        final EditText productQty = findViewById(R.id.editTextProductQty);
        final EditText productPrice = findViewById(R.id.editTextPrice);
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName = productName.getText().toString();
                String pQty = productQty.getText().toString();
                String pPrice = productPrice.getText().toString();
                if((!pName.isEmpty() && !pQty.isEmpty()) && !pPrice.isEmpty() ) {

                    SQLiteStatement statement = MainActivity.myDb.compileStatement("INSERT INTO items (name, qty, price) VALUES (?, ?, ?)");
                    statement.bindString(1, pName);
                    statement.bindString(2, pQty);
                    statement.bindString(3, pPrice);
                    statement.execute();

                    MainActivity.items.add(pName);
                    MainActivity.itemsQty.add(Integer.parseInt(pQty));
                    MainActivity.itemsPrice.add(Integer.parseInt(pPrice));
                    Toast.makeText(addItemActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(addItemActivity.this, "Fill all 3 details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}