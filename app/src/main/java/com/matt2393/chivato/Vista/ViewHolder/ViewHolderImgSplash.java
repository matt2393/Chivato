package com.matt2393.ind.Vista.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matt2393.ind.R;

public class ViewHolderImgSplash extends RecyclerView.ViewHolder{
    public ImageView img;
    public TextView elim;
    public ViewHolderImgSplash(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.img_splash);
        elim=itemView.findViewById(R.id.elim_img_splash);
    }
}
