package com.example.projektdb;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

        /*______________________________________Prikaz printera(informatika) u recycle-u_______________________________________________________*/
        database.getReference().child("printeri").addValueEventListener(new ValueEventListener() {
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

                PrinterAdapter adapter = new PrinterAdapter(MainActivity.this, arrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                /*______________________________________Prikaz printera(informatika) u recycle-u_______________________________________________________*/


                adapter.setOnItemClickListener(new PrinterAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Printeri printeri) {

                    }


                    /*______________________________________btnLDC_______________________________________________________*/

                    @Override
                    public void onBtnLDCClick(Printeri printeri, int position) {
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_printeri_dialog_ldc, null);
                        TextInputLayout titleLayout, contentLayout;
                        TextInputEditText titleET, contentET;
                        Spinner spinner;
                        spinner = view.findViewById(R.id.spinner);
                        ArrayList<Objekti> arrayListObjekti = new ArrayList<>(); // Lista objekata
                        ObjektiAdapterSpinner objektiAdapter = new ObjektiAdapterSpinner(MainActivity.this, arrayListObjekti);
                        DatabaseReference spinnerRef;

                        // Fetch data from Firebase Realtime Database for Objekti
                        spinnerRef = FirebaseDatabase.getInstance().getReference("objekti"); // dobavljanje liste objeakta
                        spinnerRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Clear the existing data
                                arrayListObjekti.clear();

                                // Iterate through dataSnapshot to get Objekti objects
                                for (DataSnapshot objektiSnapshot : dataSnapshot.getChildren()) {
                                    Objekti objekti = objektiSnapshot.getValue(Objekti.class);
                                    if (objekti != null) {
                                        // Add Objekti object to the list
                                        arrayListObjekti.add(objekti);
                                    }
                                }

                                // Notify the adapter that the data has changed
                                objektiAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error if any
                                Toast.makeText(MainActivity.this, "Error fetching Objekti data", Toast.LENGTH_SHORT).show();
                            }
                        });

                        spinner.setAdapter(objektiAdapter);// prikaz objekata u spineru
                        /*______________Metoda uređivanja - korištena da prilikom klika preuzme parametre odabranog printera___________*/
                        titleET = view.findViewById(R.id.titleET);
                        contentET = view.findViewById(R.id.contentET);
                        titleLayout = view.findViewById(R.id.titleLayout);
                        contentLayout = view.findViewById(R.id.contentLayout);

                        titleET.setText(printeri.getTitle());
                        contentET.setText(printeri.getContent());


                        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
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
                                                Toast.makeText(MainActivity.this, "ne smije biti ista", Toast.LENGTH_SHORT).show();
                                            }


                                            progressDialog.setMessage("Saving...");
                                            progressDialog.show();
                                            Printeri printeri1 = new Printeri();
                                            printeri1.setTitle(titleET.getText().toString());
                                            printeri1.setContent(contentET.getText().toString());
                                            database.getReference().child("printeri").child(printeri.getKey()).setValue(printeri1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(MainActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
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


                        /*______________Metoda uređivanja - korištena da prilikom klika preuzme parametre odabranog printera___________*/

                        //Trenutacno - Prilikom pritiska gumba LDC na printeru otvara se dihalog koji preuzima printerove podatke i dodaje odabir objekta

                        /*Potrebno je:
                        1.Prilikom odabira spremi - sprema se printer sa objektom u Activity (Prikaz printera LDC)

                        Problem:
                        -Da li je potrebno stvoriti novu ArrayListu koja ce prikazivati postojeci printer + objekt?
                        -Da li se problem da rjesiti spajanjem 2 liste (objekti i printeri)
                        -Da li se problem moze rjesiti prikazivanjem posebno 1 liste i naknadno 2 liste?

                        Rješenje:
                        Koristiti 1 ArrayListu printera koja prilikom unosa printera odabire se da li je printer u informatici,servisu ili LDC-u.
                        Prikaz printera  na osnovu toga
                        Nakon toga dodati listu objekata
                        
*/
                        /*

                        // Ukloni element iz ArrayList
                        arrayList.remove(position);
                        //Dodavanje printera u printeriLDC
                        database.getReference().child("printeriLDC").push().setValue(printeri);
                        adapter.notifyDataSetChanged();
                        //brisanje printera
                        database.getReference().child("printeri").child(printeri.getKey()).removeValue();
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();

                        recreate();
                        */
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
                        // refreshData();
                        // Obavijesti adapter o promjeni podataka
                        //adapter.notifyItemRemoved(position);

                       recreate();

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
                Intent intent = new Intent(MainActivity.this, Mapa.class);
                startActivity(intent);
            }
        });


        btnPrinteriNaLDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrikazPrinteraLDC.class);
                startActivity(intent);
            }
        });


        btnPrinteriNaServisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrikazPrinteraServis.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

                // AĹľurirajte RecyclerView s novim podacima
                adapter.updateData(updatedList);
                recreate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Prikazivanje poruke o greĹˇki ako je potrebno
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.unos_printera) {
            Intent intent = new Intent(this, UnosPrintera.class);
            startActivity(intent);

        } else if (id == R.id.unos_objekata) {
            Intent intent = new Intent(this, UnosObjekata.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}