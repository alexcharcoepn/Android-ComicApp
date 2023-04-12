package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.services.retrofit.MarsPhoto
import acc.mobile.comic_app.services.retrofit.RetrofitService
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    fun logIn(authData: AuthData):String{
        lateinit var result:List<MarsPhoto>
        viewModelScope.launch {
            try {
                result = RetrofitService.retrofitService.getPhotos()
            }catch (e: Exception){
                Log.d("services-network:AuthViewModel","${e}")
            }
        }
        return  "${authData} -> ${result}"
    }
}