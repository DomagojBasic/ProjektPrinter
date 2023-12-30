package com.example.projektdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Oaplikaciji extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oaplikaciji);
        Button btnPovratak = findViewById(R.id.btn_Povratak);
        btnPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Oaplikaciji.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}