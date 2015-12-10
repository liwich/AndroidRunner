package com.example.luismanuel.runner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class estadisticasActivity extends AppCompatActivity {

    private dataBase databaseHelper;
    private SQLiteDatabase dbr;
    private SQLiteDatabase dbw;
    private ListView listView;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        usuario= getIntent().getExtras().getString("usuario");

        databaseHelper=new dataBase(this,"Runner",null,1);
        dbr=databaseHelper.getReadableDatabase();
        dbw=databaseHelper.getWritableDatabase();

        listView= (ListView)findViewById(R.id.listView);

        ArrayList <String> id=new ArrayList<>();

        ArrayAdapter<String> tablaAdapter;


        Cursor c1=dbr.rawQuery("SELECT id FROM sesiones",null);

        if(c1.moveToFirst()){
            do{
                String sql="SELECT sesion_id, count(*)  FROM puntos WHERE sesion_id="+c1.getInt(0)+" GROUP BY sesion_id";
                Cursor c2=dbr.rawQuery(sql,null);
                if(c2.moveToFirst()){
                    if(c2.getString(1).equals("1")){
                        dbw.execSQL("DELETE FROM sesiones WHERE id="+c1.getInt(0));
                        dbw.execSQL("DELETE FROM puntos WHERE sesion_id="+c1.getInt(0));
                    }
                }else{
                    dbw.execSQL("DELETE FROM sesiones WHERE id="+c1.getInt(0));
                }
            }while(c1.moveToNext());
        }


        //Cursor c= dbr.rawQuery("SELECT id, sesion_id, latitud, longitud, fecha FROM puntos ORDER BY id, fecha",null);
        Cursor c= dbr.rawQuery("SELECT id, usuario FROM sesiones WHERE usuario='"+usuario+"'",null);

        if(c.moveToFirst()){
            do{
                id.add("Sesion "+c.getString(0));
            }while (c.moveToNext());
            tablaAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,id);
            listView.setAdapter(tablaAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String sesion_id=parent.getItemAtPosition(position).toString();
                sesion_id=sesion_id.replaceAll("Sesion ","");
                Intent intent=new Intent(estadisticasActivity.this,resultadosActivity.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("sesion",sesion_id);
                intent.putExtra("tiempo","");
                startActivity(intent);
            }
        });
    }
}
