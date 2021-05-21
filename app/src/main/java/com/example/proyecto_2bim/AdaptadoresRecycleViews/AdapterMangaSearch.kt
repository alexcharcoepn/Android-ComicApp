
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
import androidx.core.content.ContextCompat.startActivity
import com.example.proyecto_2bim.ComicDetail
import com.example.proyecto_2bim.DTO.MangaDetailDTO
import com.example.proyecto_2bim.R
import com.example.proyecto_2bim.Search


class AdapterMangaSearch(
        private val mangaFound:MangaDetailDTO,
        private val imageManga:Bitmap,
        private val contexto: Search
):androidx.recyclerview.widget.
RecyclerView.Adapter<AdapterMangaSearch.MyViewHolder>()
{


    inner class MyViewHolder(view: View):
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
            val imgManga: ImageView
            val titleManga: TextView
            val authorManga:TextView
            val statusManga:TextView
            val genreManga:TextView
            val descriptionManga:TextView
            val containerManga:ConstraintLayout

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
                irActividad(mangaFound.comicId)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int)
            : AdapterMangaSearch.MyViewHolder
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
        return 1
    }

    override fun onBindViewHolder(
        holder: AdapterMangaSearch.MyViewHolder,
        position: Int
    ) {
        holder.titleManga.setText(mangaFound.comicTitle)
        holder.authorManga.setText(mangaFound.comicAuthor)
        holder.descriptionManga.setText(mangaFound.comicDesc)
        holder.imgManga.setImageBitmap(imageManga)
        holder.statusManga.setText(mangaFound.comicStatus)
        var txtTags="Teags: "
        for(item in mangaFound.comicGenres!!){
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
        startActivity(contexto,intentEx,null)
    }


}