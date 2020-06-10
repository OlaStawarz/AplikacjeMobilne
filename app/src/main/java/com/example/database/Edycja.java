package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class Edycja extends AppCompatActivity {

    private CheckBox checkBoxName, checkBoxZapas, checkBoxJednostka;
    private EditText editTextPozycja, editTextZapas, editTextNazwa;
    private Spinner spinner;
    Button zapisz;
    DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja);

        checkBoxJednostka = findViewById(R.id.checkBoxJednostka);
        checkBoxName = findViewById(R.id.checkBoxNazwa);
        checkBoxZapas = findViewById(R.id.checkBoxZapas);

        zapisz = findViewById(R.id.buttonZapiszZmiany);

        editTextNazwa = findViewById(R.id.editTextNazwaEdycja);
        editTextPozycja = findViewById(R.id.editTextPozycjaEdycja);
        editTextZapas = findViewById(R.id.editTextZapasEdycja);
        editTextNazwa.setVisibility(View.INVISIBLE);
        editTextZapas.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.spinnerJednostkaEdycja);
        spinner.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance().getReference().child("Lek");

        dodajElementyDoSpinnera_Jednostka();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final int licznik = bundle.getInt("licznik");
        //Toast.makeText(Edycja.this, String.valueOf(licznik), Toast.LENGTH_LONG).show();
        checkBoxName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxName.isChecked()) {
                    editTextNazwa.setVisibility(View.VISIBLE);

                }
            }
        });
        checkBoxZapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxZapas.isChecked()) {
                    editTextZapas.setVisibility(View.VISIBLE);

                }
            }
        });
        checkBoxJednostka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxJednostka.isChecked()) {
                    spinner.setVisibility(View.VISIBLE);

                }
            }
        });

        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextPozycja.getText().toString().isEmpty()) {
                    if (Integer.parseInt(editTextPozycja.getText().toString()) > 0
                            && Integer.parseInt(editTextPozycja.getText().toString()) <= licznik) {
                        int pozycja = Integer.parseInt(editTextPozycja.getText().toString());
                        database = FirebaseDatabase.getInstance().getReference().child("Lek").child(String.valueOf(pozycja - 1));
                        //Toast.makeText(this, String.valueOf(pozycja), Toast.LENGTH_LONG).show();
                        if (!editTextZapas.getText().toString().isEmpty())
                            database.child("zapas").setValue(editTextZapas.getText().toString());
                        if (!editTextNazwa.getText().toString().isEmpty())
                            database.child("nazwa").setValue(editTextNazwa.getText().toString());
                        if(!spinner.getSelectedItem().toString().isEmpty())
                            database.child("jednostka").setValue(spinner.getSelectedItem().toString());
                        finish();
                    }
                    else {
                        editTextPozycja.setError("Nie ma takiego leku w bazie");
                    }

                } else {
                    editTextPozycja.setError("To pole nie może być puste");
                }
            }


        });



    }

    private void dodajElementyDoSpinnera_Jednostka() {
        //Inicjalizowanie elementów spinnera
        ArrayList<String> jednostki = new ArrayList<>();
        jednostki.add("tabletka");
        jednostki.add("miligram");
        jednostki.add("saszetka");
        jednostki.add("łyżeczka");
        jednostki.add("pastylka");
        spinner.setAdapter(new ArrayAdapter<>(Edycja.this,
                android.R.layout.simple_spinner_dropdown_item, jednostki));

    }



}
