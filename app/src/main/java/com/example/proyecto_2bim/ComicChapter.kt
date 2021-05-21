package com.example.proyecto_2bim

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_2bim.AdaptadoresRecycleViews.AdapterReadingChapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ComicChapter : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_chapter)


        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val pathComicChapter =  intent.getStringExtra("pathChapter")
        val numPagesChapter =  intent.getStringExtra("numPages")?.toInt()

        val rvPages=findViewById<RecyclerView>(R.id.rv_container_pages)

        getPagesFirebase(pathComicChapter,numPagesChapter,rvPages)


    }

    fun getPagesFirebase(path:String?,numPages:Int?,rvPages:RecyclerView){
        var finishedTasks=0
        val storage = Firebase.storage
        val array=ArrayList<Any>()
        for (i in 1..numPages!!){
            array.add(0)
        }

        for (i in 1..numPages!!){
            val gsReference = storage.getReferenceFromUrl("gs://cheemsproject.appspot.com"+path+i.toString()+".jpg")
                 val ONE_MEGABYTE: Long =  1000000000
                 gsReference.getBytes(ONE_MEGABYTE)
                         .addOnSuccessListener {
                             val  bmp: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size);
                             array.set(i-1,bmp)
                         }.addOnFailureListener {
                             Log.i("fb-chapter","Failed: Image for ${path}")
                         }.addOnCompleteListener {
                             finishedTasks++
                             if(finishedTasks==numPages){
                                 Log.i("fb-chapter","Now launching RV, gotten: ${finishedTasks}")
                                 Log.i("fb-chapter","Final Array: ${array}")
                                 Toast.makeText(this, "Download Finished", Toast.LENGTH_SHORT).show()
                                 iniciarRecycleView(array,this,rvPages)
                     }else{

                                 Log.i("fb-chapter","Not Yet, gotten: ${finishedTasks} pages, current ${i} page")
                             }
                 }
        }
    }

    fun iniciarRecycleView(
            lista:ArrayList<Any>,
            actividad:ComicChapter,
            recyclerView: androidx.recyclerview.widget.RecyclerView
    ){
        val adaptador= AdapterReadingChapter(
                lista,actividad,recyclerView
        )
        recyclerView.adapter=adaptador
        //Animations
        recyclerView.itemAnimator=androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager=androidx.recyclerview.widget.LinearLayoutManager(actividad)
    }
}