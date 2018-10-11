package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.TipoNoticia;

public class AddTipoNotInteractor {

    private ValueEventListener valueEventListener;
    public interface OnFinishSaveAddTipoNot{
        void success();
        void error(String e);
        void errorNombre();
    }

    public void guardar(TipoNoticia tipoNoticia, final OnFinishSaveAddTipoNot onFinishSaveAddTipoNot){
        if(!TextUtils.isEmpty(tipoNoticia.getNombre())) {
            FirebaseDatabase.getInstance().getReference()
                    .child("TipoNoticia")
                    .push().setValue(tipoNoticia, (databaseError, databaseReference) -> {
                        if(databaseError!=null) {
                            onFinishSaveAddTipoNot.error(databaseError.getMessage());
                            return;
                        }
                        if(tipoNoticia.isPublico()){
                            editRoles(databaseReference.getKey(),true);
                        }
                        onFinishSaveAddTipoNot.success();
                    });



        }
        else
            onFinishSaveAddTipoNot.errorNombre();
    }
    public void editar(String key,TipoNoticia tipoNoticia, final OnFinishSaveAddTipoNot onFinishSaveAddTipoNot){
        if(!TextUtils.isEmpty(tipoNoticia.getNombre())) {
            FirebaseDatabase.getInstance().getReference()
                    .child("TipoNoticia").child(key)
                    .updateChildren(tipoNoticia.toMap())
                    //.setValue(tipoNoticia)
                    .addOnSuccessListener(v -> {
                        editRoles(key, tipoNoticia.isPublico());
                        onFinishSaveAddTipoNot.success();
                    })
                    .addOnFailureListener(e ->
                            onFinishSaveAddTipoNot.error(e.getMessage())
                    );
        }
        else
            onFinishSaveAddTipoNot.errorNombre();
    }

    private void editRoles(String keyTipo, boolean dato){
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("RolUsuario");
        final DatabaseReference ref2=FirebaseDatabase.getInstance()
                .getReference().child("TipoNoticia")
                .child(keyTipo);

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    if(d.getKey()!=null) {
                        ref.child(d.getKey())
                                .child("lectura")
                                .child(keyTipo)
                                .setValue(dato);
                        ref2.child(d.getKey())
                                .setValue(dato);
                    }
                }
                ref.removeEventListener(valueEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ref.removeEventListener(valueEventListener);
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }
}
