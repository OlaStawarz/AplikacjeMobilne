package com.example.database;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PowiadomieniaDzisiaj extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dzisiaj_powiadomienia);

        listView = findViewById(R.id.listViewPowiadomienia);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.lek, list);
        listView.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lek");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Lek lek = snapshot.getValue(Lek.class);
                    String[] godzina = lek.getPowiadomienie().getGodzina().split(" ");
                    String[] dawka = lek.getPowiadomienie().getIlosc().split(" ");
                    String txt = "Nazwa: " + lek.getNazwa() + "\n";
                    StringBuilder pom = new StringBuilder();
                    for (int i = 0; i < godzina.length; i++){
                        pom.append(godzina[i] + ", " + dawka[i] + " " + lek.getJednostka() + "\n");
                    }
                    list.add(txt + pom);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
