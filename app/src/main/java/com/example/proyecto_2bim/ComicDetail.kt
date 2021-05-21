package com.example.proyecto_2bim

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterMangaChapters
import com.example.proyecto_2bim.DTO.MangaDetailChaptersDTO
import com.example.proyecto_2bim.DTO.MangaDetailDTO
import com.example.proyecto_2bim.Data.AuthUser
import com.google.android.gms.auth.api.Auth
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.ArrayList

class ComicDetail : AppCompatActivity() {
    var comic: MangaDetailDTO? =null
    var comicId:String=""
    var chaptersComic= ArrayList<MangaDetailChaptersDTO>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val idComic =  intent.getStringExtra("id")
        if (idComic != null) {
            comicId=idComic
        }
        val rvChapters=findViewById<RecyclerView>(R.id.rv_det_chapters)
        val btnMap=findViewById<Button>(R.id.btn_maps)
        val btnFav=findViewById<Button>(R.id.btn_favorites)

        btnMap.setOnClickListener {
            irMap()
        }
        btnFav.setOnClickListener {
            anadirFavoritos()
        }

        getComicDetail(idComic,rvChapters)

        val bnv_navbar=findViewById<BottomNavigationView>(R.id.bnv_comic_detail)
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

    fun iniciarRVComicChapters(
            lista:ArrayList<MangaDetailChaptersDTO>,
            actividad:ComicDetail,
            recyclerView: RecyclerView,
            comicTitle:String
    ){
        val adaptador= AdapterMangaChapters(
                lista,actividad,comicTitle
        )
        recyclerView.adapter=adaptador

        //Animations
        recyclerView.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(actividad)
    }

    fun getComicDetail(idComic:String?,rv:RecyclerView){
        val db=Firebase.firestore
        if (idComic != null) {
            db.collection("comics")
                    .document(idComic)
                    .get()
                    .addOnSuccessListener {

                        Log.i("fb-detail","Comic com.example.proyecto_2bim.Data: ${it.data}")
                        comic= it.toObject(MangaDetailDTO::class.java)!!
                        Log.i("fb-detail","Comic Casted: ${comic}")
                        Log.i("fb-d","LAND: ${comic?.comicImgLand} - PORT: ${comic?.comicImgPort}")
                        if(comic?.comicImgLand!=null && comic?.comicImgPort!=null){
                            getImages(comic!!.comicImgPort,comic!!.comicImgLand)
                        }
                    }
                    .addOnFailureListener {
                        Log.i("fb-detail","Result: ${it}")
                    }.addOnCompleteListener{
                        findViewById<TextView>(R.id.tv_comic_title).text=comic?.comicTitle
                        findViewById<TextView>(R.id.tv_comic_author).text=comic?.comicAuthor
                        findViewById<TextView>(R.id.tv_comic_status).text=comic?.comicStatus
                        findViewById<TextView>(R.id.tv_comic_desc).text=comic?.comicDesc
                    }

            Log.i("fb-detail","Getting Chapters")

            db.collection("comics")
                    .document(idComic)
                    .collection("chapters")
                    .get()
                    .addOnSuccessListener {
                        Log.i("fb-detail","Chapters Gotten")
                        for(chap in it){
                            val chapterDTO=chap.toObject(MangaDetailChaptersDTO::class.java)
                            chaptersComic.add(chapterDTO)
                            Log.i("fb-detail","ChapterArray: ${chaptersComic.size}")
                        }
                    }
                    .addOnFailureListener {
                        Log.i("fb-detail","NO Chapters")
                    }.addOnCompleteListener {
                        iniciarRVComicChapters(chaptersComic,this,rv,comic!!.comicTitle)
                    }
            Log.i("fb-detail","Finished Getting Chapters")
        }
    }

    fun getImages(
            strPathLit:String,
            strPathBig:String
    ){
        val storage = Firebase.storage
        val ONE_MEGABYTE: Long =  1000000000
        var bmpLittle:Bitmap?=null
        var bmpBig:Bitmap?=null
        val ivLittle=findViewById<ImageView>(R.id.iv_det_port)
        val ivBig=findViewById<ImageView>(R.id.iv_back_comic)


        storage.getReferenceFromUrl("gs://cheemsproject.appspot.com"+strPathLit).getBytes(ONE_MEGABYTE)
                .addOnSuccessListener {
            bmpLittle = BitmapFactory.decodeByteArray(it, 0, it.size)
        }.addOnFailureListener {
                Log.i("fb-firestore-search","Failed: Image for ${strPathLit}")
        }.addOnCompleteListener {
                Log.i("fb-d","Little: ${bmpLittle}")
                ivLittle.setImageBitmap(bmpLittle)
        }

        storage.getReferenceFromUrl("gs://cheemsproject.appspot.com"+strPathBig).getBytes(ONE_MEGABYTE)
                .addOnSuccessListener {
            bmpBig= BitmapFactory.decodeByteArray(it, 0, it.size)
        }.addOnFailureListener {
            Log.i("fb-firestore-search","Failed: Image for ${strPathLit}")
        }.addOnCompleteListener {
                Log.i("fb-d","Big: ${bmpBig}")
                ivBig.setImageBitmap(bmpBig)
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

    fun irMap(
    ){
        val intentEx=Intent(this,MapContainer::class.java)

        Log.i("fb-d","Store: ${comic?.comicStore}")
        intentEx.putExtra("lat", comic?.comicStore?.get(0))
        intentEx.putExtra("lot",comic?.comicStore?.get(1))
        intentEx.putExtra("title",comic?.comicStore?.get(2))

        startActivity(intentEx)
    }

    fun anadirFavoritos(){
        Log.i("fb-d","Favorite id: ${comicId}")
        Log.i("fb-d","UserIfno: ${AuthUser.user.toString()}")

        if(AuthUser.user?.email!=null){
            Log.i("fb-d","ID User: ${AuthUser.user!!.email }")

            val comicFav= hashMapOf<String,Any>(
                "comicId" to comicId
            )

            val db=Firebase.firestore
            db.collection("users")
                .document(AuthUser.user!!.email)
                .collection("favorites")
                .add(comicFav)
                .addOnFailureListener {
                    Log.i("fb-d","Update Failed")
                }
                .addOnSuccessListener {
                    Log.i("fb-d","Sucessfull Update")
                }.addOnCompleteListener {
                    Log.i("fb-d","Finished Task")
                }
        }

    }

}