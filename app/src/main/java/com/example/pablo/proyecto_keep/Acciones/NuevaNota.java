package com.example.pablo.proyecto_keep.Acciones;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pablo.proyecto_keep.BD.GestorNota;
import com.example.pablo.proyecto_keep.Entidades.Nota;
import com.example.pablo.proyecto_keep.Entidades.Usuario;
import com.example.pablo.proyecto_keep.Gestion.GestionNota;
import com.example.pablo.proyecto_keep.R;

import java.util.List;

public class NuevaNota extends AppCompatActivity {
    private EditText et;
    private Usuario user;
    private GestionNota gn;
    private GestorNota grn;
    private List<Nota> listaNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_nota);
        grn = new GestorNota(this);
        gn = new GestionNota(this);
        et=(EditText)findViewById(R.id.editText);
        user = getIntent().getParcelableExtra("user");
        listaNotas=grn.select();
    }

    public void guardarNota(View v){
        if(et.getText().toString().isEmpty()){
            Toast.makeText(this, "Nota vacia!", Toast.LENGTH_LONG);
        } else {
            Nota n = new Nota(gn.getNextAndroidId(listaNotas), et.getText().toString(), user.getUsuario(), false);
            grn.insert(n);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    public void volver(View v){
        finish();
    }


}
