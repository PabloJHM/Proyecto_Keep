package com.example.pablo.proyecto_keep.Entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private String usuario,pass;

    public Usuario(String usuario, String pass) {
        this.usuario = usuario;
        this.pass = pass;
    }

    protected Usuario(Parcel in) {
        usuario = in.readString();
        pass = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usuario);
        dest.writeString(pass);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "pass='" + pass + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
