package com.matt2393.chivato.Vista.View;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderMenu;

import java.util.ArrayList;

public interface MenuView {

    ViewHolderMenu OnCreateViewHolder(@NonNull ViewGroup parent, int viewType);
    void populateViewHolderMenu(ViewHolderMenu menu, TipoNoticia tnot,int pos);
    void errorMenu();

    void changeDataMenu(ArrayList<TipoNoticia> tt);

}
