package com.matt2393.chivato.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.matt2393.chivato.Firebase.CompareAndFilter;

public class Usuario implements CompareAndFilter<Usuario>,Parcelable {
    private String key,email,password;
    private String nombre,apellido,keyRol;
    private String urlImage;
    private boolean activo;
    private boolean suAdmin,admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isSuAdmin() {
        return suAdmin;
    }

    public void setSuAdmin(boolean suAdmin) {
        this.suAdmin = suAdmin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getKeyRol() {
        return keyRol;
    }

    public void setKeyRol(String keyRol) {
        this.keyRol = keyRol;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    /**
     * metodos de CompareAndFilter
     */
    @Override
    public int compareTo(Usuario o) {
        return 0;
    }

    @Override
    public String getKeyClass() {
        return this.key;
    }

    @Override
    public boolean filter(String wordKey) {
        String aux=wordKey.toLowerCase();
        return nombre.toLowerCase().contains(aux) || apellido.toLowerCase().contains(aux)
                || email.toLowerCase().contains(aux);
    }

    @Override
    public void setKeyClass(String key) {
        this.key=key;
    }

    public Usuario() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.nombre);
        dest.writeString(this.apellido);
        dest.writeString(this.keyRol);
        dest.writeString(this.urlImage);
        dest.writeByte(this.activo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.suAdmin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.admin ? (byte) 1 : (byte) 0);
    }

    protected Usuario(Parcel in) {
        this.key = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.nombre = in.readString();
        this.apellido = in.readString();
        this.keyRol = in.readString();
        this.urlImage = in.readString();
        this.activo = in.readByte() != 0;
        this.suAdmin = in.readByte() != 0;
        this.admin = in.readByte() != 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
