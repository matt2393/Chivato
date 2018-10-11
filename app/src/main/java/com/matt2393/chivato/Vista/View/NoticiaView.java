package com.matt2393.chivato.Vista.View;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.chivato.Modelo.Noticia;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderNoticia;

public interface NoticiaView {

    ViewHolderNoticia loadViewHolder(@NonNull ViewGroup parent,int viewType);
    void populateViewHolder(ViewHolderNoticia v, Noticia not, int pos);


    void loadData();
    void errorData();
    void emptyData();
}
