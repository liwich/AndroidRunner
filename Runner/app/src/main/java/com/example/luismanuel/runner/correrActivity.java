package com.example.luismanuel.runner;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class correrActivity extends AppCompatActivity {
    private String usuario;
    private String sesion;
    private Button boton_iniciar_correr;
    private Button boton_terminar;
    private Chronometer chronometer;
    private LocationManager locationManager=null;
    private dataBase dataBaseHelper;
    private SQLiteDatabase dbr;
    private SQLiteDatabase dbw;
    private boolean corriendo_bandera;
    TextView texto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correr);
        corriendo_bandera=false;

        //componentes
        boton_iniciar_correr = (Button) findViewById(R.id.boton_iniciar_correr);
        boton_terminar= (Button)findViewById(R.id.boton_terminar);
        chronometer =(Chronometer) findViewById(R.id.chronometer);
        usuario= getIntent().getExtras().getString("usuario");

        //base de datos
        dataBaseHelper = new dataBase(this,"Runner",null,1);
        dbr= dataBaseHelper.getReadableDatabase();
        dbw=dataBaseHelper.getWritableDatabase();
        bd();

        texto=(TextView) findViewById(R.id.text);

        //GPS
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        localizacion();
    }

    public void bd(){
        dbw.execSQL("INSERT INTO sesiones (id,usuario,tiempo) VALUES(null,'" + usuario + "','')");
        Cursor c=dbr.rawQuery("SELECT id FROM SESIONES ORDER BY id DESC LIMIT 1",null);
        if(c.moveToFirst()){
            sesion=c.getString(0);
        }
    }

    /*Localizació GPS*/
    public void localizacion(){
        final LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(corriendo_bandera==true&&location.getAccuracy()<15){
                    texto.setText("Velocidad: " + location.getSpeed());
                    setLocation(location);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {hacerAlertSalir("GPS desactivado"); }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);


        }else{
            String Texto= "No se pudo acceder al GPS";
            Toast.makeText(getApplicationContext(), Texto, Toast.LENGTH_SHORT);
        }
    }

    public void setLocation(Location location){

            double latitud=location.getLatitude();
            double longitud=location.getLongitude();
            Calendar c1 = GregorianCalendar.getInstance();

            SimpleDateFormat sfd=new SimpleDateFormat("hh:mm:ss");
            String hora = sfd.format(c1.getTime());
            dbw.execSQL("INSERT INTO puntos (id,sesion_id,latitud,longitud,fecha) VALUES(null,'"+sesion+"','"+latitud+"','"+longitud+"','"+hora+"')");
            dbw.execSQL("UPDATE sesiones SET tiempo='"+chronometer.getText()+"' WHERE id="+sesion);

    }

    /*Eventos de botones*/
    public void iniciar(View view){
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        boton_iniciar_correr.setEnabled(false);
        boton_terminar.setEnabled(true);
        corriendo_bandera=true;
    }

    public void terminar(View view){
        chronometer.stop();
        corriendo_bandera=false;
        Intent intent =new Intent(correrActivity.this,resultadosActivity.class);
        intent.putExtra("sesion",sesion);
        intent.putExtra("usuario", usuario);
        intent.putExtra("tiempo", chronometer.getText());
        startActivity(intent);
        finish();

    }

    /*Boton Regresar*/
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            alertRegresar("Desea cancelar la carrera?").show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*Alert de eventos */
    AlertDialog hacerAlertSalir(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(correrActivity.this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        return alert;
    }

    AlertDialog alertRegresar(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(correrActivity.this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dbw.execSQL("DELETE FROM sesiones WHERE id=" + sesion);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        return alert;
    }

}
