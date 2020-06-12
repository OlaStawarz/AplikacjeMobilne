package com.example.database;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class Edycja extends AppCompatActivity implements Usuwanie.UsuwanieListener{

    private CheckBox checkBoxName, checkBoxZapas, checkBoxJednostka;
    private EditText editTextPozycja, editTextZapas, editTextNazwa;
    private Spinner spinner;
    Button zapisz, edytuj, usun;
    DatabaseReference database;

    int licznik;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja);

        checkBoxJednostka = findViewById(R.id.checkBoxJednostka);
        checkBoxName = findViewById(R.id.checkBoxNazwa);
        checkBoxZapas = findViewById(R.id.checkBoxZapas);
        checkBoxJednostka.setVisibility(View.INVISIBLE);
        checkBoxZapas.setVisibility(View.INVISIBLE);
        checkBoxName.setVisibility(View.INVISIBLE);

        zapisz = findViewById(R.id.buttonZapiszZmiany);
        edytuj = findViewById(R.id.buttonEdytujLek);
        usun = findViewById(R.id.buttonUsunLek);
        zapisz.setVisibility(View.INVISIBLE);

        editTextNazwa = findViewById(R.id.editTextNazwaEdycja);
        //editTextPozycja = findViewById(R.id.editTextPozycjaEdycja);
        editTextZapas = findViewById(R.id.editTextZapasEdycja);
        editTextNazwa.setVisibility(View.INVISIBLE);
        editTextZapas.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.spinnerJednostkaEdycja);
        spinner.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance().getReference().child("Lek");

        dodajElementyDoSpinnera_Jednostka();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        licznik = bundle.getInt("licznik");
        //Toast.makeText(Edycja.this, String.valueOf(licznik), Toast.LENGTH_LONG).show();

        edytuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxJednostka.setVisibility(View.VISIBLE);
                checkBoxZapas.setVisibility(View.VISIBLE);
                checkBoxName.setVisibility(View.VISIBLE);
                zapisz.setVisibility(View.VISIBLE);

            }
        });

        usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        checkBoxName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxName.isChecked()) {
                    editTextNazwa.setVisibility(View.VISIBLE);
                } else {
                    editTextNazwa.setVisibility(View.INVISIBLE);
                }
            }
        });
        checkBoxZapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxZapas.isChecked()) {
                    editTextZapas.setVisibility(View.VISIBLE);
                }  else {
                    editTextZapas.setVisibility(View.INVISIBLE);
                }
            }
        });
        checkBoxJednostka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxJednostka.isChecked()) {
                    spinner.setVisibility(View.VISIBLE);
                } else {
                    spinner.setVisibility(View.INVISIBLE);
                }
            }
        });

        // zapisanie wprowadzonych zmian -  dodatkowo zabezpieczenia przed wpisaniem pozycji leku, który nie jest obecnie w bazie
        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance().getReference().child("Lek").child(String.valueOf(licznik));
                //Toast.makeText(this, String.valueOf(pozycja), Toast.LENGTH_LONG).show();
                if (!editTextZapas.getText().toString().isEmpty())
                    database.child("zapas").setValue(editTextZapas.getText().toString());
                if (!editTextNazwa.getText().toString().isEmpty())
                    database.child("nazwa").setValue(editTextNazwa.getText().toString());
                if (!spinner.getSelectedItem().toString().isEmpty())
                    database.child("jednostka").setValue(spinner.getSelectedItem().toString());
                finish();
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

    private void openDialog(){
        Usuwanie dialog = new Usuwanie();
        dialog.show(getSupportFragmentManager(), "Potwierdzenie");

    }

    @Override
    public void potwierdz() {
        Toast.makeText(Edycja.this, "Usuwanie", Toast.LENGTH_LONG).show();
        database = FirebaseDatabase.getInstance().getReference().child("Lek").child(String.valueOf(licznik));
        database.removeValue();
        finish();
    }
}
