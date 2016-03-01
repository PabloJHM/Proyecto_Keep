package com.example.pablo.proyecto_keep.Entidades;

public class Nota {
    private long id;
    private String contenido,usuario;
    private boolean subida;

    public Nota() {}

    public Nota(long id, String contenido, String usuario, boolean actualizada) {
        this.id = id;
        this.contenido = contenido;
        this.subida = actualizada;
        this.usuario=usuario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getUsuario() {return usuario;}

    public void setUsuario(String usuario) {this.usuario = usuario;}

    public boolean isSubida() {
        return subida;
    }

    public void setSubida(boolean subida) {
        this.subida = subida;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", contenido='" + contenido + '\'' +
                ", actualizada=" + subida +
                '}';
    }
}
