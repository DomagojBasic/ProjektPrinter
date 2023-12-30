package com.example.projektdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        TextView empty = findViewById(R.id.empty);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        ArrayList<Printeri> arrayList = new ArrayList<>();

        PrinterAdapter adapter = new PrinterAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);


        database.getReference().child("printeri").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
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

                PrinterAdapter adapter = new PrinterAdapter(MainActivity.this, arrayList);

                //adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);



                adapter.setOnItemClickListener(new PrinterAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Printeri printeri) {
                        // Implementacija za klik na stavku
                        Log.d("YourActivity", "Stavka je kliknuta: " + printeri.getTitle());
                    }

                    @Override
                    public void onBtnLDCClick(Printeri printeri, int position) {

                        // Implementacija za klik na btnLDC
                        Log.d("YourActivity", "BtnLDC je kliknut na poziciji: " + position);
                        // Ukloni element iz ArrayList
                        arrayList.remove(position);
                       //Dodavanje printera u printeriLDC
                        database.getReference().child("printeriLDC").push().setValue(printeri);
                        //brisanje printera
                        database.getReference().child("printeri").child(printeri.getKey()).removeValue(); // brisanje odabranog printera iz baze

                        //refresh stranice
                        refreshData();
                        // Obavijesti adapter o promjeni podataka
                        adapter.notifyItemRemoved(position);

                    }

                    @Override
                    public void onBtnServisClick(Printeri printeri, int position) {

                        // Implementacija za klik na btnLDC
                        Log.d("YourActivity", "BtnServis je kliknut na poziciji: " + position);
                        // Ukloni element iz ArrayList
                        arrayList.remove(position);
                        //Dodavanje printera u printeriServis
                        database.getReference().child("printeriServis").push().setValue(printeri);
                        //brisanje printera
                        database.getReference().child("printeri").child(printeri.getKey()).removeValue(); // brisanje odabranog printera iz baze

                        //refresh stranice
                        refreshData();
                        // Obavijesti adapter o promjeni podataka
                        adapter.notifyItemRemoved(position);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        Button btnPrinteriNaServisu = findViewById(R.id.btn_PritneriNaServisu);
        Button btnPrinteriNaLDC = findViewById(R.id.btn_PritneriLDC);
        Button btnMapa = findViewById(R.id.btn_mapa);

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Mapa.class);
                startActivity(intent);
            }
        });


        btnPrinteriNaLDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PrikazPrinteraLDC.class);
                startActivity(intent);
            }
        });



        btnPrinteriNaServisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PrikazPrinteraServis.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("proba", "proba");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    private void refreshData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ArrayList<Printeri> arrayList = new ArrayList<>();
        PrinterAdapter adapter = new PrinterAdapter(this, arrayList);
        database.getReference().child("printeri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Printeri> updatedList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Printeri printeri = dataSnapshot.getValue(Printeri.class);
                    Objects.requireNonNull(printeri).setKey(dataSnapshot.getKey());
                    updatedList.add(printeri);
                }

                // Ažurirajte RecyclerView s novim podacima
                adapter.updateData(updatedList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Prikazivanje poruke o greški ako je potrebno
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.unos_printera){
            Intent intent = new Intent(this,UnosPrintera.class);
            startActivity(intent);

        }else if(id == R.id.unos_objekata){
            Intent intent = new Intent(this,UnosObjekata.class);
            startActivity(intent);}

        return super.onOptionsItemSelected(item);
    }
}