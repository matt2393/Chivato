package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class TipoNotInteractor {
    private Query query;
    private FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderAdminGen> adapter;

    public interface OnLoadChangeViewListener {
        ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType);

        void populateViewHolder(ViewHolderAdminGen v, TipoNoticia u, int pos);
    }

    public void loadData(final TipoNotInteractor.OnLoadChangeViewListener onLoadListener) {
        query = FirebaseDatabase.getInstance()
                .getReference().child("TipoNoticia");

        adapter = new FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderAdminGen>(
                TipoNoticia.class, ViewHolderAdminGen.class, query, true) {
            @Override
            protected void populateViewHolder(ViewHolderAdminGen viewHolder, TipoNoticia model, int position) {
                onLoadListener.populateViewHolder(viewHolder, model, position);
            }

            @NonNull
            @Override
            public ViewHolderAdminGen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return onLoadListener.getViewHolder(parent, viewType);
            }
        };
    }

    public void loadDataAdmin(final TipoNotInteractor.OnLoadChangeViewListener onLoadListener){

    }

    public void starListeners() {
        if (adapter != null)
            adapter.addListeners();
    }

    public void stopListeners() {
        if (adapter != null)
            adapter.cleanListeners();
    }

    public FirebaseRecyclerAdapterMatt<TipoNoticia, ViewHolderAdminGen> getAdapter() {
        return adapter;
    }
}
