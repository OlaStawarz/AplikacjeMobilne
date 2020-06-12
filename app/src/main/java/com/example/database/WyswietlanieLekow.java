package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wyswietlanie_lekow);

        listView = findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.lek, list);
        listView.setAdapter(adapter);
        final ArrayList<String> positions = new ArrayList<>();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lek");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 1;
                String zapas;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Lek lek = snapshot.getValue(Lek.class);
                    if(Integer.parseInt(lek.getZapas()) < 0){
                        zapas = "0";
                    } else {
                        zapas = lek.getZapas();
                    }
                    String txt = "Pozycja " + i  + "\n" + "Nazwa: " + lek.getNazwa() + "\nJednostka: " + lek.getJednostka()
                            +"\nDawkowanie: " + lek.getDawkowanie() + "\nZapas: " + zapas;
                    if (!lek.getKiedyPowiadomienie().equals("0")){
                       txt += " (powiadomienie: " + lek.getKiedyPowiadomienie() +")";
                    }

                    list.add(txt);
                    i++;
                    positions.add(snapshot.getKey());
                    //Toast.makeText(WyswietlanieLekow.this, String.valueOf(snapshot.getKey()), Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = positions.get(position);
                Toast.makeText(WyswietlanieLekow.this, String.valueOf(reference.child(s).getKey()), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(WyswietlanieLekow.this, Edycja.class);
                Bundle bundle = new Bundle();
                bundle.putInt("licznik", Integer.parseInt(s));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });





    }
}
