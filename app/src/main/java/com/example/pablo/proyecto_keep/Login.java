package com.example.pablo.proyecto_keep;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pablo.proyecto_keep.Entidades.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Login extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String destino = "http://192.168.0.154:8080/ParteNetbeans/go";

    private HebraLogin hl =null;
    private EditText usuario;
    private EditText pass;
    private Usuario user;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usuario=(EditText)findViewById(R.id.etUser);
        pass=(EditText)findViewById(R.id.etPass);
    }

    public void logear(View v) {
        if (hl != null) {
            return;
        }

        String usuario = this.usuario.getText().toString();
        String password = pass.getText().toString();

        boolean cancel = false;

        if (usuario.isEmpty()) {
            Toast.makeText(Login.this, R.string.userVoid, Toast.LENGTH_LONG).show();
            cancel = true;
        }else if(password.isEmpty()) {
            Toast.makeText(Login.this, R.string.passVoid, Toast.LENGTH_LONG).show();
            cancel = true;
        }

        if (!cancel) {
            user = new Usuario(usuario,password);
            hl = new HebraLogin(user);
            hl.execute();
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        List<String> emails = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            emails.add(c.getString(ProfileQuery.ADDRESS));
            c.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    private void lanzaActividad(Usuario u){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("usuario",u);
        startActivity(i);
    }

    public class HebraLogin extends AsyncTask<Void, Void, Boolean> {
        private Usuario user;
        public HebraLogin(Usuario usu) {
            this.user=usu;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            URL url;
            BufferedReader in;
            String res = "";
            String login;
            String pass;
            try {
                login = URLEncoder.encode(user.getUsuario(), "UTF-8");
                pass = URLEncoder.encode(user.getPass(), "UTF-8");
                String ruta = destino+"?login="+login+"&pass="+pass+"&tabla=usuario&accion=&op=login&origen=android";
                url = new URL(ruta);
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                String linea;
                while ((linea = in.readLine()) != null) {
                    res += linea;
                    System.out.println(linea);
                }
                in.close();
                JSONObject obj = new JSONObject(res);
                return obj.getBoolean("r");
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }catch (JSONException e){
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            hl = null;
            if(success) {
                lanzaActividad(user);
            } else {
                Toast.makeText(Login.this, R.string.errorLogin, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            hl = null;
        }
    }
}
