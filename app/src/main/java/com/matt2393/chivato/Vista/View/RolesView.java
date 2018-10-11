package com.matt2393.chivato.Vista.View;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public interface RolesView {

    ViewHolderAdminGen loadViewHolder(@NonNull ViewGroup parent, int viewType);
    void populateViewHolder(ViewHolderAdminGen v, RolUsuario rol, int pos);


    void loadData();
    void errorData();
    void emptyData();
}
