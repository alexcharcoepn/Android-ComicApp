package com.example.proyecto_2bim

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterMangaMainBig
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterMangaMainLittle
import com.example.proyecto_2bim.DTO.MangaMainBigDTO
import com.example.proyecto_2bim.DTO.MangaMainLittleDTO
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {


    var listComicsLittlePop= arrayListOf<MangaMainLittleDTO>()
    var listComicsLittleLas= arrayListOf<MangaMainLittleDTO>()
    var listComicsBigRec= arrayListOf<MangaMainBigDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val metrics= DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics  )
//        Toast.makeText(this, "${metrics.heightPixels}x${metrics.widthPixels}", Toast.LENGTH_SHORT).show()
        val rvRecomendation=findViewById<RecyclerView>(R.id.rv_main_recomendation)
        val rvPopulars=findViewById<RecyclerView>(R.id.rv_container_lit_pop)
        val rvLasts=findViewById<RecyclerView>(R.id.rv_container_last)

        val navbar=findViewById<BottomNavigationView>(R.id.navbar)

        getFirebaseLittle("populars",listComicsLittlePop,rvPopulars)
        getFirebaseLittle("recents",listComicsLittleLas,rvLasts)
      //  getFirebaseLittle("recomendation",listComicsLittlePop,rvRecomendation)
        getFirebaseBig("recomendation",listComicsBigRec,rvRecomendation)


        //Barra de Navegacion
        navbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.go_search -> {
                    irActividad(Search::class.java)
                    Toast.makeText(this, "SEARCH", Toast.LENGTH_SHORT).show()
                }
                R.id.go_back -> finish()
                R.id.go_comics -> {
                    irActividad(Library::class.java)
                }
                R.id.go_favorites -> {
                    irActividad(Favoritos::class.java)
                }
                R.id.go_settings -> {
                    irActividad(Settings::class.java)
                }
            }
            true
        }

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

    fun startRVLittle(
            listInfo:ArrayList<MangaMainLittleDTO>,
            recView:RecyclerView
    ) {
                //Populars
                val lm=LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                val adapter=AdapterMangaMainLittle(listInfo,this)
                recView.layoutManager=lm
                recView.setHasFixedSize(true)
                recView.adapter=adapter
    }

    fun startRVBig(
            listInfo:ArrayList<MangaMainBigDTO>,
            recView:RecyclerView
    ) {
        //Populars
        val lm=LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        val adapter= AdapterMangaMainBig(listInfo,this)
        recView.layoutManager=lm
        recView.setHasFixedSize(true)
        recView.adapter=adapter
    }

    fun getFirebaseLittle(
            strCol:String,
            arrayInfo:ArrayList<MangaMainLittleDTO>,
            rvLit:RecyclerView
    ){
        val db=Firebase.firestore
                db.collection(strCol)
                .get()
                .addOnSuccessListener {
                    for(doc in it){
                        val docItem=doc.toObject(MangaMainLittleDTO::class.java)
                        arrayInfo?.add(docItem)
                    }
                    downloadImagesLittle(arrayInfo,rvLit)
                }
                .addOnFailureListener {
     //              Log.i("fb-firestore-search","Result: ${it}")
                }
    }
    fun getFirebaseBig(
            strCol:String,
            arrayInfo:ArrayList<MangaMainBigDTO>,
            rvLit:RecyclerView
    ){
        val db=Firebase.firestore
        db.collection(strCol)
                .get()
                .addOnSuccessListener {
                    for(doc in it){
                        val docItem=doc.toObject(MangaMainBigDTO::class.java)
                        arrayInfo?.add(docItem)
                    }
                    downloadImagesBig(arrayInfo,rvLit)
                }
                .addOnFailureListener {
  //                  Log.i("fb-firestore-search","Result: ${it}")
                }
    }

    fun downloadImagesLittle(
            listComics:ArrayList<MangaMainLittleDTO>,
            rvLit:RecyclerView
    ){
        var finishedTasks=0
        val storage = Firebase.storage
        for (path in listComics){
            val gsReference = storage.getReferenceFromUrl("gs://cheemsproject.appspot.com"+path.comicPosterLittle)
            val ONE_MEGABYTE: Long =  1000000000
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                val  bmp: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size);
                path.comigImg=bmp
//                Log.i("fb-firestore-search","Gotten: Image for ${path}")
            }.addOnFailureListener {
//                Log.i("fb-firestore-search","Failed: Image for ${path}")
            }.addOnCompleteListener {
                finishedTasks++
                if(finishedTasks==listComics.size){
//                    Log.i("fb-f","Now launching RV, gotten: ${finishedTasks}")
                    startRVLittle(listComics,rvLit)
                }
            }
        }
    }


    fun downloadImagesBig(
            listComics:ArrayList<MangaMainBigDTO>,
            rvLit:RecyclerView
    ){
        var finishedTasks=0
        val storage = Firebase.storage
        for (path in listComics){
            val gsReference = storage.getReferenceFromUrl("gs://cheemsproject.appspot.com"+path.comicPosterBig)
            val ONE_MEGABYTE: Long =  1000000000
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                val  bmp: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size);
                path.comigImg=bmp
                Log.i("fb-firestore-search","Gotten: Image for ${path}")
            }.addOnFailureListener {
                Log.i("fb-firestore-search","Failed: Image for ${path}")
            }.addOnCompleteListener {
                finishedTasks++
                if(finishedTasks==listComics.size){
                    Log.i("fb-f","Now launching RV, gotten: ${finishedTasks}")
                    startRVBig(listComics,rvLit)
                }
            }
        }
    }
}

