package com.matt2393.chivato.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.matt2393.chivato.Firebase.CompareAndFilter;

import java.util.HashMap;
import java.util.Map;

public class Docente implements Parcelable, CompareAndFilter<Docente> {

    private String key, nombre;
    private Map<String,String> materias;

    public Map<String, String> getMaterias() {
        return materias;
    }

    public void setMaterias(Map<String, String> materias) {
        this.materias = materias;
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


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", this.nombre);
        map.put("grado", this.materias);
        return map;
    }


    @Override
    public int compareTo(Docente o) {
        return 0;
    }

    @Override
    public String getKeyClass() {
        return key;
    }

    @Override
    public boolean filter(String wordKey) {
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
        dest.writeString(this.nombre);
        dest.writeInt(this.materias.size());
        for (Map.Entry<String, String> entry : this.materias.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }



    public Docente() {
    }

    protected Docente(Parcel in) {
        this.key = in.readString();
        this.nombre = in.readString();
        int materiasSize = in.readInt();
        this.materias = new HashMap<String, String>(materiasSize);
        for (int i = 0; i < materiasSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.materias.put(key, value);
        }
    }

    public static final Creator<Docente> CREATOR = new Creator<Docente>() {
        @Override
        public Docente createFromParcel(Parcel source) {
            return new Docente(source);
        }

        @Override
        public Docente[] newArray(int size) {
            return new Docente[size];
        }
    };
}
