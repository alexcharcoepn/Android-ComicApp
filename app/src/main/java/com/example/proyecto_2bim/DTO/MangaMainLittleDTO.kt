package com.example.proyecto_2bim.DTO

import android.graphics.Bitmap

class MangaMainLittleDTO (
        var comicPosterLittle:String="",
        var comicTitle:String="",
        var comicId:String="",
        var comigImg:Bitmap?=null
        ){

        override fun toString(): String {
                return comicId+"  TITLE: "+comicTitle+"  POSTER  "+comicPosterLittle
        }
}