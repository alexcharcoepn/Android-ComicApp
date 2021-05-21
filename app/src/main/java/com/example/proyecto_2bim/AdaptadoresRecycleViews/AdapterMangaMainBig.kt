package com.example.proyecto_2bim.AdaptadoresRecycleViews
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.proyecto_2bim.ComicDetail
import com.example.proyecto_2bim.DTO.MangaMainBigDTO
import com.example.proyecto_2bim.MainActivity
import com.example.proyecto_2bim.R

class AdapterMangaMainBig(
        private val listaMangas :ArrayList<MangaMainBigDTO>,
        private val contexto: MainActivity
):androidx.recyclerview.widget.
RecyclerView.Adapter<AdapterMangaMainBig.MyViewHolder>()
{

    inner class MyViewHolder(view: View):
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        var idStrComic:String=""
        var imgManga: ImageView

        init{
            imgManga=view.findViewById(R.id.iv_rv_main_big)
            imgManga.isClickable=true

            imgManga.setOnClickListener{
                irActividad(ComicDetail::class.java,idStrComic)
            }

        }
    }


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int)
            : AdapterMangaMainBig.MyViewHolder
    {
        val itemView= LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.rv_main_big_comic,
                        parent,
                        false
                )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaMangas.size
    }

    override fun onBindViewHolder(
            holder: AdapterMangaMainBig.MyViewHolder,
            position: Int
    ) {
        val content=listaMangas[position]
        holder.imgManga.setImageBitmap(content.comigImg)
        holder.idStrComic=content.comicId
    }

    fun irActividad(
            clase:Class<*>,
            idComic:String
    ){
        val intentEx= Intent(
                contexto,
                clase
        )
        intentEx.putExtra("id",idComic)
            startActivity(contexto,intentEx,null)
    }
}