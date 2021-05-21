package com.example.proyecto_2bim.DTO

import com.example.proyecto_2bim.ComicChapter

class MangaDetailDTO(
        var comicImgPort:String="",//
        var comicTitle:String="",//
        var comicGenres:ArrayList<String>?=null,//
        var comicImgLand:String="",//
        var comicAuthor:String="",//
        var comicDesc:String="",//
        var comicChapters:Int?=null,
        var comicStatus:String="",
        var comicStore:ArrayList<String>?=null,
        var comicId:String=""
) {
}

