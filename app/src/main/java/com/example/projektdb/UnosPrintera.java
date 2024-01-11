package com.example.projektdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class UnosPrintera extends AppCompatActivity {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unos_printera);
        Button btnOdustani = findViewById(R.id.btn_Povratak);
        Button btnUnosPrintera = findViewById(R.id.btn_UnosPrintera);

        FirebaseApp.initializeApp(UnosPrintera.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        TextView empty = findViewById(R.id.empty);
        RecyclerView recyclerView = findViewById(R.id.recycler);

        database.getReference().child("printeri").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Printeri> arrayList = new ArrayList<>();

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

                PrinterAdapter adapter = new PrinterAdapter(UnosPrintera.this, arrayList);
                recyclerView.setAdapter(adapter);



                adapter.setOnItemClickListener(new PrinterAdapter.OnItemClickListener() {

                                                   public void onClick(Printeri printeri, int position) {

                        View view = LayoutInflater.from(UnosPrintera.this).inflate(R.layout.add_inf_serv_ldc_printeri_dialog, null);
                        TextInputLayout titleLayout, contentLayout;
                        TextInputEditText titleET, contentET;

                        boolean checkBoxInformatika, checkBoxLDC, checkBoxServis;


                        titleET = view.findViewById(R.id.titleET);
                        contentET = view.findViewById(R.id.contentET);
                        titleLayout = view.findViewById(R.id.titleLayout);
                        contentLayout = view.findViewById(R.id.contentLayout);

                        titleET.setText(printeri.getTitle());
                        contentET.setText(printeri.getContent());

                       checkBoxInformatika = ((CheckBox) findViewById(R.id.checkBoxInformatika)).isChecked();
                       checkBoxLDC = ((CheckBox) findViewById(R.id.checkBoxLDC)).isChecked();
                       checkBoxServis = ((CheckBox) findViewById(R.id.checkBoxServis)).isChecked();


                        ProgressDialog progressDialog = new ProgressDialog(UnosPrintera.this);

                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(UnosPrintera.this)
                                .setTitle("Edit")
                                .setView(view)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                            titleLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                            contentLayout.setError("This field is required!");
                                        } else {
                                            String unesenaVrijednost = Objects.requireNonNull(titleET.getText()).toString();
                                            // Provjera je li već unesena ista vrijednost
                                            if (Objects.requireNonNull(titleET.getText()).toString().contains(Objects.requireNonNull(titleET.getText()).toString())) {
                                                // Već je unesena ista vrijednost, možete poduzeti odgovarajuće mjere
                                                Toast.makeText(UnosPrintera.this, "ne smije biti ista", Toast.LENGTH_SHORT).show();
                                            }


                                            progressDialog.setMessage("Saving...");
                                            progressDialog.show();
                                            Printeri printeri1 = new Printeri();
                                            printeri1.setTitle(titleET.getText().toString());
                                            printeri1.setContent(contentET.getText().toString());
                                           // ne znam kako prikazati checkbox!!
                                            // printeri1.setCheckBoxInformatika(true);
                                            database.getReference().child("printeri").child(printeri.getKey()).setValue(printeri1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(UnosPrintera.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UnosPrintera.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                })
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.setTitle("Deleting...");
                                        progressDialog.show();
                                        database.getReference().child("printeri").child(printeri.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(UnosPrintera.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).create();
                        alertDialog.show();

                    }


                }


                );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnUnosPrintera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = LayoutInflater.from(UnosPrintera.this).inflate(R.layout.add_inf_serv_ldc_printeri_dialog, null);
                TextInputLayout titleLayout, contentLayout;

                CheckBox checkBoxInformatika, checkBoxLDC, checkBoxServis;
                checkBoxInformatika = view1.findViewById(R.id.checkBoxInformatika);
                checkBoxLDC = view1.findViewById(R.id.checkBoxLDC);
                checkBoxServis = view1.findViewById(R.id.checkBoxServis);

                titleLayout = view1.findViewById(R.id.titleLayout);
                contentLayout = view1.findViewById(R.id.contentLayout);
                TextInputEditText titleET, contentET;
                titleET = view1.findViewById(R.id.titleET);
                contentET = view1.findViewById(R.id.contentET);
                AlertDialog alertDialog = new AlertDialog.Builder(UnosPrintera.this).setTitle("Dodaj").setView(view1).setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                    titleLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                    contentLayout.setError("This field is required!");
                                } else {
                                    ProgressDialog dialog = new ProgressDialog(UnosPrintera.this);
                                    dialog.setMessage("Storing in Database...");
                                    dialog.show();
                                    Printeri printeri = new Printeri();
                                    printeri.setTitle(titleET.getText().toString());
                                    printeri.setContent(contentET.getText().toString());
                                    printeri.setCheckBoxInformatika(checkBoxInformatika.isChecked());
                                    printeri.setCheckBoxLDC(checkBoxLDC.isChecked());
                                    printeri.setCheckBoxServis(checkBoxServis.isChecked());
                                    database.getReference().child("printeri").push().setValue(printeri).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            dialogInterface.dismiss();
                                            Toast.makeText(UnosPrintera.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(UnosPrintera.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });


        btnOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnosPrintera.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

};
