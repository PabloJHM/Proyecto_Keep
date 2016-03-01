package com.example.pablo.proyecto_keep.Gestion;

import android.content.Context;

import com.example.pablo.proyecto_keep.BD.GestorNota;
import com.example.pablo.proyecto_keep.Entidades.Nota;
import com.example.pablo.proyecto_keep.Entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GestionNota {

    private GestorNota gn;
    private String urlnet = "http://192.168.0.154:8080/ParteNetbeans/go";

    public GestionNota(Context c) {
        this.gn= new GestorNota(c);
    }

    public List<Nota> getNotasUsuario(Usuario u) {
        List<Nota> listaNotas = new ArrayList<>();
        URL url;
        BufferedReader in;
        String res = "";
        String login;
        try {
            login = URLEncoder.encode(u.getUsuario(), "UTF-8");
            String destino = urlnet + "?tabla=keep&op=read&login=" + login + "&origen=android&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea = in.readLine()) != null) {
                res += linea;
            }
            in.close();
            JSONObject obj = new JSONObject(res);
            JSONArray array = (JSONArray) obj.get("r");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = (JSONObject) array.get(i);
                Nota n = new Nota(o.getInt("ida"), o.getString("cont").replaceAll("\\s","|"), u.getUsuario() ,true);
                listaNotas.add(n);
            }
            return listaNotas;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
        }
        return null;
    }

    public long getNextAndroidId(List<Nota> ln) {
        long next = -1;
        for (Nota n : ln) {
            if (n.getId() > next) {
                next = n.getId();
            }
        }
        return next+1;
    }

    public List<Nota> subirNota(List<Nota> ln, Usuario u) {
        gn.open();
        List<Nota> listaNotas= new ArrayList<>();
        URL url;
        BufferedReader in;
        String res = "";
        String login;
        List<Nota> notasUsuario= getNotasUsuario(u);
        try {
            login = URLEncoder.encode(u.getUsuario(), "UTF-8");
            for (Nota n : ln) {
                if(!n.isSubida()) {
                    String contenido=n.getContenido().replaceAll("\\s","|");
                    if(notasUsuario.contains(n)){
                        String destino = urlnet + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + n.getId() + "&contenido=" + contenido + "&accion=";
                        url = new URL(destino);
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                    String destino = urlnet + "?tabla=keep&op=create&login=" + login + "&origen=android&idAndroid=" + n.getId() + "&contenido=" + contenido + "&accion=";
                    url = new URL(destino);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String linea;
                    while ((linea = in.readLine()) != null) {
                        res += linea;
                    }
                    in.close();
                    n.setSubida(true);
                    gn.nuevoEstado(n);
                }
                listaNotas.add(n);
            }
            gn.close();
            return listaNotas;

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        gn.close();
        return null;

    }

    public void borrarNota(Nota n, Usuario u){
        URL url;
        BufferedReader in = null;
        String res;
        String login;
        String contenido=n.getContenido().replaceAll("\\s","|");
        try {
            login = URLEncoder.encode(u.getUsuario(), "UTF-8");
            String destinor = urlnet + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + n.getId() + "&contenido=" + contenido + "&accion=";
            url = new URL(destinor);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }

    public void editarNota(Nota n, Usuario u){
        URL url;
        BufferedReader in = null;
        String res;
        String login;
        String contenido=n.getContenido().replaceAll("\\s","|");
        try {
            login = URLEncoder.encode(u.getUsuario(), "UTF-8");

            String destino = urlnet + "?tabla=keep&op=delete&login=" + login + "&origen=android&idAndroid=" + n.getId() + "&contenido=" +contenido + "&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));

            destino = urlnet + "?tabla=keep&op=create&login=" + login + "&origen=android&idAndroid=" + n.getId() + "&contenido=" + contenido + "&accion=";
            url = new URL(destino);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
    }
}
