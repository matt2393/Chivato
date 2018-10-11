package com.matt2393.tercerojo.Vista.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matt2393.tercerojo.Modelo.Miembro;
import com.matt2393.tercerojo.R;

public class DialogMiembro extends DialogFragment {

    public final static String TAG = "DialogMiembro";
    private final static String MIEMBRO = "miembro";

    private TextInputEditText nombre, grado;
    private Button guardar;
    private Miembro miembro;
    private DialogLoad load;

    public static DialogMiembro newInstance(Miembro m) {
        Bundle bn = new Bundle();
        bn.putParcelable(MIEMBRO, m);
        DialogMiembro d = new DialogMiembro();
        d.setArguments(bn);
        return d;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_miembro, null);

        nombre = view.findViewById(R.id.nombre_miembro);
        grado = view.findViewById(R.id.grado_miembro);
        guardar = view.findViewById(R.id.guardar_miembro);

        load=DialogLoad.newInstance();

        if (getArguments() != null)
            miembro = getArguments().getParcelable(MIEMBRO);

        if (miembro != null) {
            nombre.setText(miembro.getNombre());
            grado.setText(miembro.getGrado());
        }


        guardar.setOnClickListener(v -> {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Miembro");
            load.show(getActivity().getSupportFragmentManager(),DialogLoad.TAG);
            String nom,gr;
            nom=nombre.getText().toString();
            gr=grado.getText().toString();
            if(TextUtils.isEmpty(nom) || TextUtils.isEmpty(gr)){
                Toast.makeText(getActivity(),
                        "Llene todos los datos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (miembro == null) {
                miembro=new Miembro();
                miembro.setNombre(nombre.getText().toString());
                miembro.setGrado(grado.getText().toString());
                ref.push().setValue(miembro)
                        .addOnSuccessListener(aVoid -> {
                            load.dismiss();
                            dismiss();
                        })
                        .addOnFailureListener(e -> {
                            load.dismiss();
                            Toast.makeText(getActivity(),
                                    "No se pudo guardar, intente nuevamente", Toast.LENGTH_SHORT).show();
                        });

            }
            else{
                miembro.setNombre(nom);
                miembro.setGrado(gr);
                ref.child(miembro.getKey())
                        .updateChildren(miembro.toMap())
                        .addOnSuccessListener(aVoid -> {
                            load.dismiss();
                            dismiss();
                        })
                        .addOnFailureListener(e -> {
                            load.dismiss();
                            Toast.makeText(getActivity(),
                                    "No se pudo guardar, intente nuevamente", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        alert.setView(view);
        return alert.create();
    }
}
