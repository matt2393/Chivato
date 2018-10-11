package com.matt2393.chivato.Vista.ViewHolder;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.matt2393.chivato.R;

public class ViewHolderNoticia extends RecyclerView.ViewHolder {

    public TextView titulo,descr,fecha, nom_user;
    public LottieAnimationView descarga;
    public ImageView img1,img2,img3, img_ext,elim, img_user;
    public LinearLayout conten1, contenAllImg;
    public CoordinatorLayout conten2,contenMas;
    public TextView textMasImg;
    public CardView conten_arch,conten_img_user;
    public ViewHolderNoticia(View itemView) {
        super(itemView);
        titulo=itemView.findViewById(R.id.titulo_noticia);
        descr=itemView.findViewById(R.id.descripcion_noticia);
        fecha=itemView.findViewById(R.id.fecha_publi_noticia);
        descarga=itemView.findViewById(R.id.descarga_noticia);
        img1=itemView.findViewById(R.id.image_noticia_1);
        img2=itemView.findViewById(R.id.image_noticia_2);
        img3=itemView.findViewById(R.id.image_noticia_3);

        contenAllImg=itemView.findViewById(R.id.contenedor_all_img_not);
        conten1=itemView.findViewById(R.id.contenedor_img_not);
        conten2=itemView.findViewById(R.id.contenedor_img_2_not);
        contenMas=itemView.findViewById(R.id.contenedor_mas_img_not);

        textMasImg=itemView.findViewById(R.id.text_mas_img_not);
        img_ext=itemView.findViewById(R.id.img_tipo_doc);

        elim=itemView.findViewById(R.id.delete_noticia);

        nom_user=itemView.findViewById(R.id.nombre_user_not);
        img_user=itemView.findViewById(R.id.img_user_not);
        conten_arch=itemView.findViewById(R.id.conten_arch);

        conten_img_user=itemView.findViewById(R.id.conten_img_user_not);
    }
}
