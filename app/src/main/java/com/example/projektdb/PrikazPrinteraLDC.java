package com.example.projektdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

        database.getReference().child("objekti").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()) {
                    arrayListObjekti.add(d.getValue(Objekti.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        database.getReference().child("printeri").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Printeri printeri = dataSnapshot.getValue(Printeri.class);

                    if (printeri != null && printeri.isCheckBoxLDC()) {
                        Objects.requireNonNull(printeri).setKey(dataSnapshot.getKey());
                        arrayList.add(printeri);

                        PrinterAdapter adapter = new PrinterAdapter(PrikazPrinteraLDC.this, arrayList,arrayListObjekti);
                        recyclerView.setAdapter(adapter);
                       // adapter.notifyDataSetChanged();

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

                    @Override
                    public void onCheckServis(Printeri printeri, int position) {
                    }

                    @Override
                    public void onCheckInformatika(Printeri printeri, int position) {

                        View view = LayoutInflater.from(PrikazPrinteraLDC.this).inflate(R.layout.add_inf_serv_ldc_printeri_dialog, null);
                        TextInputLayout titleLayout, contentLayout;
                        TextInputEditText titleET, contentET;
                        CheckBox checkBoxInformatika, checkBoxLDC, checkBoxServis;

                        checkBoxInformatika = view.findViewById(R.id.checkBoxInformatika);
                        checkBoxLDC = view.findViewById(R.id.checkBoxLDC);
                        checkBoxServis = view.findViewById(R.id.checkBoxServis);

                        titleET = view.findViewById(R.id.titleET);
                        contentET = view.findViewById(R.id.contentET);
                        titleLayout = view.findViewById(R.id.titleLayout);
                        contentLayout = view.findViewById(R.id.contentLayout);

                        titleET.setText(printeri.getTitle());
                        contentET.setText(printeri.getContent());

                        Printeri printeri1 = new Printeri();
                        printeri1.setTitle(titleET.getText().toString());
                        printeri1.setContent(contentET.getText().toString());
                        printeri1.setCheckBoxLDC(false);
                        printeri1.setCheckBoxServis(false);
                        printeri1.setCheckBoxInformatika(true);

                        database.getReference().child("printeri").child(printeri.getKey()).setValue(printeri1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(PrikazPrinteraLDC.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(PrikazPrinteraLDC.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
                            }
                        });
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