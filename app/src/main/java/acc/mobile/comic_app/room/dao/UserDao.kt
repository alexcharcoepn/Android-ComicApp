package acc.mobile.comic_app.room.dao

import acc.mobile.comic_app.room.entity.User
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(item: User)

    @Update
    suspend fun update(item: User)

    @Delete
    suspend fun delete(item: User)

    @Query("SELECT * from user WHERE userId = :userId")
    fun getItem(userId: String): Flow<User>
}