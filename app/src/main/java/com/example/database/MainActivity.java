package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnDodajLek, btnWyswietlLeki, btnWyswietlPowiadomienia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDodajLek = findViewById(R.id.buttonDodajLek);
        btnWyswietlLeki = findViewById(R.id.buttonWyswietlLeki);
        btnWyswietlPowiadomienia = findViewById(R.id.buttonPowiadomienia);

        btnDodajLek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DodajLek.class);
                startActivity(myIntent);
            }
        });

        btnWyswietlLeki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, WyswietlanieLekow.class);
                startActivity(myIntent);
            }
        });

        btnWyswietlPowiadomienia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, PowiadomieniaDzisiaj.class);
                startActivity(myIntent);
            }
        });



        // połączenie z bazą danych w celu zweryfikowania czy zapas się już skończył
     /*   DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Lek");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Lek lek = snapshot.getValue(Lek.class);
                    if (Integer.parseInt(lek.getKiedyPowiadomienie()) >= Integer.parseInt(lek.getZapas())){
                        Toast.makeText(MainActivity.this, "powiadomienie", Toast.LENGTH_LONG).show();
                        if (Integer.parseInt(lek.getZapas()) > 0) {
                            txt = " wynosi " + lek.getZapas();

                        } else {
                            txt = " skończył się!";
                        }
                        sendNotification(txt, lek.getNazwa());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }


}
