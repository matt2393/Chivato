package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.matt2393.chivato.Modelo.Noticia;

public class AddNotInteractor {

    public interface OnFinishSaveNot{
        void success(String key);
        void error(String e);
        void errorEmptyTitulo();
        void errorEmptyDesc();
    }

    public void guardar(Noticia not,final OnFinishSaveNot onFinishSaveNot){
        if(TextUtils.isEmpty(not.getTitulo())){
            onFinishSaveNot.errorEmptyTitulo();
            return;

        }
        if(TextUtils.isEmpty(not.getDescripcion())){
            onFinishSaveNot.errorEmptyDesc();
            return;
        }
        FirebaseDatabase.getInstance().getReference()
                .child("Noticia").push().setValue(not, (databaseError, databaseReference) -> {
                    if(databaseError!=null){
                        onFinishSaveNot.error(databaseError.getMessage());
                        return;
                    }
                    onFinishSaveNot.success(databaseReference.getKey());
                });

    }
}
