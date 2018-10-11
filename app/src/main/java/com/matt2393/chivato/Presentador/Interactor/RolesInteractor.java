package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class RolesInteractor {
    private Query query;
    private FirebaseRecyclerAdapterMatt<RolUsuario, ViewHolderAdminGen> adapter;

    public interface OnLoadChangeViewListener {
        ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType);

        void populateViewHolder(ViewHolderAdminGen v, RolUsuario rol, int pos);
    }

    public void loadData(final RolesInteractor.OnLoadChangeViewListener onLoadListener) {
        query = FirebaseDatabase.getInstance()
                .getReference().child("RolUsuario");

        adapter = new FirebaseRecyclerAdapterMatt<RolUsuario, ViewHolderAdminGen>(
                RolUsuario.class, ViewHolderAdminGen.class, query, true) {
            @Override
            protected void populateViewHolder(ViewHolderAdminGen viewHolder, RolUsuario model, int position) {
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

    public FirebaseRecyclerAdapterMatt<RolUsuario, ViewHolderAdminGen> getAdapter() {
        return adapter;
    }
}
