package com.example.luismanuel.runner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class resultadoMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    dataBase dataBaseHelper;
    SQLiteDatabase dbr;
    String sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sesion=getIntent().getExtras().getString("sesion");
        dataBaseHelper=new dataBase(this,"Runner",null,1);
        dbr=dataBaseHelper.getReadableDatabase();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng punto = null;
        PolylineOptions lineas= new PolylineOptions();
        //CircleOptions circulos =new CircleOptions();

        //circulos.radius(0.2);
        //circulos.strokeColor(Color.BLUE);
        //circulos.fillColor(Color.BLUE);

        lineas.width(8);
        lineas.color(Color.RED);

        Cursor c=dbr.rawQuery("SELECT * FROM puntos WHERE sesion_id="+sesion,null);

        if(c.moveToFirst()){
            do{
                String latitud=c.getString(2);
                String longitud=c.getString(3);
                punto=new LatLng(Double.parseDouble(latitud),Double.parseDouble(longitud));

                //circulos.center(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));
                lineas.add(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)));

                //mMap.addCircle(circulos);
                mMap.addMarker(new MarkerOptions().position(punto).title(c.getString(4)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));

                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            }while (c.moveToNext());

            mMap.addPolyline(lineas);


        }

    }
}
