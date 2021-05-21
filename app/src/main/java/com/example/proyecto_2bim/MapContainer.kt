package com.example.proyecto_2bim

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class MapContainer : AppCompatActivity() , OnMapReadyCallback
{

    private lateinit var mMap: GoogleMap
    var permisos=false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_container)
        getPermisos();



        val mapFrag=supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment;
        mapFrag.getMapAsync(this)



    }

    //
    override fun onMapReady(googleMap: GoogleMap?) {
        if(googleMap!=null){
            mMap=googleMap
            mapConfiguraction(googleMap)
        }

        val lotCoor =  intent.getStringExtra("lot")
        val latCoor =  intent.getStringExtra("lat")
        val title=intent.getStringExtra("title").toString()

        var latLon:LatLng?=null
        if(lotCoor!=null&&latCoor!=null){
            latLon=LatLng(latCoor.toDouble(), lotCoor.toDouble())
        }

        if (latLon != null) {
            centerCamera(latLon,17f)
            addMarker(latLon,title)
        }
    }

    fun addMarker(latLng:LatLng,tit:String){
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(tit)
        )
    }

    fun centerCamera(latLng: LatLng,zoom:Float=10f){
        mMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(latLng,zoom)
        )
    }

    fun mapConfiguraction(map:GoogleMap){
        val context=this.applicationContext
        with(map){
            val permisosFineLocation=ContextCompat
                .checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            val permisos=permisosFineLocation==PackageManager.PERMISSION_GRANTED
            if(permisos){
                map?.isMyLocationEnabled=true
            }
            uiSettings.isZoomControlsEnabled  =true
            uiSettings.isMyLocationButtonEnabled=true
        }

    }

    fun getPermisos(){
        val permisoFineLocation=ContextCompat
            .checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        val permisos=permisoFineLocation==PackageManager.PERMISSION_GRANTED
        if(permisos){
            Toast.makeText(this, "Tiene permiso FINE LOCATION", Toast.LENGTH_SHORT).show()
            this.permisos=true
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }

    }

}