package com.example.pablo.proyecto_keep.BD;

import android.provider.BaseColumns;

public class Tablas {
    private Tablas(){}

    public static abstract class TablaNota implements BaseColumns {
        public static final String TABLA= "nota";
        public static final String CONTENIDO ="contenido";
        public static final String SUBIDA= "subida";
        public static final String USUARIO= "usuario";
    }
}
