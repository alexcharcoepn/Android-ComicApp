package acc.mobile.comic_app.services.retrofit

import com.squareup.moshi.Json

data class MarsPhoto(
    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String
)