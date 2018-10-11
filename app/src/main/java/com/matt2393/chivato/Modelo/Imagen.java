package com.matt2393.chivato.Modelo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.matt2393.chivato.Firebase.CompareAndFilter;

public class Imagen implements Comparable<Imagen>,Parcelable , CompareAndFilter<Imagen>{
    private String url,ext,key;
    private int pos;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Override
    public int compareTo(@NonNull Imagen o) {
        if(this.pos>o.getPos())
            return 1;
        else
            if(this.pos<o.getPos())
                return -1;
        return 0;
    }

    @Override
    public String getKeyClass() {
        return this.key;
    }

    @Override
    public boolean filter(String wordKey) {
        return false;
    }

    @Override
    public void setKeyClass(String key) {
        this.key=key;
    }

    public Imagen() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.ext);
        dest.writeString(this.key);
        dest.writeInt(this.pos);
    }

    protected Imagen(Parcel in) {
        this.url = in.readString();
        this.ext = in.readString();
        this.key = in.readString();
        this.pos = in.readInt();
    }

    public static final Creator<Imagen> CREATOR = new Creator<Imagen>() {
        @Override
        public Imagen createFromParcel(Parcel source) {
            return new Imagen(source);
        }

        @Override
        public Imagen[] newArray(int size) {
            return new Imagen[size];
        }
    };
}
