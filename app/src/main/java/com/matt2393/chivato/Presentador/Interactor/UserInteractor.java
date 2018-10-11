package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class UserInteractor {
    private Query query;
    private FirebaseRecyclerAdapterMatt<Usuario, ViewHolderAdminGen> adapter;

    public interface OnLoadChangeViewListener {
        ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType);

        void populateViewHolder(ViewHolderAdminGen v, Usuario u, int pos);
    }

    public void loadData(final UserInteractor.OnLoadChangeViewListener onLoadListener) {

        if(Tools.usuario!=null && Tools.usuario.isSuAdmin()) {
            query = FirebaseDatabase.getInstance()
                    .getReference().child("Usuario");
        }
        else {
            query = FirebaseDatabase.getInstance()
                    .getReference().child("Usuario")
                    .orderByChild("suAdmin").equalTo(false);
        }

        adapter = new FirebaseRecyclerAdapterMatt<Usuario, ViewHolderAdminGen>(
                Usuario.class, ViewHolderAdminGen.class, query, true) {
            @Override
            protected void populateViewHolder(ViewHolderAdminGen viewHolder, Usuario model, int position) {
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

    public FirebaseRecyclerAdapterMatt<Usuario, ViewHolderAdminGen> getAdapter() {
        return adapter;
    }
}
