package com.matt2393.chivato.Vista.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.matt2393.chivato.R;

public class ViewHolderAddContRol extends RecyclerView.ViewHolder {

    public TextView titulo;
    public CheckBox lectura,escritura;
    public ViewHolderAddContRol(View itemView) {
        super(itemView);
        titulo=itemView.findViewById(R.id.rec_titulo_tipo_not_rol);
        lectura=itemView.findViewById(R.id.rec_lectura);
        escritura=itemView.findViewById(R.id.rec_escritura);
    }
}
