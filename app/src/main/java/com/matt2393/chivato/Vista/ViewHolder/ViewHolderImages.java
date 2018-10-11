package com.matt2393.chivato.Vista.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.matt2393.chivato.R;

public class ViewHolderImages extends RecyclerView.ViewHolder {
    public ImageView image;
    public ViewHolderImages(View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.img_add_not);
    }
}
