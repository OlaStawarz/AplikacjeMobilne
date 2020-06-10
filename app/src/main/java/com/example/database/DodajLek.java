package com.example.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DodajLek extends AppCompatActivity {

    Spinner spinnerJednostka, spinnerDawkowanie;
    EditText editNazwa, editOpakowania, editIlosc, editKiedyPowiadomienie;
    TextView textDawkowanie, textKiedyPowiadomienie;
    Button btnSave;
    DatabaseReference database;
    Lek lek;
    long maxId;
    String msg = "", godzinaPowiadomienia, ile, tygodniowo = "";
    String ktoryLek;
    Switch switchPowiadomienie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_lek);

        editNazwa = findViewById(R.id.editNazwa);
        editOpakowania = findViewById(R.id.editTextOpakowania);
        editIlosc = findViewById(R.id.editTextIlosc);
        textDawkowanie = findViewById(R.id.textViewDawkowanie);
        editKiedyPowiadomienie = findViewById(R.id.editTextIlePozostalo);
        textKiedyPowiadomienie = findViewById(R.id.textViewIlePozostalo);
        switchPowiadomienie = findViewById(R.id.switchPowiadomienie);

        btnSave = findViewById(R.id.buttonDodajLekDoBazy);
        spinnerJednostka = findViewById(R.id.spinner);
        spinnerDawkowanie = findViewById(R.id.spinnerDawkowanie);

        editKiedyPowiadomienie.setVisibility(View.INVISIBLE);
        textKiedyPowiadomienie.setVisibility(View.INVISIBLE);

        dodajElementyDoSpinnera_Jednostka();
        dodajElementyDoSpinnera_Dawkowanie();

        ktoryLek = "lek1";

        //Dopiero po wpisaniu nazwy kolejne pola są możliwe do uzupełnienia
        database = FirebaseDatabase.getInstance().getReference().child("Lek");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxId = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        spinnerDawkowanie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("X razy dziennie")){
                    String jednostka = spinnerJednostka.getSelectedItem().toString();
                    Intent myIntent = new Intent(DodajLek.this, DawkowanieDziennie.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("jednostka", jednostka);
                    bundle.putString("nazwa", editNazwa.getText().toString().trim());
                    //tutaj dac maxId
                    bundle.putString("ktoryLek", String.valueOf(maxId));
                    myIntent.putExtras(bundle);
                    startActivityForResult(myIntent, 101);
                }
                if(parent.getItemAtPosition(position).equals("X razy w tygodniu")){
                    Intent intentDodajLek_DawkowanieTygodniowe = new Intent(DodajLek.this, DawkowanieTygodniowe.class);
                    startActivityForResult(intentDodajLek_DawkowanieTygodniowe, 201);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switchPowiadomienie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editKiedyPowiadomienie.setVisibility(View.VISIBLE);
                    textKiedyPowiadomienie.setVisibility(View.VISIBLE);
                } else {
                    editKiedyPowiadomienie.setVisibility(View.INVISIBLE);
                    textKiedyPowiadomienie.setVisibility(View.INVISIBLE);
                }
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lek = new Lek();
                int opakowania = Integer.parseInt(editOpakowania.getText().toString());
                int ilosc = Integer.parseInt(editIlosc.getText().toString());
                String nazwa = editNazwa.getText().toString().trim();
                String zapas = String.valueOf(opakowania * ilosc);

                Powiadomienie powiadomienie = new Powiadomienie();

                lek.setNazwa(nazwa);
                lek.setJednostka(spinnerJednostka.getSelectedItem().toString().trim());
                if (!msg.isEmpty()) {
                    lek.setDawkowanie(msg);
                    powiadomienie.setGodzina(godzinaPowiadomienia);
                    powiadomienie.setIlosc(ile);
                    lek.setPowiadomienie(powiadomienie);
                }
                else {
                    lek.setDawkowanie(tygodniowo);
                    powiadomienie.setGodzina("12:00");
                    powiadomienie.setIlosc("5");
                    lek.setPowiadomienie(powiadomienie);
                }
                if (!editKiedyPowiadomienie.getText().toString().isEmpty()) {
                    lek.setKiedyPowiadomienie(editKiedyPowiadomienie.getText().toString());
                }
                else
                    lek.setKiedyPowiadomienie("-100000");
                lek.setZapas(zapas);
                // tutaj dać maxId
                database.child(String.valueOf(maxId)).setValue(lek);
                finish();
            }

        });

    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode == 101 && resultCode == Activity.RESULT_OK){
                Bundle bundleResult = data.getExtras();
                msg = bundleResult.getString("wiadomosc");
                godzinaPowiadomienia = bundleResult.getString("godzina");
                ile = bundleResult.getString("ile");
                Toast.makeText(DodajLek.this, godzinaPowiadomienia + " " + ile, Toast.LENGTH_LONG).show();

            }
            if(requestCode == 201 && resultCode == Activity.RESULT_OK) {
                Bundle bundleResult = data.getExtras();
                 tygodniowo = bundleResult.getString("dawkowanieTygodniowe");
                //godzinaPowiadomienia = bundleResult.getString("godzina");
                //ile = bundleResult.getString("ile");
                Toast.makeText(DodajLek.this, msg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

 private void dodajElementyDoSpinnera_Jednostka(){
        //Inicjalizowanie elementów spinnera
        ArrayList<String> jednostki = new ArrayList<>();
        jednostki.add("tabletka");
        jednostki.add("miligram");
        jednostki.add("saszetka");
        jednostki.add("łyżeczka");
        jednostki.add("pastylka");
        spinnerJednostka.setAdapter(new ArrayAdapter<>(DodajLek.this,
                android.R.layout.simple_spinner_dropdown_item, jednostki));

    }

    private void dodajElementyDoSpinnera_Dawkowanie(){
        //Inicjalizowanie elementów spinnera
        ArrayList<String> dawkowanie = new ArrayList<>();
        dawkowanie.add("-------------");
        dawkowanie.add("X razy dziennie");
        dawkowanie.add("X razy w tygodniu");
        spinnerDawkowanie.setAdapter(new ArrayAdapter<>(DodajLek.this,
                android.R.layout.simple_spinner_dropdown_item, dawkowanie));


    }

}
