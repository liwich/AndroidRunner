package com.example.luismanuel.runner;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class registroActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText password;
    private Button guardar_boton;
    private SQLiteDatabase dbw;
    private SQLiteDatabase dbr;
    private dataBase dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dataBaseHelper= new dataBase(this, "Runner",null, 1);
        dbw= dataBaseHelper.getWritableDatabase();
        dbr= dataBaseHelper.getReadableDatabase();

        usuario = (EditText) findViewById(R.id.usuario);
        password= (EditText) findViewById(R.id.password);
        guardar_boton=(Button) findViewById(R.id.guardar_boton);
        guardar_boton.setEnabled(false);


        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Cursor c=dbr.rawQuery("SELECT * FROM usuarios WHERE usuario='"+usuario.getText()+"'",null);
                if(c.moveToFirst()){
                    if(!usuario.getText().equals("")||!password.getText().equals("")){
                        Toast.makeText(registroActivity.this, "Usuario ya existente", Toast.LENGTH_SHORT).show();
                        guardar_boton.setEnabled(false);
                    }
                }else{
                    if(!usuario.getText().equals("")||!password.getText().equals("")){
                        guardar_boton.setEnabled(true);
                    }
                }
            }
        });

    }

    public void guardarUsuario(View view){
        if(!usuario.getText().equals("")||!password.getText().equals("")){
            dbw.execSQL("INSERT INTO usuarios (usuario,password) VALUES('" + usuario.getText() + "','" + password.getText() + "')");
            Toast.makeText(registroActivity.this, "Agregado Correctamente", Toast.LENGTH_SHORT).show();
            usuario.setText("");
            password.setText("");
            dbw.close();
            finish();
        }
    }
}
