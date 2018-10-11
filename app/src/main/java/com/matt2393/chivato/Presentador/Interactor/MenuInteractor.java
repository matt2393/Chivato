package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderMenu;

import java.util.ArrayList;

public class MenuInteractor {
    private Query query;
    private FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderMenu> adapter;

    private ValueEventListener valueEventListener;


    public interface OnFinishLoad{
        void onPopulateViewH(@NonNull ViewHolderMenu v, TipoNoticia not,int pos);
        ViewHolderMenu onCreateViewHolder(ViewGroup vg,int typeView);
        void error();

        void changeData(ArrayList<TipoNoticia> tipo_not);
    }

    public void loadView(String key, final OnFinishLoad onFinishLoad, boolean isAdmin){

        if(Tools.usuario!=null && Tools.usuario.isSuAdmin())
            query = FirebaseDatabase.getInstance().getReference()
                    .child("TipoNoticia");
        else
        if(isAdmin)
            query = FirebaseDatabase.getInstance().getReference()
                    .child("TipoNoticia");
        else {
            if (key != null) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("TipoNoticia").orderByChild(key).equalTo(true);

            } else
                query = FirebaseDatabase.getInstance().getReference()
                        .child("TipoNoticia").orderByChild("publico").equalTo(true);
        }
        adapter=new FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderMenu>(
                TipoNoticia.class,ViewHolderMenu.class,query,true) {
            @Override
            protected void populateViewHolder(ViewHolderMenu viewHolder, TipoNoticia model, int position) {
                onFinishLoad.onPopulateViewH(viewHolder,model,position);
            }

            @NonNull
            @Override
            public ViewHolderMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return onFinishLoad.onCreateViewHolder(parent,viewType);
            }
        };

    }

    public void loadViews(String key, final OnFinishLoad onFinishLoad, boolean isAdmin){

        if(isAdmin)
            query = FirebaseDatabase.getInstance().getReference()
                    .child("TipoNoticia");
        else {
            if (key != null) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("TipoNoticia").orderByChild(key).equalTo(true);

            } else
                query = FirebaseDatabase.getInstance().getReference()
                        .child("TipoNoticia").orderByChild("publico").equalTo(true);
        }
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                    ArrayList<TipoNoticia> nots=new ArrayList<>();
                    for (DataSnapshot dd:dataSnapshot.getChildren()) {
                        TipoNoticia t=dd.getValue(TipoNoticia.class);
                        if(t!=null)
                            t.setKey(dd.getKey());
                        nots.add(t);
                    }

                    onFinishLoad.changeData(nots);
                }
                else
                    onFinishLoad.error();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFinishLoad.error();
            }
        };

    }

    public void addListeners(){
        if(query!=null && valueEventListener!=null)
            query.addValueEventListener(valueEventListener);
    }
    public void removeListeners(){
        if(query!=null && valueEventListener!=null)
            query.removeEventListener(valueEventListener);
    }

    public FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderMenu> getAdapter() {
        return adapter;
    }
}
