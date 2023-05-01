package acc.mobile.comic_app.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: String,
    val userId: String,
    val username: String,
    val email: String,
    val name: String,
    val birthday: Date,
    val genre: String,
)