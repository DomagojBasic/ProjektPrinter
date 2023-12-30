package com.example.projektdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.widget.ProgressBar;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressBar progressBar;
    private TextView txtLat, txtLon, txtTime, txtSpeed, txtPrecision, txtProvjera;
    private final int REQ_LOCATION = 1;
    private final String LOGTAG = "DANTE-LOG-geolocation";
    private GoogleMap mMap;
    private MapView mapView;
    private Marker marker;
    Location location = new Location("provider");

    /*--------------------------------------------------------------MAIN ACTIVITY----------------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        /*--------------------------------------------------------------------MAPA-definiranje varijabli--------------------------------------------------------------------*/
        progressBar = findViewById(R.id.progressBar);
        mapView = findViewById(R.id.mapView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /*--------------------------------------------------------------------MAPA-definiranje varijabli--------------------------------------------------------------------*/
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        /*---------------------------------------------------------------------MAPA-Provjera dopustenja----------------------------------------------------------------------*/
        Button btnDohvatiLokaciju = findViewById(R.id.btnDohvatiLokaciju); // dohvaćanje lokacije na klik gumba
        // spremamo this u varijablu koja će biti dostupna u donjoj funkciji, u ovoj točki "this" je aktivnost, a niže je "this" onClickListener
        Activity thisActivity = this;
        btnDohvatiLokaciju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (!checkLocationPermission()) {

                    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                    ActivityCompat.requestPermissions(thisActivity, permissions, REQ_LOCATION);
                } else {
                    accessLocation();

                }
            }
        });
        /*---------------------------------------------------------------------MAPA-Provjera dopustenja----------------------------------------------------------------------*/
        /*-------------------------------------------------------------------Povratak na prethodnu stranicu------------------------------------------------------------------*/
        // povratak na početni ekran
        Button btnBack = findViewById(R.id.btn_Povratak);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
        /*-------------------------------------------------------------------Povratak na prethodnu stranicu------------------------------------------------------------------*/

    /*--------------------------------------------------------------MAIN ACTIVITY----------------------------------------------------------------------------------------------------------*/

    /*--------------------------------------------------------------Metoda provjera lokacijskih dopustenja---------------------------------------------------------------*/
    private boolean checkLocationPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    /*--------------------------------------------------------------Metoda provjera lokacijskih dopustenja---------------------------------------------------------------*/

    /*-------------------------------------------------------------------Metoda provjera dopustenja----------------------------------------------------------------------*/
    // ugrađena funkcija koja se poziva kao odgovor na requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_LOCATION) {
            Log.i(LOGTAG, "onRequestPermissionsResult - REQ_LOCATION");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // ako je dozvoljen pristup -> čitanje lokacije
                Log.i(LOGTAG, "PERMISSION_GRANTED");
                accessLocation();
            } else {
                // ako nije dozvoljen pristup -> ispis na ekranu da nema dozvole
                txtProvjera = findViewById(R.id.txtProvjera);
                txtProvjera.setText("Nisu zadovoljeni uvjeti pristupa");
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
    /*-------------------------------------------------------------------Metoda provjera dopustenja----------------------------------------------------------------------*/

        /*--------------------------------------------------------------------Metoda update lokacije---------------------------------------------------------------------*/
    private void accessLocation() {
        // čitanje podataka o lokaciji
        if (!checkLocationPermission()) {
            Log.i(LOGTAG, "NO PERMISSION");
            return;
        }
        Log.i(LOGTAG, "permission OK");

        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setInterval(10000); // 10 sekundi

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    location = locationResult.getLastLocation();

                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.INVISIBLE);
                    });

                    // Zaustavljanje praćenja lokacije nakon dobivanja jedne lokacije (ako je potrebno)
                    fusedLocationClient.removeLocationUpdates(this);
                    accessLocation();
                }
            }
        }, null);
        /*--------------------------------------------------------------------Metoda update lokacije---------------------------------------------------------------------*/

        /*------------------------------------------------------------------Metoda Postavljanja markera-------------------------------------------------------------------*/
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.e("proba", "lat" + latitude + "long" + longitude);

        if (mMap != null) {
            Log.i(LOGTAG, "Placing marker");


            // uvijek je jedan marker na karti
            // ako ga nema -> postavlja se novi marker i sprema se u varijablu da ga drugi put pomaknemo
            // ako je već postavljen -> postojećem markeru se pomiče pozicija na karti
            // ako želimo svaki put novi marker, svaki put bi izvršili prvi slučaj (addMarker)
            if (marker == null) {
                Log.i(LOGTAG, "Adding marker (" + latitude + ", " + longitude + ")");
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Last position")); //citanje lokacije i postavljanje markera na to mjesto
                Log.e("proba", "dohvati marker" + latitude + longitude);

            } else {
                marker.setPosition(new LatLng(latitude, longitude));
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
        /*------------------------------------------------------------------Metoda Postavljanja markera-------------------------------------------------------------------*/

    /*------------------------------------------------------------Metoda postavljanja mape prije pritiska na gumb----------------------------------------------------------*/
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // metoda za inicijalizaciju karte, poziva se kada je karta spremna primiti naredbe
        Log.i(LOGTAG, "onMapReady");
        // spremamo objekt karte za kasnije korištenje (dodavanje markera i slično)
        mMap = googleMap;
        // postavljanje pozicije karte i zoom faktora
        LatLng rijeka = new LatLng(45.33573, 14.41609);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rijeka, 11f));
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
}
    /*------------------------------------------------------------Metoda postavljanja mape prije pritiska na gumb----------------------------------------------------------*/