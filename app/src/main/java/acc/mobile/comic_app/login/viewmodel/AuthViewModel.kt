package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.login.data.UserData
import acc.mobile.comic_app.login.data.ValidationResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    private val _userData = MutableLiveData(UserData(null, null, null, null))
    val userData: LiveData<UserData>
        get() = _userData

    private val _validInputs = MutableLiveData(ValidationResult(true, "null"))
    val validInputs: LiveData<ValidationResult>
        get() = _validInputs


    fun validateUserData(userData: UserData) {
        _userData.value = userData
        val validationResult = ValidationResult(true, null)

        if (userData.name!!.length <= 10 || userData.username!!.length <= 10) {
            validationResult.valid = false
            validationResult.message = "Name/Username length must be greater than 10"
        }

        _validInputs.value = validationResult
    }

}