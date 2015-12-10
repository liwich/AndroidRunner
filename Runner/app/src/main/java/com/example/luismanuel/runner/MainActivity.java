package com.example.luismanuel.runner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager=null;
    private ProgressBar progressBar;
    private TextView mensaje;
    private EditText usuario;
    private EditText password;
    private Button ingresar_boton;
    private Button registrar_boton;
    private SQLiteDatabase dbr;


    private void inicializarComponentes(){
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        mensaje=(TextView) findViewById(R.id.mensaje);
        usuario = (EditText) findViewById(R.id.usuario);
        password = (EditText) findViewById(R.id.password);
        ingresar_boton=(Button) findViewById(R.id.ingresar_boton);
        registrar_boton=(Button) findViewById(R.id.registrar_boton);

        usuario.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        ingresar_boton.setVisibility(View.INVISIBLE);
        registrar_boton.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarComponentes();
        dataBase dataBaseHelper=new dataBase(this,"Runner",null,1);
        dbr= dataBaseHelper.getReadableDatabase();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                usuario.setVisibility(View.VISIBLE);
                usuario.setFocusable(true);

                password.setVisibility(View.VISIBLE);
                ingresar_boton.setVisibility(View.VISIBLE);
                registrar_boton.setVisibility(View.VISIBLE);

                mensaje.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if(status==0){
                    mensaje.setText("GPS fuera de servicio");
                }else if(status==1){
                    mensaje.setText("GPS temporalmente fuera de servicio");
                }
            }

            @Override
            public void onProviderEnabled(String provider) {            }

            @Override
            public void onProviderDisabled(String provider) {
                mensaje.setText("GPS desactivado, es necesario que se active");
            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }else{
            mensaje.setVisibility(View.VISIBLE);
            mensaje.setText("No hay permiso de uso de GPS");
        }

    }

     public void ingresar(View view){
        Cursor c=dbr.rawQuery("SELECT usuario FROM usuarios WHERE usuario='"+usuario.getText()+"' AND password='"+password.getText()+"'",null);
        if(c.moveToFirst()){
            Intent intent = new Intent(MainActivity.this,usuarioActivity.class);
            intent.putExtra("usuario", usuario.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrar(View view){
        Intent intent = new Intent(MainActivity.this, registroActivity.class);
        startActivity(intent);
    }
}
