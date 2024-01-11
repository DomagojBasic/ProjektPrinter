package com.example.projektdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PrikazPrinteraLDC extends AppCompatActivity {

    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prikaz_printera_ldc);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        TextView empty = findViewById(R.id.empty);
        RecyclerView recyclerView = findViewById(R.id.recyclerLDC);
        ArrayList<Printeri> arrayList = new ArrayList<>();
        ArrayList<Objekti> arrayListObjekti = new ArrayList<>(); // za objekte

        progressBar2 = findViewById(R.id.progressBar2);

        PrinterAdapter adapter = new PrinterAdapter(this, arrayList);

        database.getReference().child("printeri").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Printeri printeri = dataSnapshot.getValue(Printeri.class);

                    if (printeri != null && printeri.isCheckBoxLDC()) {
                        Objects.requireNonNull(printeri).setKey(dataSnapshot.getKey());
                        arrayList.add(printeri);

                        PrinterAdapter adapter = new PrinterAdapter(PrikazPrinteraLDC.this, arrayList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                }

                if (arrayList.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {

                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                adapter.setOnItemClickListener(new PrinterAdapter.OnItemClickListener() {

                    @Override
                    public void onClick(Printeri printeri) {

                    }

                    @Override
                    public void onCheckLDC(Printeri printeri, int position) {

                    }
                });


                Button btnPovratak = findViewById(R.id.btn_Povratak);
                btnPovratak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PrikazPrinteraLDC.this, MainActivity.class);
                        startActivity(intent);
                    }

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}