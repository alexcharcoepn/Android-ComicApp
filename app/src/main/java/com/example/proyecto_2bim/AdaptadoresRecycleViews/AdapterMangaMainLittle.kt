
package com.example.proyecto_2bim.AdaptadoresRecycleViews


import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.proyecto_2bim.ComicDetail
import com.example.proyecto_2bim.DTO.MangaMainLittleDTO
import com.example.proyecto_2bim.MainActivity
import com.example.proyecto_2bim.R

class AdapterMangaMainLittle(
        private val listInfo :ArrayList<MangaMainLittleDTO>,
        private val contexto: MainActivity
):androidx.recyclerview.widget.
RecyclerView.Adapter<AdapterMangaMainLittle.MyViewHolder>()
{

    inner class MyViewHolder(view: View):
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val imgManga: ImageView
        val titleManga: TextView
        var idComic:String=""
        init{
            imgManga=view.findViewById(R.id.iv_rv_main_litte)
            titleManga=view.findViewById(R.id.tv_rv_main_little)
            imgManga.isClickable=true
            imgManga.setOnClickListener{
                irActividad(ComicDetail::class.java,idComic)
            }

        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int)
            : AdapterMangaMainLittle.MyViewHolder
    {
        val itemView= LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.rv_main_little_comic,
                        parent,
                        false
                )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listInfo.size
    }

    override fun onBindViewHolder(
            holder: AdapterMangaMainLittle.MyViewHolder,
            position: Int
    ) {
        val contentInfo=listInfo[position]
        val contentImg=listInfo[position].comigImg
        holder.titleManga.setText(contentInfo.comicTitle)
        holder.imgManga.setImageBitmap(contentImg)
        holder.idComic=contentInfo.comicId
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
        ContextCompat.startActivity(contexto, intentEx, null)
    }


}

