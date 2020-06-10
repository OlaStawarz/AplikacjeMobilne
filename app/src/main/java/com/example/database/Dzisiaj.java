package com.example.database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Dzisiaj extends AppCompatActivity {
    DatabaseReference database;
    String nazwa, zapas, jednostka, nZapas, kiedyPowiadomienie;
    TextView t, textDobraRobota;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dzisiaj);

        t = findViewById(R.id.textViewNazwaDzisiaj);
        textDobraRobota = findViewById(R.id.textViewDobraRobota);
        textDobraRobota.setVisibility(View.INVISIBLE);
        Button update = findViewById(R.id.buttonUpdate);
        Button delay = findViewById(R.id.buttonDelay);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String odebranaNazwa = bundle.getString("nazwa");
        final String odebranyLek = bundle.getString("kLek");
        final String odebraneOIleMniejszy = bundle.getString("zZapas");
        t.setText(odebranaNazwa + ", " + odebraneOIleMniejszy);


        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Lek").child(odebranyLek);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                zapas = dataSnapshot.child("zapas").getValue(String.class);
                nazwa = dataSnapshot.child("nazwa").getValue(String.class);
                jednostka = dataSnapshot.child("jednostka").getValue(String.class);
                kiedyPowiadomienie = dataSnapshot.child("kiedyPowiadomienie").getValue(String.class);
                int oZapas = Integer.parseInt(zapas);
                int oIleZapas = Integer.parseInt(odebraneOIleMniejszy);
                int nowyZapas = oZapas - oIleZapas;
                nZapas = String.valueOf(nowyZapas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //database = FirebaseDatabase.getInstance().getReference().child("Lek");
        database = FirebaseDatabase.getInstance().getReference().child("Lek").child(odebranyLek);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDobraRobota.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .repeat(2)
                        .playOn(textDobraRobota);
                database.child("zapas").setValue(nZapas);
                //dodanie finish
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                    }

                }, 3000); // 5000ms delay

            }
        });

        delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intentDzisiaj_Alarm = new Intent(Dzisiaj.this, AlertReciever.class);
                Bundle bundleDawkowanie_Alarm = new Bundle();
                bundleDawkowanie_Alarm.putString("nazwaLeku", nazwa);
                bundleDawkowanie_Alarm.putString("ktoryLek1", odebranyLek);
                bundleDawkowanie_Alarm.putString("zmniejszZapas", odebraneOIleMniejszy);
                intentDzisiaj_Alarm.putExtras(bundleDawkowanie_Alarm);
                int ticks = (int) System.currentTimeMillis();
                PendingIntent pi = PendingIntent.getBroadcast(Dzisiaj.this, ticks, intentDzisiaj_Alarm, 0);
                am.set(AlarmManager.RTC_WAKEUP, 600000 , pi);
                //dodanie finish?
                finish();
            }
        });




    }
}
