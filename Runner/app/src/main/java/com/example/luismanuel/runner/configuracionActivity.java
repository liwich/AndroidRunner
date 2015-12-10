package com.example.luismanuel.runner;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class configuracionActivity extends AppCompatActivity {

    private dataBase databaseHelper;
    private SQLiteDatabase dbw;
    private EditText password_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        databaseHelper=new dataBase(this,"Runner",null,1);
        dbw=databaseHelper.getWritableDatabase();
        password_edit =(EditText) findViewById(R.id.password_edit);

    }

    public  void cambiarPassword(View view){
        if(!password_edit.equals("")){
            dbw.execSQL("UPDATE usuarios SET password='" + password_edit.getText() + "'");
            Toast.makeText(configuracionActivity.this, "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(configuracionActivity.this, "Campo password vacío", Toast.LENGTH_SHORT).show();
        }
    }
}
