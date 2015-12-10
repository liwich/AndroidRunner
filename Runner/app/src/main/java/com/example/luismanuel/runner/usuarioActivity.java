package com.example.luismanuel.runner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class usuarioActivity extends AppCompatActivity {
    private String usuario;
    private TextView bienvenida_texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        inicializarComponentes();
        usuario= getIntent().getExtras().getString("usuario");
        bienvenida_texto.setText(bienvenida_texto.getText() + "" + usuario);
    }

    private void inicializarComponentes(){
        bienvenida_texto=(TextView) findViewById(R.id.bienvenida_text);
    }

    AlertDialog hacerAlertSalir(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(usuarioActivity.this);
        builder.setMessage(mensaje);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        return alert;
    }

    public void correr(View view){
        Intent intent = new Intent(usuarioActivity.this, correrActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void estadisticas(View view){
        Intent intent = new Intent(usuarioActivity.this,estadisticasActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);

    }
    public void configuracion(View view){
        Intent intent = new Intent(usuarioActivity.this,configuracionActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void salir(View view){
        hacerAlertSalir("Seguro que desea salir?").show();
    }
}
