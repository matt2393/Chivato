package com.matt2393.tercerojo.Vista.View;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.tercerojo.Modelo.Miembro;
import com.matt2393.tercerojo.Vista.ViewHolder.ViewHolderAdminGen;

public interface MiembroView {
    ViewHolderAdminGen loadViewHolder(@NonNull ViewGroup parent, int viewType);
    void populateViewHolder(ViewHolderAdminGen v, Miembro miembro, int pos);


    void loadData();
}
