package acc.mobile.comic_app.room.entity

import acc.mobile.comic_app.room.utils.DateConverter
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: String,

    val username: String? = null,

    val email: String? = null,

    val name: String? = null,

    val birthday: Date? = null,

    val genre: String? = null,
)