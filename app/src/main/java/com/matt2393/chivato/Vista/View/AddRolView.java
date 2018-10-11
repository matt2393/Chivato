package com.matt2393.chivato.Vista.View;

import android.view.ViewGroup;

import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAddContRol;

public interface AddRolView {
    void onSuccess();
    void onError(String e);
    void onErrorEmptyTit();
    void onErrorEmptyPermisos();
    void onPopulateViewHolder(ViewHolderAddContRol viewHolder, TipoNoticia tn, int pos);
    ViewHolderAddContRol OnCreateViewHolder(ViewGroup v, int typeView);

    void showProgress();
    void hideProgress();

}
