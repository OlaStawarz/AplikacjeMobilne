package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.TimePickerDialog;
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
    EditText editText, editNumer;
    TextView t1, t2, t3, t4;
    Button b1;
    DatabaseReference reff;
    private ListView listView;
    private ArrayList<String> zapas, powiadomienie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDodajLek = findViewById(R.id.buttonDodajLek);
        btnWyswietlLeki = findViewById(R.id.buttonWyswietlLeki);
        btnWyswietlPowiadomienia = findViewById(R.id.buttonPowiadomienia);


        //FirebaseDatabase database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference("message");

        /*myRef.setValue("Hej, World!");
        Toast.makeText(MainActivity.this, "Dodano", Toast.LENGTH_LONG).show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, "Udalo sie", Toast.LENGTH_LONG).show();
                //t2.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Nie udalo sie", Toast.LENGTH_LONG).show();
            }
        });*/


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



        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Lek");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //long count = dataSnapshot.getChildrenCount();
                zapas = new ArrayList<>();
                ArrayList<String> powiadomienie = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Lek lek = snapshot.getValue(Lek.class);
                    zapas.add(lek.getZapas());
                    powiadomienie.add(lek.getKiedyPowiadomienie());
                    if (Integer.parseInt(lek.getKiedyPowiadomienie()) >= Integer.parseInt(lek.getZapas())){
                        Toast.makeText(MainActivity.this, "powiadomienie", Toast.LENGTH_LONG).show();
                    }
                }
                Toast.makeText(MainActivity.this, powiadomienie.get(0), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*for(int i = 0; i < powiadomienie.size(); i++){
            if (Integer.parseInt(powiadomienie.get(i)) >= Integer.parseInt(zapas.get(i)))
                Toast.makeText(MainActivity.this, "powiadomienie", Toast.LENGTH_LONG).show();
        }*/




    }

}
