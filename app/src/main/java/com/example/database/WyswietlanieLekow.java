package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
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

public class WyswietlanieLekow extends AppCompatActivity {

    private ListView listView;
    private Button btnPowrot, btnAktualizuj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wyswietlanie_lekow);

        btnAktualizuj = findViewById(R.id.buttonAktualizuj);
        //btnPowrot = findViewById(R.id.buttonPowrot);



        btnAktualizuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WyswietlanieLekow.this, Edycja.class);
                Bundle bundle = new Bundle();
                bundle.putInt("licznik", listView.getAdapter().getCount());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        listView = findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.lek, list);
        listView.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lek");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Lek lek = snapshot.getValue(Lek.class);
                    String txt = "Pozycja " + i  + "\n" + "Nazwa: " + lek.getNazwa() + "\nJednostka: " + lek.getJednostka()
                            +"\nDawkowanie: " + lek.getDawkowanie() + "\nZapas: " + lek.getZapas();
                    list.add(txt);
                    i++;
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
