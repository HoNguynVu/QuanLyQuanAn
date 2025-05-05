package com.example.doan.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.R;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        EditText edtName = findViewById(R.id.edtName);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtbirth = findViewById(R.id.edtbirth);

        Intent intent = getIntent();
        String Name = intent.getStringExtra("username");
        String Email = intent.getStringExtra("email");
        String Phone = intent.getStringExtra("phone");
        String Birth = intent.getStringExtra("dob");

        edtName.setText(Name);
        edtEmail.setText(Email);
        edtPhone.setText(Phone);
        edtbirth.setText(Birth);
    }
}