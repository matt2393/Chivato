package com.matt2393.chivato.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.matt2393.chivato.Firebase.CompareAndFilter;

import java.util.HashMap;

public class RolUsuario implements CompareAndFilter<RolUsuario>,Parcelable {
    private String key;
    private String titulo;
    /**
     * Permisos que tiene a un contenido, la key del hashMap es la key de tipo de noticia
     * el valor booleano, si es true tiene permiso de escritura, si es false, solo tiene
     * permisos de lectura.
     */
    private HashMap<String, Boolean> contenido;
    private HashMap<String, Boolean> lectura;
    /**
     * variable para detectar que este tipo de usuario es admin y puede edita,
     * añadir o eliminar usuarios, contenido y roles.
     */
    private boolean admin;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public HashMap<String, Boolean> getLectura() {
        return lectura;
    }

    public void setLectura(HashMap<String, Boolean> lectura) {
        this.lectura = lectura;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public HashMap<String, Boolean> getContenido() {
        return contenido;
    }

    public void setContenido(HashMap<String, Boolean> contenido) {
        this.contenido = contenido;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public RolUsuario() {
    }


    /**
     *
     */
    @Override
    public int compareTo(RolUsuario o) {
        return 0;
    }

    @Override
    public String getKeyClass() {
        return this.key;
    }

    @Override
    public boolean filter(String wordKey) {
        String aux=wordKey.toLowerCase();
        if(this.titulo.toLowerCase().contains(aux))
            return true;

        return false;
    }

    @Override
    public void setKeyClass(String key) {
        this.key=key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.titulo);
        dest.writeSerializable(this.contenido);
        dest.writeSerializable(this.lectura);
        dest.writeByte(this.admin ? (byte) 1 : (byte) 0);
        dest.writeInt(this.color);
    }

    protected RolUsuario(Parcel in) {
        this.key = in.readString();
        this.titulo = in.readString();
        this.contenido = (HashMap<String, Boolean>) in.readSerializable();
        this.lectura = (HashMap<String, Boolean>) in.readSerializable();
        this.admin = in.readByte() != 0;
        this.color = in.readInt();
    }

    public static final Creator<RolUsuario> CREATOR = new Creator<RolUsuario>() {
        @Override
        public RolUsuario createFromParcel(Parcel source) {
            return new RolUsuario(source);
        }

        @Override
        public RolUsuario[] newArray(int size) {
            return new RolUsuario[size];
        }
    };
}
