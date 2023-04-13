package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.enums.AsyncResultEnum
import acc.mobile.comic_app.services.retrofit.MarsPhoto
import acc.mobile.comic_app.services.retrofit.RetrofitService
import android.util.Log
import android.widget.Toast
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
                        _authResult.value = AuthViewModelResult(AsyncResultEnum.FAILURE,"Error: ${it.exception?.message}")
                    }
                }
                .addOnFailureListener {
                    _authResult.value = AuthViewModelResult(AsyncResultEnum.ERROR,"Error: ${it.message}")
                }
        }
    }

    fun createPasswordAuthAccount(authData: AuthData) {
        auth.createUserWithEmailAndPassword(authData.email, authData.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(
                        "services-network:AuthViewModel:createPasswordAuthAccount",
                        "User created successfully"
                    )
                } else {
                    Log.w(
                        "services-network:AuthViewModel:createPasswordAuthAccount",
                        "Error: ${it.result}"
                    )
                }
            }
    }
}