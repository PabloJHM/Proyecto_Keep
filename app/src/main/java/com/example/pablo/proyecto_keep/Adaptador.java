package com.example.pablo.proyecto_keep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pablo.proyecto_keep.Entidades.Nota;

import java.util.List;

public class Adaptador extends ArrayAdapter<Nota> {
    private Context ctx;
    private int res;
    private LayoutInflater lInflator;
    private List<Nota> valores;

    static class ViewHolder {
        public TextView tv1;
    }

    public Adaptador(Context context, int resource, List<Nota> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.res = resource;
        this.valores = objects;
        this.lInflator = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1
        ViewHolder gv = new ViewHolder();
        if(convertView==null){
            convertView = lInflator.inflate(res, null);
            TextView tv = (TextView) convertView.findViewById(R.id.textView);
            gv.tv1 = tv;
            convertView.setTag(gv);
        } else {
            gv = (ViewHolder) convertView.getTag();
        }
        gv.tv1.setText(valores.get(position).getContenido().replace("|"," "));
        return convertView;
    }
}
