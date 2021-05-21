package com.example.proyecto_2bim

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_2bim.DTO.MangaDetailDTO
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterMangaSearch as AdapterSearchManga

class Search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val bnv_navbar=findViewById<BottomNavigationView>(R.id.navbar_search)
        bnv_navbar.selectedItemId=R.id.go_search

        val rvSearch=findViewById<RecyclerView>(R.id.rv_search_manga)
        val etSearch=findViewById<EditText>(R.id.et_search)

        etSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                    getFirebase(etSearch.text.toString())
                Toast.makeText(this, "Searching: ${etSearch.text.toString()}", Toast.LENGTH_SHORT).show()
                findComic(etSearch.text.toString(),rvSearch)
                return@OnKeyListener true
            }
            false
        });

        bnv_navbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.go_search -> {
                    finish()
                    irActividad(Search::class.java)
                }
                R.id.go_back -> {
                    finish()
                }
                R.id.go_comics -> {
                    finish()
                    irActividad(Library::class.java)
                }
                R.id.go_favorites -> {
                    finish()
                    irActividad(Favoritos::class.java)
                }
                R.id.go_settings -> {
                    finish()
                    irActividad(Settings::class.java)
                }
            }
            true
        }


    }




    fun getFirebase(
            idComic:String
    ){
        val db= Firebase.firestore
        db.collection("comics")
                .whereEqualTo("comicTitle",idComic)
                .get()
                .addOnSuccessListener {
                    Log.i("fb-firestore-search","Result: ${it.documents}")
                }
                .addOnFailureListener {
                    Log.i("fb-firestore-search","Result: ${it}")
                }
    }


    fun findComic(titleComic:String,rvSearch:RecyclerView) {
        Firebase.firestore.collection("comics")
            .whereEqualTo("comicTitle",titleComic)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val comicFound = doc.toObject(MangaDetailDTO::class.java)
                    Log.i("fb-s","CASTED: ${comicFound}")
                    getImage(comicFound,rvSearch)
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "Comic not Found", Toast.LENGTH_SHORT).show()
            }

    }

    fun getImage(comic: MangaDetailDTO,rvSearch: RecyclerView){
        Log.i("fb-s","Downloading images from: ${comic.comicImgPort}")
        var bmp:Bitmap?=null
        val storage = Firebase.storage
        val gsReference = storage.getReferenceFromUrl("gs://cheemsproject.appspot.com"+comic.comicImgPort)
        val ONE_MEGABYTE: Long =  1000000000
        gsReference.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {
            bmp= BitmapFactory.decodeByteArray(it, 0, it.size);
            }.addOnFailureListener {
                Log.i("fb-firestore-search","Failed: Image ")
        }.addOnCompleteListener {
                Log.i("fb-f","Now launching RV")
                if(bmp!=null){
                    iniciarRecycleView(comic, bmp!!,this,rvSearch)
                }
        }

    }

    fun iniciarRecycleView(
        comic:MangaDetailDTO,
        image:Bitmap,
        context:Context,
        rvSearch: RecyclerView
    ){
        Toast.makeText(context, "Comic Found", Toast.LENGTH_SHORT).show()
        val adaptador= AdapterSearchManga(comic,image,this)
        rvSearch.adapter=adaptador

        //Animations
        rvSearch.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
        rvSearch.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    fun irActividad(
            clase:Class<*>,
            idComic:String?=null
    ){
        val intentEx= Intent(
                this,
                clase
        )
        if(idComic!=null){
            intentEx.putExtra("idComic",idComic)
        }
        startActivity(intentEx)
    }

}