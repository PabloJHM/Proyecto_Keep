package com.example.pablo.proyecto_keep;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pablo.proyecto_keep.Acciones.Editar;
import com.example.pablo.proyecto_keep.Acciones.NuevaNota;
import com.example.pablo.proyecto_keep.BD.GestorNota;
import com.example.pablo.proyecto_keep.Entidades.Nota;
import com.example.pablo.proyecto_keep.Entidades.Usuario;
import com.example.pablo.proyecto_keep.Gestion.GestionNota;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Usuario user;
    private Adaptador ad;
    private List<Nota> listaNotas;
    private GestionNota gnn;
    private ListView lv;
    private GestorNota grn;
    private long idEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getIntent().getParcelableExtra("usuario");
        init();
    }

    @Override
    public void onPostResume(){
        super.onPostResume();
        generaAdaptador();
    }

    public void init(){
        gnn=new GestionNota(this);
        grn=new GestorNota(this);
        lv = (ListView) findViewById(R.id.listView);
        generaAdaptador();
    }

    public void generaAdaptador(){
        listaNotas=grn.select("usuario='"+user.getUsuario()+"'");
        ad=new Adaptador(this,R.layout.item_list,listaNotas);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
        ad.notifyDataSetChanged();
    }

    private int ANIADIR=1;
    public void add(View v){
        Intent i=new Intent (this, NuevaNota.class);
        i.putExtra("user", user);
        startActivityForResult(i, ANIADIR);
    }

    private int EDITAR=2;
    public void editar(int posicion){
        Intent i = new Intent( this,Editar.class);
        long idRec=listaNotas.get(posicion).getId();
        Bundle b=new Bundle();
        b.putLong("id",idRec);
        i.putExtras(b);
        idEditar=idRec;
        startActivityForResult(i, EDITAR);
    }

    public void synchro(View v){
        if (internetEnabled()) {
            KeepAsync a = new KeepAsync(user,"synchro",null);
            a.execute();
            generaAdaptador();
            Toast.makeText(this, R.string.subidas, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.noInter, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ANIADIR) {
            if (resultCode == RESULT_OK) {
                if (internetEnabled()) {
                    KeepAsync a = new KeepAsync(user,"subir",null);
                    a.execute();
                    generaAdaptador();
                    Toast.makeText(this, R.string.subidaser, Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == EDITAR) {
                if (resultCode == RESULT_OK) {
                    if (internetEnabled()) {
                        Nota n = grn.select("_ID = "+idEditar).get(0);
                        KeepAsync a = new KeepAsync(user,"editar",n);
                        a.execute();
                        generaAdaptador();
                        Toast.makeText(this, R.string.editaser, Toast.LENGTH_LONG).show();
                    }
                }
            }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo vistainfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = vistainfo.position;
        switch(item.getItemId()){
            case R.id.mnBorrar:
                long id=listaNotas.get(posicion).getId();
                Nota borrar=grn.select("_ID = "+id).get(0);
                if (internetEnabled()) {
                    KeepAsync a = new KeepAsync(user,"borrar",borrar);
                    a.execute();
                    Toast.makeText(this, R.string.deleteser, Toast.LENGTH_LONG).show();
                }
                grn.delete(borrar);
                generaAdaptador();
                return true;

            case R.id.mnEditar:
                editar(posicion);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public boolean internetEnabled() {
        ConnectivityManager m = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean is3g = m.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWiFi = m.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (is3g || isWiFi) {
            return true;
        }
        return false;
    }

    private class KeepAsync extends AsyncTask<Void, Void, Void> {
        private Usuario u;
        private String opcion;
        private Nota n;
        public KeepAsync(Usuario u,String opcion, Nota n){
            this.u=u;
            this.n=n;
            this.opcion=opcion;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(opcion.equals("subir")) {
                listaNotas = gnn.subirNota(listaNotas, u);
            } else if(opcion.equals("borrar")){
                gnn.borrarNota(n,u);
            } else if(opcion.equals("editar")){
                gnn.editarNota(n,u);
            } else if(opcion.equals("synchro")) {
                List<Nota> listaSynchro=gnn.getNotasUsuario(u);
                for(Nota n: listaSynchro){
                    if(n.getId()==0){
                        gnn.borrarNota(n, u);
                        n.setId(gnn.getNextAndroidId(listaNotas));
                        n.setSubida(false);
                        grn.insert(n);
                        listaNotas.add(n);
                    }
                }
                listaNotas=gnn.subirNota(listaNotas,u);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            generaAdaptador();
        }
    }


}
