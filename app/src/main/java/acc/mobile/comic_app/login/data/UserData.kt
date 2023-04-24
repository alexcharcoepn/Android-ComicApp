package acc.mobile.comic_app.login.data

import java.util.Date

data class UserData(
    var name: String? = null,
    var username: String? = null,
    var birthday: Date? = null,
    var genre: String? = null,
    var email: String? = null,
    var userId: String? = null
)