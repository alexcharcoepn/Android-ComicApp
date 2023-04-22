package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.R
import acc.mobile.comic_app.enums.AsyncResultEnum
import acc.mobile.comic_app.login.data.AuthData
import acc.mobile.comic_app.login.data.AuthProviderEnum
import acc.mobile.comic_app.login.data.AuthViewModelResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import android.content.Context

class AuthViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private val _authResult = MutableLiveData<AuthViewModelResult>()
    val authResult: LiveData<AuthViewModelResult> = _authResult

    fun logInWithEmailAndPassword(authData: AuthData) {

    }

}