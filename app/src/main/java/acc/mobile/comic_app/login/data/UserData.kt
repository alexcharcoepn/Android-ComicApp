package acc.mobile.comic_app.login.data

import java.util.Date

data class UserData(
    val name: String,
    val username: String,
    val birthday: Date,
    val genre: String,
    val country: String
)