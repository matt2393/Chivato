package com.matt2393.chivato.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.matt2393.chivato.Firebase.CompareAndFilter;

import java.util.HashMap;

public class Noticia implements CompareAndFilter<Noticia>,Parcelable {
    private String key,titulo, descripcion;
    private HashMap<String, Imagen> imagenes;
    private String urlDoc,extDoc;
    private long hora_fecha;
    private int prio;
    private Usuario usuario;
    private int colorRol;
    private String keyRol;

    public String getKeyRol() {
        return keyRol;
    }

    public void setKeyRol(String keyRol) {
        this.keyRol = keyRol;
    }

    public int getColorRol() {
        return colorRol;
    }

    public void setColorRol(int colorRol) {
        this.colorRol = colorRol;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public HashMap<String, Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(HashMap<String, Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public String getUrlDoc() {
        return urlDoc;
    }

    public void setUrlDoc(String urlDoc) {
        this.urlDoc = urlDoc;
    }

    public long getHora_fecha() {
        return hora_fecha;
    }

    public void setHora_fecha(long hora_fecha) {
        this.hora_fecha = hora_fecha;
    }

    public int getPrio() {
        return prio;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }

    public String getExtDoc() {
        return extDoc;
    }

    public void setExtDoc(String extDoc) {
        this.extDoc = extDoc;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int compareTo(Noticia o) {
        if(this.prio>o.getPrio())
            return 1;
        else
            if(this.prio==o.getPrio() && this.hora_fecha>o.getHora_fecha())
                return 1;
        return 0;
    }

    @Override
    public String getKeyClass() {
        return this.key;
    }

    @Override
    public boolean filter(String wordKey) {
        String aux=wordKey.toLowerCase();
        return titulo.toLowerCase().contains(aux) || descripcion.toLowerCase().contains(aux);
    }

    @Override
    public void setKeyClass(String key) {
        this.key=key;
    }


    public Noticia() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.titulo);
        dest.writeString(this.descripcion);
        dest.writeSerializable(this.imagenes);
        dest.writeString(this.urlDoc);
        dest.writeString(this.extDoc);
        dest.writeLong(this.hora_fecha);
        dest.writeInt(this.prio);
        dest.writeParcelable(this.usuario, flags);
        dest.writeInt(this.colorRol);
        dest.writeString(this.keyRol);
    }

    protected Noticia(Parcel in) {
        this.key = in.readString();
        this.titulo = in.readString();
        this.descripcion = in.readString();
        this.imagenes = (HashMap<String, Imagen>) in.readSerializable();
        this.urlDoc = in.readString();
        this.extDoc = in.readString();
        this.hora_fecha = in.readLong();
        this.prio = in.readInt();
        this.usuario = in.readParcelable(Usuario.class.getClassLoader());
        this.colorRol = in.readInt();
        this.keyRol = in.readString();
    }

    public static final Creator<Noticia> CREATOR = new Creator<Noticia>() {
        @Override
        public Noticia createFromParcel(Parcel source) {
            return new Noticia(source);
        }

        @Override
        public Noticia[] newArray(int size) {
            return new Noticia[size];
        }
    };
}
