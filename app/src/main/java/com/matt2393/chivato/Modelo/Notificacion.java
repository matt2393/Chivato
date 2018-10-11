package com.matt2393.chivato.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Notificacion {
    public String key,keyTipo,titulo,descripcion,imagen;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyTipo() {
        return keyTipo;
    }

    public void setKeyTipo(String keyTipo) {
        this.keyTipo = keyTipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }




    public Notificacion() {
    }

}
