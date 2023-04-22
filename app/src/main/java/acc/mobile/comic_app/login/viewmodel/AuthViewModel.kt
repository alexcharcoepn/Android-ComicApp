package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.enums.AsyncResultEnum
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private val _authResult = MutableLiveData<AuthViewModelResult>()
    val authResult: LiveData<AuthViewModelResult> = _authResult

    fun logInWithEmailAndPassword(authData: AuthData) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(authData.email, authData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _authResult.value = AuthViewModelResult(AsyncResultEnum.SUCCESS,"Welcome")
                    } else {
                        _authResult.value = AuthViewModelResult(AsyncResultEnum.FAILURE,"${it.exception?.message}")
                    }
                }
        }
    }

    fun createPasswordAuthAccount(authData: AuthData) {
        auth.createUserWithEmailAndPassword(authData.email, authData.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                } else {

                }
            }
    }
}