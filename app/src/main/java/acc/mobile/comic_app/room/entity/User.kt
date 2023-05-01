package acc.mobile.comic_app.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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