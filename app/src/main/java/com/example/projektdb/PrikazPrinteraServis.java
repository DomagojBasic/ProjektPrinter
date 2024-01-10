package com.example.projektdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PrikazPrinteraServis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prikaz_printera_servis);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        TextView empty = findViewById(R.id.empty);
        RecyclerView recyclerView = findViewById(R.id.recycler);


        ArrayList<Printeri> arrayList = new ArrayList<>();
        PrinterAdapterServis adapterServis = new PrinterAdapterServis(PrikazPrinteraServis.this, arrayList);

        database.getReference().child("printeriServis").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Printeri printeri = dataSnapshot.getValue(Printeri.class);
                    Objects.requireNonNull(printeri).setKey(dataSnapshot.getKey());
                    arrayList.add(printeri);
                }

                if (arrayList.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                recyclerView.setAdapter(adapterServis);

                adapterServis.setOnItemClickListener(new PrinterAdapterServis.OnItemClickListener() {
                    @Override
                    public void onClick(Printeri printeri) {

                    }

                    @Override
                    public void onBtnLDCClick(Printeri printeri, int position) {
                        // Ukloni element iz ArrayList
                        arrayList.remove(position);
                        //Dodavanje printera u printeriLDC
                        database.getReference().child("printeriLDC").push().setValue(printeri);
                        //brisanje printera
                        database.getReference().child("printeriServis").child(printeri.getKey()).removeValue(); // brisanje odabranog printera iz baze

                        // Obavijesti adapter o promjeni podataka
                        adapterServis.notifyItemRemoved(position);

                      recreate();

                    }

                    @Override
                    public void onBtnInformatikaClick(Printeri printeri, int position) {
                        // Ukloni element iz ArrayList
                        arrayList.remove(position);
                        //Dodavanje printera u printeriLDC
                        database.getReference().child("printeri").push().setValue(printeri);
                        //brisanje printera
                        database.getReference().child("printeriServis").child(printeri.getKey()).removeValue(); // brisanje odabranog printera iz baze

                        // Obavijesti adapter o promjeni podataka
                        adapterServis.notifyItemRemoved(position);

                        recreate();


                    }

                    @Override
                    public void onBtnServisClick(Printeri printeri, int position) {

                    }
                });


                Button btnPovratak = findViewById(R.id.btn_Povratak);

                btnPovratak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PrikazPrinteraServis.this, MainActivity.class);
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
