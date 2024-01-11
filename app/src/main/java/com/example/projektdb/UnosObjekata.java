package com.example.projektdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

public class UnosObjekata extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unos_objekata);

        Button btnUnosObjekata = findViewById(R.id.btn_UnosObjekata);
        Button btnPovratak = findViewById(R.id.btn_Povratak);

        FirebaseApp.initializeApp(UnosObjekata.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        TextView empty = findViewById(R.id.empty);
        RecyclerView recyclerView = findViewById(R.id.recycler);

        database.getReference().child("objekti").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Objekti> arrayListObjekti = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Objekti objekt = dataSnapshot.getValue(Objekti.class);
                    Objects.requireNonNull(objekt).setKey(dataSnapshot.getKey());
                    arrayListObjekti.add(objekt);

                }
                if (arrayListObjekti.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                ObjektiAdapter adapter = new ObjektiAdapter(UnosObjekata.this, arrayListObjekti);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ObjektiAdapter.OnItemClickListener() {

                    @Override
                    public void onClick(Objekti objekt) {
                        View view = LayoutInflater.from(UnosObjekata.this).inflate(R.layout.add_objekti_dialog, null);
                        TextInputLayout titleLayout, contentLayout;
                        TextInputEditText titleET, contentET;

                        titleET = view.findViewById(R.id.titleET);
                        contentET = view.findViewById(R.id.contentET);
                        titleLayout = view.findViewById(R.id.titleLayout);
                        contentLayout = view.findViewById(R.id.contentLayout);

                        titleET.setText(objekt.getTitle());
                        contentET.setText(objekt.getContent());


                        ProgressDialog progressDialog = new ProgressDialog(UnosObjekata.this);

                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(UnosObjekata.this)
                                .setTitle("Edit")
                                .setView(view)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Objects.requireNonNull(titleET.getText()).toString().isEmpty())  {
                                            titleLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                            contentLayout.setError("This field is required!");
                                        } else {
                                            String unesenaVrijednost = Objects.requireNonNull(titleET.getText()).toString();
                                            // Provjera je li već unesena ista vrijednost
                                            if (Objects.requireNonNull(titleET.getText()).toString().contains(Objects.requireNonNull(titleET.getText()).toString())) {
                                                // Već je unesena ista vrijednost, možete poduzeti odgovarajuće mjere
                                                Toast.makeText(UnosObjekata.this,"ne smije biti ista",Toast.LENGTH_SHORT).show();
                                            }

                                            progressDialog.setMessage("Saving...");
                                            progressDialog.show();
                                            Objekti objekti1 = new Objekti();
                                            objekti1.setTitle(titleET.getText().toString());
                                            objekti1.setContent(contentET.getText().toString());

                                            database.getReference().child("objekti").child(objekt.getKey()).setValue(objekti1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(UnosObjekata.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UnosObjekata.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
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
                                        database.getReference().child("objekti").child(objekt.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(UnosObjekata.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
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

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnUnosObjekata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = LayoutInflater.from(UnosObjekata.this).inflate(R.layout.add_objekti_dialog, null);
                TextInputLayout titleLayout, contentLayout;
                titleLayout = view1.findViewById(R.id.titleLayout);
                contentLayout = view1.findViewById(R.id.contentLayout);
                TextInputEditText titleET, contentET;
                titleET = view1.findViewById(R.id.titleET);
                contentET = view1.findViewById(R.id.contentET);
                AlertDialog alertDialog = new AlertDialog.Builder(UnosObjekata.this).setTitle("Dodaj").setView(view1).setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                    titleLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                    contentLayout.setError("This field is required!");
                                } else {
                                    ProgressDialog dialog = new ProgressDialog(UnosObjekata.this);
                                    dialog.setMessage("Storing in Database...");
                                    dialog.show();
                                    Objekti objekti = new Objekti();
                                    objekti.setTitle(titleET.getText().toString());
                                    objekti.setContent(contentET.getText().toString());
                                    database.getReference().child("objekti").push().setValue(objekti).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            dialogInterface.dismiss();
                                            Toast.makeText(UnosObjekata.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(UnosObjekata.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
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
        btnPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UnosObjekata.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}