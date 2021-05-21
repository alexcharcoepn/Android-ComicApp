package com.example.proyecto_2bim.AdaptadoresRecycleViews



import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.proyecto_2bim.ComicChapter
import com.example.proyecto_2bim.ComicDetail
import com.example.proyecto_2bim.DTO.MangaDetailChaptersDTO
import com.example.proyecto_2bim.Data.AuthUser
import com.example.proyecto_2bim.R
import com.google.android.gms.auth.api.Auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AdapterMangaChapters(
        private val listChapters :ArrayList<MangaDetailChaptersDTO>,
        private val contexto: ComicDetail,
        private val titleComic:String
):androidx.recyclerview.widget.
RecyclerView.Adapter<AdapterMangaChapters.MyViewHolder>()
{

    inner class MyViewHolder(view: View):
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val titleManga: TextView
        val numManga:TextView
        var pathMangaChapter:String=""
        var numPages:String=""
        val btnRead:Button


        init{
            titleManga=view.findViewById(R.id.tv_chapter_name)
            numManga=view.findViewById(R.id.tv_num_chapter)
            btnRead=view.findViewById(R.id.btn_chapter_buy)

            btnRead.setOnClickListener{
                irChapter(pathMangaChapter,numPages)
                addLibrary(titleManga.text.toString(),pathMangaChapter,numManga.text.toString())
            }

        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int)
            : AdapterMangaChapters.MyViewHolder
    {
        val itemView= LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.rv_comic_chapters,
                        parent,
                        false
                )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listChapters.size
    }

    override fun onBindViewHolder(
            holder: AdapterMangaChapters.MyViewHolder,
            position: Int
    ) {
        val content=listChapters[position]
        holder.numManga.setText(content.chapterNum)
        holder.titleManga.setText(content.chapterTitle)
        holder.pathMangaChapter=content.chapterPath
        holder.numPages=content.chapterPages

    }

    fun irChapter(
            pathChapter:String,
            numPages:String
    ){
        val intentEx=Intent(
                contexto,
                ComicChapter::class.java
        )
        intentEx.putExtra("pathChapter",pathChapter)
        intentEx.putExtra("numPages",numPages)
        startActivity(contexto,intentEx,null)
    }

    fun addLibrary(titleManga:String,pathChapter:String,numChapter:String){
        val data= hashMapOf<String,String>(
            "titleManga" to titleComic,
            "titleChapter" to titleManga,
            "pathManga" to pathChapter,
            "numChapter" to numChapter
        )
        val db=Firebase.firestore.collection("users").document(AuthUser.user!!.email)
        db.collection("library")
            .add(data)

    }

}