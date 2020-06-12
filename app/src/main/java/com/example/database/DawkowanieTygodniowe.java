package com.example.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DawkowanieTygodniowe extends AppCompatActivity {

    CheckBox poniedzialek, wtorek, sroda, czwartek, piatek, sobota, niedziela;
    Button dawkowanieTygodniowe;
    String dawkowanie = "";

    // klasa w budowie - nie ustawiono godzin powiadomienia

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dawkowanie_tygodniowe);

        poniedzialek = findViewById(R.id.checkBoxiedziałek);
        wtorek = findViewById(R.id.checkBoxWtorek);
        sroda = findViewById(R.id.checkBoxSroda);
        czwartek = findViewById(R.id.checkBoxCzwartek);
        piatek = findViewById(R.id.checkBoxPiatek);
        sobota = findViewById(R.id.checkBoxSobota);
        niedziela = findViewById(R.id.checkBoxNiedziela);

        dawkowanieTygodniowe = findViewById(R.id.buttonZapiszTygodniowe);

        poniedzialek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(poniedzialek.isChecked()){
                    String dzien = poniedzialek.getText().toString();
                    dawkowanie += dzien + " ";
                }

            }
        });

        wtorek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wtorek.isChecked()){
                    String dzien = wtorek.getText().toString();
                    dawkowanie += dzien + " ";
                }

            }
        });

        sroda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sroda.isChecked()){
                    String dzien = sroda.getText().toString();
                    dawkowanie += dzien + " ";
                }

            }
        });

        czwartek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(czwartek.isChecked()){
                    String dzien = czwartek.getText().toString();
                    dawkowanie += dzien + " ";
                }

            }
        });

        piatek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(piatek.isChecked()){
                    String dzien = piatek.getText().toString();
                    dawkowanie += dzien + " ";
                }

            }
        });

        sobota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sobota.isChecked()){
                    String dzien = sobota.getText().toString();
                    dawkowanie += dzien + " ";
                }

            }
        });

        niedziela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(niedziela.isChecked()){
                    String dzien = niedziela.getText().toString();
                    dawkowanie += dzien + ", ";
                }

            }
        });


        dawkowanieTygodniowe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString("dawkowanieTygodniowe", dawkowanie);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });

    }
}
