package com.example.dark.gamebaucua;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;


/**
 * Created by Dark on 3/23/2017.
 */

public class Custom_GridView_BanCo extends ArrayAdapter<Integer> {
    Context context;
    int resource;
    Integer[] object;
    Integer[] giatien={0,100,200,300,400,500};
    ArrayAdapter<Integer> adapter;
    public Custom_GridView_BanCo(Context context,int resource ,Integer[] objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.object=objects;
        adapter=new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,giatien);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context,resource,null);
        ImageView imgBanCo=(ImageView) view.findViewById(R.id.imgbanco);
        Spinner spnTiecCuoc=(Spinner) view.findViewById(R.id.spngiatien);
        imgBanCo.setImageResource(object[position]);
        spnTiecCuoc.setAdapter(adapter);
        spnTiecCuoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionspn, long id) {
            MainActivity.giatiendatcuoc[position]=giatien[positionspn];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
