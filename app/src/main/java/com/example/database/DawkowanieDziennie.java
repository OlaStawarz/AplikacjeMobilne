package com.example.database;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DawkowanieDziennie extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private EditText editTextD1, editTextD2, editTextD3, editTextD4, editTextD5;
    private Spinner spinnerIle;
    private TextView textViewGodzina1, textViewGodzina2, textViewGodzina3, textViewGodzina4, textViewGodzina5;
    private String odebranaNazwa, odebranyLek;
    private Button btnDodaj1, btnDodaj2, btnDodaj3, btnDodaj4, btnDodaj5, btnZatwierdz;
    private boolean clicked1 = false, clicked2 = false;
    private Calendar c = Calendar.getInstance();

    Intent intentDodajLek_Dawkowanie;
    Bundle bundleDodajLek_Dawkowanie;
    AlarmManager am;
    Intent intentDawkowanie_Alarm;
    Bundle bundleDawkowanie_Alarm;
    String txt1, txt2;
    String ile1, ile2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dawkowanie);

        btnZatwierdz = findViewById(R.id.buttonZatwierdz);
        btnDodaj1 = findViewById(R.id.buttonDodaj1);
        btnDodaj2 = findViewById(R.id.buttonDodaj2);
        btnDodaj3 = findViewById(R.id.buttonDodaj3);
        btnDodaj4 = findViewById(R.id.buttonDodaj4);
        btnDodaj5 = findViewById(R.id.buttonDodaj5);


        textViewGodzina1 = findViewById(R.id.textViewGodzina1);
        textViewGodzina2 = findViewById(R.id.textViewGodzina2);
        textViewGodzina3 = findViewById(R.id.textViewGodzina3);
        textViewGodzina4 = findViewById(R.id.textViewGodzina4);
        textViewGodzina5 = findViewById(R.id.textViewGodzina5);

        editTextD1 = findViewById(R.id.editTextD1);
        editTextD2 = findViewById(R.id.editTextD2);
        editTextD3 = findViewById(R.id.editTextD3);
        editTextD4 = findViewById(R.id.editTextD4);
        editTextD5 = findViewById(R.id.editTextD5);

        spinnerIle = findViewById(R.id.spinnerIle);

        setVisibility();
        dodajElementyDoSpinnera();
        pokazPola();

        editTextD1.setText("");
        editTextD2.setText("");
        textViewGodzina1.setText("");
        textViewGodzina2.setText("");

        intentDodajLek_Dawkowanie = getIntent();
        bundleDodajLek_Dawkowanie = intentDodajLek_Dawkowanie.getExtras();
        //odebraneDane = bundleDodajLek_Dawkowanie.getString("jednostka");
        odebranaNazwa = bundleDodajLek_Dawkowanie.getString("nazwa");
        odebranyLek = bundleDodajLek_Dawkowanie.getString("ktoryLek");
        //Toast.makeText(DawkowanieDziennie.this, odebraneDane + odebranaNazwa + odebranyLek, Toast.LENGTH_LONG).show();


        btnDodaj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                clicked1 = true;
                //txt1.setText();
            }
        });

        // jeśli drugi przycisk został kliknięty to zakładam, że w pierwszym oknie jest ustawione powiadomienie - więc je wysyłam
        btnDodaj2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt1 = textViewGodzina1.getText().toString();
                ile1 = editTextD1.getText().toString();
                bundleDodajLek_Dawkowanie.putString("godzina", txt1);
                bundleDodajLek_Dawkowanie.putString("ile", ile1);
                startAlarm1(c);
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                clicked2 = true;
                //txt1.setText();

            }
        });

        btnZatwierdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextD2.getText().toString().isEmpty())
                    startAlarm1(c);
                else
                    startAlarm2(c);
                bundleDodajLek_Dawkowanie.putString("wiadomosc", (spinnerIle.getSelectedItem().toString() + " raz/-y dziennie"));

                if(textViewGodzina2.getText().toString().isEmpty()) {
                    txt1 = textViewGodzina1.getText().toString();
                    ile1 = editTextD1.getText().toString();
                    bundleDodajLek_Dawkowanie.putString("godzina", txt1);
                    bundleDodajLek_Dawkowanie.putString("ile", ile1);
                }
                else {
                    txt2 = textViewGodzina2.getText().toString();
                    ile2 = editTextD2.getText().toString();
                    bundleDodajLek_Dawkowanie.putString("godzina", txt1 + " " + txt2);
                    bundleDodajLek_Dawkowanie.putString("ile", ile1 + " " + ile2);
                }
                intentDodajLek_Dawkowanie.putExtras(bundleDodajLek_Dawkowanie);
                setResult(Activity.RESULT_OK, intentDodajLek_Dawkowanie);

                finish();
            }
        });


    }

    // pobranie godziny z kalendarza
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
    }

    // ustawienie clicked1 lub clicked2 z true na false umożliwia ustawienie odpowiedniego pola z godziną - korzystam z globalnej instancji kalendarza
    private void updateTimeText(Calendar c){
        String textTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        if (clicked1){
            textViewGodzina1.setText(textTime);
            clicked1 = false;
        } else if (clicked2){
            textViewGodzina2.setText(textTime);
            clicked2 = false;
        }
    }

    // ustawienie pierwszego  alarmu
    private void startAlarm1(Calendar c) {
        createIntent();
        bundleDawkowanie_Alarm.putString("zmniejszZapas", editTextD1.getText().toString().trim());
        intentDawkowanie_Alarm.putExtras(bundleDawkowanie_Alarm);
        int ticks = (int) System.currentTimeMillis();
        PendingIntent pi = PendingIntent.getBroadcast(DawkowanieDziennie.this, ticks, intentDawkowanie_Alarm, 0);
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
    }

    //ustawienie drugiego alarmu
    private void startAlarm2(Calendar c){
        createIntent();
        bundleDawkowanie_Alarm.putString("zmniejszZapas", editTextD2.getText().toString().trim());
        intentDawkowanie_Alarm.putExtras(bundleDawkowanie_Alarm);
        int ticks = (int) System.currentTimeMillis();
        PendingIntent pi = PendingIntent.getBroadcast(DawkowanieDziennie.this, ticks, intentDawkowanie_Alarm, 0);
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
    }

    private void createIntent(){
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intentDawkowanie_Alarm = new Intent(this, AlertReciever.class);
        bundleDawkowanie_Alarm = new Bundle();
        bundleDawkowanie_Alarm.putString("nazwaLeku", odebranaNazwa);
        bundleDawkowanie_Alarm.putString("ktoryLek1", odebranyLek);
    }

    private void dodajElementyDoSpinnera(){
        ArrayList<Integer> dawkowanie = new ArrayList<>();
        dawkowanie.add(1);
        dawkowanie.add(2);
        dawkowanie.add(3);
        spinnerIle.setAdapter(new ArrayAdapter<>(DawkowanieDziennie.this,
                android.R.layout.simple_spinner_dropdown_item, dawkowanie));
    }

    private void setVisibility(){
        btnDodaj2.setVisibility(View.INVISIBLE);
        btnDodaj3.setVisibility(View.INVISIBLE);
        btnDodaj4.setVisibility(View.INVISIBLE);
        btnDodaj5.setVisibility(View.INVISIBLE);
        editTextD2.setVisibility(View.INVISIBLE);
        editTextD3.setVisibility(View.INVISIBLE);
        editTextD4.setVisibility(View.INVISIBLE);
        editTextD5.setVisibility(View.INVISIBLE);
        textViewGodzina2.setVisibility(View.INVISIBLE);
        textViewGodzina3.setVisibility(View.INVISIBLE);
        textViewGodzina4.setVisibility(View.INVISIBLE);
        textViewGodzina5.setVisibility(View.INVISIBLE);
    }

    private void pokazPola(){

        spinnerIle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        btnDodaj2.setVisibility(View.INVISIBLE);
                        btnDodaj3.setVisibility(View.INVISIBLE);
                        btnDodaj4.setVisibility(View.INVISIBLE);
                        btnDodaj5.setVisibility(View.INVISIBLE);
                        editTextD2.setVisibility(View.INVISIBLE);
                        editTextD3.setVisibility(View.INVISIBLE);
                        editTextD4.setVisibility(View.INVISIBLE);
                        editTextD5.setVisibility(View.INVISIBLE);
                        textViewGodzina2.setVisibility(View.INVISIBLE);
                        textViewGodzina3.setVisibility(View.INVISIBLE);
                        textViewGodzina4.setVisibility(View.INVISIBLE);
                        textViewGodzina5.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        btnDodaj2.setVisibility(View.VISIBLE);
                        btnDodaj3.setVisibility(View.INVISIBLE);
                        btnDodaj4.setVisibility(View.INVISIBLE);
                        btnDodaj5.setVisibility(View.INVISIBLE);
                        editTextD2.setVisibility(View.VISIBLE);
                        editTextD3.setVisibility(View.INVISIBLE);
                        editTextD4.setVisibility(View.INVISIBLE);
                        editTextD5.setVisibility(View.INVISIBLE);
                        textViewGodzina2.setVisibility(View.VISIBLE);
                        textViewGodzina3.setVisibility(View.INVISIBLE);
                        textViewGodzina4.setVisibility(View.INVISIBLE);
                        textViewGodzina5.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        btnDodaj2.setVisibility(View.VISIBLE);
                        btnDodaj3.setVisibility(View.VISIBLE);
                        btnDodaj4.setVisibility(View.INVISIBLE);
                        btnDodaj5.setVisibility(View.INVISIBLE);
                        editTextD2.setVisibility(View.VISIBLE);
                        editTextD3.setVisibility(View.VISIBLE);
                        editTextD4.setVisibility(View.INVISIBLE);
                        editTextD5.setVisibility(View.INVISIBLE);
                        textViewGodzina2.setVisibility(View.VISIBLE);
                        textViewGodzina3.setVisibility(View.VISIBLE);
                        textViewGodzina4.setVisibility(View.INVISIBLE);
                        textViewGodzina5.setVisibility(View.INVISIBLE);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
