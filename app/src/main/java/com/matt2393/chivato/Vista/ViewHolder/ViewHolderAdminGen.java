package com.matt2393.chivato.Vista.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matt2393.chivato.R;

public class ViewHolderAdminGen extends RecyclerView.ViewHolder {

    public TextView titulo,aux;
    public ImageView edit,elim,aux_img;
    public ViewHolderAdminGen(View itemView) {
        super(itemView);
        titulo=itemView.findViewById(R.id.titulo_admin_gen);
        edit=itemView.findViewById(R.id.edit_admin_gen);
        elim=itemView.findViewById(R.id.elim_admin_gen);
        aux=itemView.findViewById(R.id.aux_admin_gen);
        aux_img=itemView.findViewById(R.id.aux_img_admin_gen);
    }
}
