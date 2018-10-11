package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.Noticia;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAddContRol;

import java.util.HashMap;
import java.util.Map;

public class AddRolInteractor {

    private Query query,query1;
    private ValueEventListener valueEventListener;
    private FirebaseRecyclerAdapterMatt<TipoNoticia,ViewHolderAddContRol> adapter;
    public interface OnLoadSaveRol{
        void populateViewHolder(ViewHolderAddContRol v, TipoNoticia tn,int pos);
        ViewHolderAddContRol OnCreateViewHolder(ViewGroup v,int typeView);

        void success();
        void error(String e);
        void errorEmptyTit();
        void errorEmptyPermisos();

    }

    public void loadConten(final OnLoadSaveRol onLoadSaveRol){
        query= FirebaseDatabase.getInstance()
                .getReference().child("TipoNoticia");
        adapter=new FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderAddContRol>(
                TipoNoticia.class,ViewHolderAddContRol.class,query,true) {
            @Override
            protected void populateViewHolder(ViewHolderAddContRol viewHolder, TipoNoticia model, int position) {
                onLoadSaveRol.populateViewHolder(viewHolder, model, position);
            }

            @NonNull
            @Override
            public ViewHolderAddContRol onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return onLoadSaveRol.OnCreateViewHolder(parent,viewType);
            }
        };
    }


    public void edit(String key, RolUsuario rol, HashMap<String, Boolean> lectura, final OnLoadSaveRol onLoadSaveRol){
        if(TextUtils.isEmpty(rol.getTitulo())){
            onLoadSaveRol.errorEmptyTit();

            return;
        }

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("TipoNoticia");

        FirebaseDatabase.getInstance().getReference()
                .child("RolUsuario")
                .child(key).setValue(rol)
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        onLoadSaveRol.error("Error al guardar");
                    }
                    else {
                        for (Map.Entry<String, Boolean> dat : lectura.entrySet()) {
                            ref.child(dat.getKey()).child(key).setValue(dat.getValue());
                        }

                        query1=FirebaseDatabase.getInstance().getReference().child("Noticia");
                        valueEventListener=new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dd:dataSnapshot.getChildren()){
                                    Noticia no=dd.getValue(Noticia.class);
                                    if(dd.getKey()!=null && no!=null && no.getKeyRol()!=null
                                            && no.getKeyRol().equals(key)){
                                        FirebaseDatabase.getInstance()
                                                .getReference().child("Noticia")
                                                .child(dd.getKey())
                                                .child("colorRol").setValue(rol.getColor());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        };
                        query1.addListenerForSingleValueEvent(valueEventListener);

                        onLoadSaveRol.success();
                    }
        });

    }

    public void guardar(RolUsuario rol, HashMap<String, Boolean> lectura, final OnLoadSaveRol onLoadSaveRol){
        if(TextUtils.isEmpty(rol.getTitulo())){
            onLoadSaveRol.errorEmptyTit();

            return;
        }

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("TipoNoticia");


        FirebaseDatabase.getInstance().getReference()
                .child("RolUsuario")
                .push().setValue(rol, (databaseError, databaseReference) -> {
                    if(databaseError!=null){
                        onLoadSaveRol.error(databaseError.getMessage());
                        return;
                    }
                    String keyRol=databaseReference.getKey();
                    for (Map.Entry<String,Boolean> dat :lectura.entrySet()) {
                        ref.child(dat.getKey()).child(keyRol).setValue(dat.getValue());
                    }
                    onLoadSaveRol.success();
                });

    }

    public void addListener(){
        if(adapter!=null)
            adapter.addListeners();
    }
    public void removeListener(){
        if(adapter!=null)
            adapter.cleanListeners();

        if(query1!=null && valueEventListener!=null)
            query1.removeEventListener(valueEventListener);
    }

    public FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderAddContRol> getAdapter() {
        Log.e("MATT","rec_rol_adapter");
        return adapter;
    }
}
