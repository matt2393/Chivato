package com.matt2393.chivato.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.matt2393.chivato.Firebase.CompareAndFilter;

import java.util.HashMap;
import java.util.Map;

public class TipoNoticia implements CompareAndFilter<TipoNoticia>,Parcelable {
    private String key;
    private String nombre,urlIcon;
    private boolean publico;

    /**
     * Roles que pueden ver este tipo de noticia
     */

    private HashMap<String, Boolean> roles;


    /**
     * ToMap
     */

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("nombre",this.nombre);
        map.put("urlIcon",this.urlIcon);
        map.put("publico",this.publico);
        map.put("roles",this.roles);

        return map;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public HashMap<String, Boolean> getRoles() {
        return roles;
    }

    public void setRoles(HashMap<String, Boolean> roles) {
        this.roles = roles;
    }

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    /**
     * métodos de la interface CompareAndFilter
     */

    @Override
    public int compareTo(TipoNoticia o) {
        return 0;
    }

    @Override
    public String getKeyClass() {
        return this.key;
    }

    @Override
    public boolean filter(String wordKey) {
        String aux=wordKey.toLowerCase();
        if(this.nombre.toLowerCase().contains(aux))
            return true;
        return false;
    }

    @Override
    public void setKeyClass(String key) {
        this.key=key;
    }




    public TipoNoticia() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.nombre);
        dest.writeString(this.urlIcon);
        dest.writeByte(this.publico ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.roles);
    }

    protected TipoNoticia(Parcel in) {
        this.key = in.readString();
        this.nombre = in.readString();
        this.urlIcon = in.readString();
        this.publico = in.readByte() != 0;
        this.roles = (HashMap<String, Boolean>) in.readSerializable();
    }

    public static final Creator<TipoNoticia> CREATOR = new Creator<TipoNoticia>() {
        @Override
        public TipoNoticia createFromParcel(Parcel source) {
            return new TipoNoticia(source);
        }

        @Override
        public TipoNoticia[] newArray(int size) {
            return new TipoNoticia[size];
        }
    };
}
