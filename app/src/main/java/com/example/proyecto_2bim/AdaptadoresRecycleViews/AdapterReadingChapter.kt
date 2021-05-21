package com.example.proyecto_2bim.AdaptadoresRecycleViews


import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.proyecto_2bim.ComicChapter
import com.example.proyecto_2bim.R


class AdapterReadingChapter(
        private val listPages :ArrayList<Any>,
        private val contexto: ComicChapter,
        private val recycleView:androidx.recyclerview.widget.RecyclerView
):androidx.recyclerview.widget.
RecyclerView.Adapter<AdapterReadingChapter.MyViewHolder>()
{
    //Code Manga
    val MANGA_CODE:Int=101
    var pageNum:String="Num: "

    inner class MyViewHolder(view: View):
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val ivPage:ImageView

        init{
            ivPage=view.findViewById(R.id.iv_chapter_page_img)
            ivPage.isClickable=true

            ivPage.setOnClickListener {
                Toast.makeText(contexto, "Page Touched: ${pageNum}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int)
            : AdapterReadingChapter.MyViewHolder
    {
        val itemView= LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.rv_chapter_reading,
                        parent,
                        false
                )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listPages.size
    }

    override fun onBindViewHolder(
            holder: AdapterReadingChapter.MyViewHolder,
            position: Int
    ) {
        val content=listPages[position]
        holder.ivPage.setImageBitmap(content as Bitmap)

    }


}