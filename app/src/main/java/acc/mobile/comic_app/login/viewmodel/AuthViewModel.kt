package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.*
import acc.mobile.comic_app.login.data.UserData
import acc.mobile.comic_app.login.data.OperationResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private val _authResult = MutableLiveData(false)
    val authResult: LiveData<Boolean>
        get() = _authResult

    private val _validInputs = MutableLiveData(OperationResult(true))
    val validInputs: LiveData<OperationResult>
        get() = _validInputs


    fun handleManualSignUp(email: String, password: String, passwordVerify: String) {
        validateInputs(email, password, passwordVerify)
        if (!_validInputs.value!!.valid) {
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                emailPasswordSignUp(email, password)
            }
        }
    }

    private fun emailPasswordSignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                createUserDocument(it.user!!)
            }
            .addOnFailureListener {
                _authResult.value = false
            }
    }

    private fun validateInputs(email: String, password: String, passwordVerify: String) {
        if (!areValuedStrings(listOf(email, password, passwordVerify))) {
            _validInputs.value = OperationResult(false, "Field(s) empty")
            return
        }
        if (!isValidEmail(email)) {
            _validInputs.value = OperationResult(false, "Email is not valid")
            return
        }
        if (password != passwordVerify) {
            _validInputs.value = OperationResult(false, "Email is not valid")
            return
        }
        if (!isValidPassword(password)) {
            _validInputs.value = OperationResult(false, "Invalid password")
            return
        }
        _validInputs.value = OperationResult(true, null)
    }


    private fun createUserDocument(user: FirebaseUser) {
        val userDoc = UserData(email = user.email, userId = user.uid)
        db.collection(Collections.user)
            .add(userDoc)
            .addOnSuccessListener {
                _authResult.value = true
            }.addOnFailureListener {
                _authResult.value = false
            }
    }
}