package com.example.proyecto_2bim.AdaptadoresRecycleViews

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.example.proyecto_2bim.*
import com.example.proyecto_2bim.DTO.LibraryChapterDTO

class AdapterMangaLibrary(
    private val listaMangas :ArrayList<LibraryChapterDTO>,
    private val contexto: Library
):androidx.recyclerview.widget.
RecyclerView.Adapter<AdapterMangaLibrary.MyViewHolder>()
{
    //Code Manga
    val MANGA_CODE:Int=101

    inner class MyViewHolder(view: View):
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val titleManga: TextView
        val chapterManga: TextView
        val numChapter: TextView
        val containerManga: ConstraintLayout
        var pathChapter=""

        init{
            titleManga=view.findViewById(R.id.tv_mangaTitle)
            chapterManga=view.findViewById(R.id.tv_nameChapter)
            numChapter=view.findViewById(R.id.tv_numChapter)
            containerManga=view.findViewById(R.id.border_library_chapter)

            containerManga.setOnClickListener{
                Toast.makeText(contexto, "Entering Manga: ${chapterManga.text}", Toast.LENGTH_SHORT).show()
                irActividad(pathChapter,numChapter.text.toString())
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int)
            : AdapterMangaLibrary.MyViewHolder
    {
        val itemView= LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.rv_library_comics_result,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaMangas.size
    }

    override fun onBindViewHolder(
        holder: AdapterMangaLibrary.MyViewHolder,
        position: Int
    ) {
        val content=listaMangas[position]
        holder.titleManga.setText(content.titleMange)
        holder.numChapter.setText(content.numChapter)
        holder.chapterManga.setText(content.titleChapter)
        holder.pathChapter=content.pathManga


    }

    fun irActividad(
        path:String,
        numPages:String
    ){
        val intentEx= Intent(
                contexto,
                ComicChapter::class.java
        )
        intentEx.putExtra("pathChapter",path)
        intentEx.putExtra("numPages","5")

        startActivity(contexto,intentEx,null)
        }



}