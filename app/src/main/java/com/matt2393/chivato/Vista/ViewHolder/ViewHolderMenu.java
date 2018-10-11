package com.matt2393.chivato.Vista.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.matt2393.chivato.R;

public class ViewHolderMenu extends RecyclerView.ViewHolder {
    public TextView item;
    public ViewHolderMenu(View itemView) {
        super(itemView);
        item=itemView.findViewById(R.id.item_menu);
    }
}
