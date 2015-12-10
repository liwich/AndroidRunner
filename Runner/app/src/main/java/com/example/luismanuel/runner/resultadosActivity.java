package com.example.luismanuel.runner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class resultadosActivity extends AppCompatActivity {
    private dataBase databaseHelper;
    private SQLiteDatabase dbr;
    private String sesion;
    private String usuario;
    private String tiempo;
    private TextView distancia_text;
    private TextView tiempo_text;
    private Button mostrar_mapa_boton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        sesion=getIntent().getExtras().getString("sesion");
        tiempo=getIntent().getExtras().getString("tiempo");
        databaseHelper=new dataBase(this,"Runner",null,1);
        dbr=databaseHelper.getReadableDatabase();

        TextView texto= (TextView) findViewById(R.id.texto);
        distancia_text =(TextView )findViewById(R.id.distancia);
        tiempo_text =(TextView) findViewById(R.id.tiempo);
        mostrar_mapa_boton=(Button) findViewById(R.id.mostrar_mapa_boton);


        Cursor c= dbr.rawQuery("SELECT * FROM puntos WHERE sesion_id=" + sesion, null);
        //Cursor c= dbr.rawQuery("SELECT * FROM puntos WHERE sesion_id=3",null);
        int count=c.getCount();
        int i=0;
        double latitud1=0;
        double longitud1=0;
        double distancia=0;

        String hora1="";
        if(c.moveToFirst()&&count>1){
            latitud1 =Double.parseDouble(c.getString(2));
            longitud1=Double.parseDouble(c.getString(3));
            hora1=c.getString(4);
            Calendar calendar= Calendar.getInstance();
            Calendar calendar2= Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");

            try {
                calendar.setTime(sdf.parse(hora1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            c.moveToNext();
            do {
                double latitud2 = Double.parseDouble(c.getString(2));
                double longitud2= Double.parseDouble(c.getString(3));;
                String hora2=c.getString(4);
                distancia+=distFrom(latitud2,longitud2,latitud1,longitud1);
                try {
                    calendar2.setTime(sdf.parse(hora2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                latitud1=latitud2;
                longitud1=longitud2;
                c.moveToNext();
                i++;
            }while (i<count-1);

        }

        distancia_text.append(" "+(float)distancia+"metros");
        if(tiempo.equals("")){
            Cursor ti=dbr.rawQuery("SELECT tiempo FROM sesiones WHERE id="+sesion,null);
            if(ti.moveToFirst()){
                tiempo_text.append(ti.getString(0));
            }

        }else{
            tiempo_text.append(" "+tiempo);
        }



    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //metros
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }


   public  void mostrarMapa(View v){
        Intent intent=new Intent(resultadosActivity.this,resultadoMapsActivity.class);
        intent.putExtra("sesion",sesion);
        startActivity(intent);
    }

    public void cerrar(View v){
        finish();
    }
}
