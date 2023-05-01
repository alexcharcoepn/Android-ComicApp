package acc.mobile.comic_app

import acc.mobile.comic_app.room.AppDatabase
import android.app.Application

class AppAplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}