package com.example.proyecto_2bim

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterMangaFavorites
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterMangaLibrary
import com.example.proyecto_2bim.DTO.FavoriteComicDTO
import com.example.proyecto_2bim.DTO.MangaDetailDTO
import com.example.proyecto_2bim.Data.AuthUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.ArrayList

class Favoritos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)


        val rvComics=findViewById<RecyclerView>(R.id.rv_favorites_comics)

        getFavorites(rvComics)

        val bnv_navbar=findViewById<BottomNavigationView>(R.id.navbar_favorites)
        bnv_navbar.selectedItemId=R.id.go_favorites
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
                }
                R.id.go_settings -> {
                    finish()
                    irActividad(Settings::class.java)
                }
            }
            true
        }


    }

    fun getFavorites(rvFavorites:RecyclerView){
        val arrayComicsId= arrayListOf<String>()
        val arrayComicsPath= arrayListOf<String>()

        Firebase.firestore.collection("users").document(AuthUser.user!!.email).collection("favorites").get()
            .addOnSuccessListener {
                for(comic in it){
                    var comicFav=comic.toObject(FavoriteComicDTO::class.java)
                    arrayComicsId.add(comicFav.comicId)
                    Log.i("fb-f","Found: ${comic.data["comicId"]}")
                }
                getDataComics(arrayComicsId,rvFavorites )
            }
    }


    fun getDataComics(arrayComicsId:ArrayList<String>,rvFavorites:RecyclerView){
        val arrayComicsDTO=ArrayList<MangaDetailDTO>()
        val arrayComicsImages=ArrayList<String>()
        val db=Firebase.firestore.collection("comics")
        for (value in arrayComicsId){
            db.document(value).get()
                .addOnSuccessListener {
                    val comicGotten=it.toObject(MangaDetailDTO::class.java)
                    if(comicGotten!=null)
                    {
                        arrayComicsImages.add(comicGotten.comicImgPort)
                        arrayComicsDTO.add(comicGotten)
                    }
                    getImages(arrayComicsImages,rvFavorites,arrayComicsDTO)
                }
        }
    }


    fun getImages(arrayComicsImagePath:ArrayList<String>,rvFavorites:RecyclerView,arrayComicsDTO:ArrayList<MangaDetailDTO>){
        val arrayImages=ArrayList<Any>()

        for (i in 1..arrayComicsImagePath.size){
            arrayImages.add(0)
        }

        var cantDownloaded=0
        Log.i("fb-s","Downloading images for Favorites")
        val storage = Firebase.storage
        for (i in 0..(arrayComicsImagePath.size-1)){
            val gsReference = storage.getReferenceFromUrl("gs://cheemsproject.appspot.com"+arrayComicsImagePath[i])
            val ONE_MEGABYTE: Long =  1000000000
            gsReference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener {
                    val bmp= BitmapFactory.decodeByteArray(it, 0, it.size);
                    arrayImages.add(i,bmp)
                }.addOnFailureListener {
                    Log.i("fb-firestore-search","Failed: Image ")
                }.addOnCompleteListener {
                    cantDownloaded++
                    if(cantDownloaded==arrayComicsImagePath.size){
                        iniciarRecycleView(arrayComicsDTO,arrayImages,this,rvFavorites)
                    }
                }
        }



    }

    fun iniciarRecycleView(
        listMangas:List<MangaDetailDTO>,
        listImages:List<Any>,
        actividad:Favoritos,
        recyclerView: RecyclerView
    ){
        val listImg=listImages as ArrayList<Bitmap>
        val adaptador= AdapterMangaFavorites(listMangas,listImg,this)

        recyclerView.adapter=adaptador

        //Animations
        recyclerView.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(actividad)
    }

    fun irActividad(
        clase:Class<*>
    ){
        val intentEx= Intent(
            this,
            clase
        )
        startActivity(intentEx)
    }
}