package com.example.database;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Usuwanie extends AppCompatDialogFragment {

    private UsuwanieListener usuwanie;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Uwaga!")
                .setMessage("Czy na pewno chcesz usunąć lek?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usuwanie.potwierdz();
                        //Toast.makeText(Usuwanie.class, "Usun", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();

    }

    public interface UsuwanieListener {
        void potwierdz();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            usuwanie = (UsuwanieListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "musi implementować UsuwanieListener");
        }
    }
}
