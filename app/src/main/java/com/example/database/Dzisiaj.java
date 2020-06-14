package com.example.database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Dzisiaj extends AppCompatActivity {
    DatabaseReference database;
    String nazwa, zapas, jednostka, nZapas, kiedyPowiadomienie;
    TextView mojepowiadomienie, textDobraRobota;
    ImageView checkMark;

    private NotificationHelper notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dzisiaj);

        notification = new NotificationHelper(this);
        mojepowiadomienie = findViewById(R.id.textViewNazwaDzisiaj);
        textDobraRobota = findViewById(R.id.textViewDobraRobota);
        textDobraRobota.setVisibility(View.INVISIBLE);
        //checkMark = findViewById(R.id.imageView);
        //checkMark.setVisibility(View.INVISIBLE);
        Button update = findViewById(R.id.buttonUpdate);
        Button delay = findViewById(R.id.buttonDelay);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String odebranaNazwa = bundle.getString("nazwa");
        final String odebranyLek = bundle.getString("kLek");
        final String odebraneOIleMniejszy = bundle.getString("zZapas");
        mojepowiadomienie.setText(odebranaNazwa + " - potwierdź zażycie leku!");

        // połączenie z bazą danych w celu uzyskania informacji o leku
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Lek").child(odebranyLek);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                zapas = dataSnapshot.child("zapas").getValue(String.class);
                nazwa = dataSnapshot.child("nazwa").getValue(String.class);
                jednostka = dataSnapshot.child("jednostka").getValue(String.class);
                kiedyPowiadomienie = dataSnapshot.child("kiedyPowiadomienie").getValue(String.class);
                int intZapas = Integer.parseInt(zapas);
                int intIleZapas = Integer.parseInt(odebraneOIleMniejszy);
                int nowyZapas = intZapas - intIleZapas;
                nZapas = String.valueOf(nowyZapas);
                if (Integer.parseInt(kiedyPowiadomienie) >= Integer.parseInt(zapas)) {
                    Toast.makeText(Dzisiaj.this, "powiadomienie", Toast.LENGTH_LONG).show(); // powiadomienie o zapasie
                    sendNotification(nazwa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // aktualizacja zapasu + dodanie prostej animacji
        database = FirebaseDatabase.getInstance().getReference().child("Lek").child(odebranyLek);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkMark.setVisibility(View.VISIBLE);
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

        // odłożenie przyjęcia leku na za 10 minut
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
                finish();
            }
        });




    }


    public void sendNotification(String nazwa){
        NotificationCompat.Builder nb = notification.getChannel2Notification(nazwa);
        notification.getManager().notify(9, nb.build());
    }

}
