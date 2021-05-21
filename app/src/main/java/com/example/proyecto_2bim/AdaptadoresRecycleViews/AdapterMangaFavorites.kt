package com.example.proyecto_2bim.AdaptadoresRecycleViews


import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.proyecto_2bim.ComicDetail
import com.example.proyecto_2bim.DTO.MangaDetailDTO
import com.example.proyecto_2bim.Favoritos

import com.example.proyecto_2bim.R

class AdapterMangaFavorites(
    private val listaMangas :List<MangaDetailDTO>,
    private val listaImages:List<Bitmap>,
    private val contexto: Favoritos
):androidx.recyclerview.widget.
RecyclerView.Adapter<AdapterMangaFavorites.MyViewHolder>()
{


    inner class MyViewHolder(view: View):
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val imgManga: ImageView
        val titleManga: TextView
        val authorManga: TextView
        val statusManga: TextView
        val genreManga: TextView
        val descriptionManga: TextView
        val containerManga: ConstraintLayout
        var comicIdStr:String=""

        init{
            imgManga=view.findViewById(R.id.iv_comic)
            titleManga=view.findViewById(R.id.tv_mangaTitle)
            authorManga=view.findViewById(R.id.tv_mangaAutor)
            statusManga=view.findViewById(R.id.tv_mangaStatus)
            genreManga=view.findViewById(R.id.tv_mangaGenres)
            descriptionManga=view.findViewById(R.id.tv_mangaDescription)
            containerManga=view.findViewById(R.id.rv_search_manga_result)

            containerManga.setOnClickListener{
                Toast.makeText(contexto, "Entering Manga: ${titleManga.text}", Toast.LENGTH_SHORT).show()
                irActividad(comicIdStr)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int)
            : AdapterMangaFavorites.MyViewHolder
    {
        val itemView= LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.comic_search_results,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaMangas.size
    }

    override fun onBindViewHolder(
        holder: AdapterMangaFavorites.MyViewHolder,
        position: Int
    ) {
        val content=listaMangas[position]
        val image=listaImages[position]
        holder.comicIdStr=content.comicId
        holder.titleManga.setText(content.comicTitle)

        holder.authorManga.setText(content.comicAuthor)
        holder.descriptionManga.setText(content.comicDesc)
        holder.imgManga.setImageBitmap(image)
        holder.statusManga.setText(content.comicStatus)
        var txtTags="Teags: "
        for(item in content.comicGenres!!){
            txtTags=txtTags+item.toString()+","
        }
        holder.genreManga.setText(txtTags)
    }

    fun irActividad(
        idComic:String
    ){
        val intentEx= Intent(
            contexto,
            ComicDetail::class.java
        )
        intentEx.putExtra("id",idComic)
        ContextCompat.startActivity(contexto, intentEx, null)
    }



}