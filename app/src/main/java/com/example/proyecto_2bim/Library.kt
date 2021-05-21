package com.example.proyecto_2bim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterMangaLibrary
import com.example.proyecto_2bim.DTO.LibraryChapterDTO
import com.example.proyecto_2bim.Data.AuthUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Library : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val rvComics=findViewById<RecyclerView>(R.id.rv_favorites_comics)
        getLibrary(rvComics)
        val bnv_navbar=findViewById<BottomNavigationView>(R.id.navbar_library)
        bnv_navbar.selectedItemId=R.id.go_comics
        bnv_navbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.go_search -> {
                    finish()
                    irActividad(Search::class.java)
                }
                R.id.go_back -> {
                    finish()
                }
                R.id.go_comics -> { }
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

    fun getLibrary(rvComics:RecyclerView){
        val arrayLibrary= arrayListOf<LibraryChapterDTO>()
        Firebase.firestore.collection("users").document(AuthUser.user!!.email).collection("library")
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    val chapterLib=doc.toObject(LibraryChapterDTO::class.java)
                    arrayLibrary.add(chapterLib)
                    iniciarRecycleView(arrayLibrary,this,rvComics)
                }

            }
    }

    fun iniciarRecycleView(
        lista:ArrayList<LibraryChapterDTO>,
        actividad:Library,
        recyclerView: RecyclerView
    ){
        val adaptador= AdapterMangaLibrary(lista,actividad)

        recyclerView.adapter=adaptador

        //Animations
        recyclerView.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(actividad)
    }

    fun irActividad(
            clase:Class<*>,
            codigo:Int? =null
    ){
        val intentEx= Intent(
                this,
                clase
        )

        if(codigo!=null){
            startActivityForResult(intentEx,codigo)
        }else{
            startActivity(intentEx)
        }
    }
}