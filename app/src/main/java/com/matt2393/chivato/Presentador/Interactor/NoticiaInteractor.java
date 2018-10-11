package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.Noticia;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderNoticia;

public class NoticiaInteractor {
    private Query query;
    private FirebaseRecyclerAdapterMatt<Noticia, ViewHolderNoticia> adapter;

    public interface OnLoadChangeViewListener {
        ViewHolderNoticia getViewHolder(@NonNull ViewGroup parent, int viewType);

        void populateViewHolder(ViewHolderNoticia v, Noticia n, int pos);
    }

    public void loadData(final String keyCont, final OnLoadChangeViewListener onLoadListener) {
        query = FirebaseDatabase.getInstance()
                .getReference().child("Noticia")
                .orderByChild(keyCont).equalTo(true);

        adapter = new FirebaseRecyclerAdapterMatt<Noticia, ViewHolderNoticia>(
                Noticia.class, ViewHolderNoticia.class, query, true) {
            @Override
            protected void populateViewHolder(ViewHolderNoticia viewHolder, Noticia model, int position) {
                onLoadListener.populateViewHolder(viewHolder, model, position);
            }

            @NonNull
            @Override
            public ViewHolderNoticia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return onLoadListener.getViewHolder(parent, viewType);
            }
        };


    }



    public void starListeners() {
        if (adapter != null)
            adapter.addListeners();
    }

    public void stopListeners() {
        if (adapter != null)
            adapter.cleanListeners();
    }

    public FirebaseRecyclerAdapterMatt<Noticia, ViewHolderNoticia> getAdapter() {
        return adapter;
    }

    public void setAdapter(FirebaseRecyclerAdapterMatt<Noticia, ViewHolderNoticia> adapter) {
        this.adapter = adapter;
    }
}
