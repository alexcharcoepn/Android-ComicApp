package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.login.data.AuthData
import acc.mobile.comic_app.login.data.AuthViewModelResult
import acc.mobile.comic_app.login.data.UserData
import acc.mobile.comic_app.login.data.ValidationResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private val _authResult = MutableLiveData<AuthViewModelResult>()
    val authResult: LiveData<AuthViewModelResult> = _authResult

    private val _userData = MutableLiveData<UserData>()
    val userData : LiveData<UserData> = _userData


    fun validateUserData(userData: UserData):ValidationResult{
        val validationResult = ValidationResult(true,"")


        return  validationResult
    }

}