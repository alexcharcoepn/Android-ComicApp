package acc.mobile.comic_app.repository

import acc.mobile.comic_app.room.dao.UserDao
import acc.mobile.comic_app.room.entity.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    val user: Flow<User> = userDao.getItem(Firebase.auth.uid.toString())

    suspend fun refreshUserData() {
        withContext(Dispatchers.IO) {
            //TODO: retrieve data from network, instead of mockuser
            val mockUser = User(userId = "")
            userDao.insert(mockUser)
        }
    }


}