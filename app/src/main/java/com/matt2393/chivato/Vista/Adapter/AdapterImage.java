package com.matt2393.chivato.Vista.Adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.matt2393.chivato.R;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderImages;

import java.util.ArrayList;

public class AdapterImage extends RecyclerView.Adapter<ViewHolderImages>{
    private ArrayList<Uri> images;

    public AdapterImage(ArrayList<Uri> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolderImages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderImages(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_image_add_not,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImages holder, int position) {
        holder.image.setImageURI(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public ArrayList<Uri> getImages() {
        return images;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
        notifyDataSetChanged();
    }
}
