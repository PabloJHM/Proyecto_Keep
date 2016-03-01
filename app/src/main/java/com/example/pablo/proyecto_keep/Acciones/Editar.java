package com.example.pablo.proyecto_keep.Acciones;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.pablo.proyecto_keep.BD.GestorNota;
import com.example.pablo.proyecto_keep.Entidades.Nota;
import com.example.pablo.proyecto_keep.R;


public class Editar extends AppCompatActivity {
    private EditText et;
    private GestorNota grn;
    private long idNota;
    private Nota n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        idNota=b.getLong("id");
        init();
    }

    public void init(){
        et=(EditText)findViewById(R.id.etEditar);
        grn = new GestorNota(this);
        n=grn.select("_ID = "+idNota).get(0);
        et.setText(n.getContenido());
    }

    public void guardarEdit(View v){
        n.setContenido(et.getText().toString());
        grn.nuevoContenido(n);
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void volverEdit(View v){
        finish();
    }
}
