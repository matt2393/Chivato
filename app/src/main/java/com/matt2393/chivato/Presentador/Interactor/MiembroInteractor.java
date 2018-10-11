package com.matt2393.tercerojo.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.matt2393.tercerojo.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.tercerojo.Modelo.Miembro;
import com.matt2393.tercerojo.Tools.Tools;
import com.matt2393.tercerojo.Vista.ViewHolder.ViewHolderAdminGen;

public class MiembroInteractor {
    private Query query;
    private FirebaseRecyclerAdapterMatt<Miembro, ViewHolderAdminGen> adapter;

    public interface OnFinishMiembro{
        ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType);

        void populateViewHolder(ViewHolderAdminGen v, Miembro m, int pos);
    }

    public void loadData(final OnFinishMiembro onLoadListener) {

        query = FirebaseDatabase.getInstance()
                .getReference().child("Miembro");

        adapter = new FirebaseRecyclerAdapterMatt<Miembro, ViewHolderAdminGen>(
                Miembro.class, ViewHolderAdminGen.class, query, true) {
            @Override
            protected void populateViewHolder(ViewHolderAdminGen viewHolder, Miembro model, int position) {
                onLoadListener.populateViewHolder(viewHolder, model, position);
            }

            @NonNull
            @Override
            public ViewHolderAdminGen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    public FirebaseRecyclerAdapterMatt<Miembro, ViewHolderAdminGen> getAdapter() {
        return adapter;
    }
}
