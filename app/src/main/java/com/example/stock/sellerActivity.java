package com.example.stock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        final EditText editTextUserName = findViewById(R.id.editTextUserName);
        final EditText editTextPwd = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextUserName.getText().toString().isEmpty() && editTextPwd.getText().toString().equals("12345")) {
                    Intent intent = new Intent(getApplicationContext(), sellerActivity2.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(sellerActivity.this, "username/password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}