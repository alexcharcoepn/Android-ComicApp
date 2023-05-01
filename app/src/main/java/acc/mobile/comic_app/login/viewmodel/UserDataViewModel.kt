package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.Collections
import acc.mobile.comic_app.login.data.UserData
import acc.mobile.comic_app.login.data.OperationResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDataViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private val _userData =
        MutableLiveData(UserData(userId = auth.uid, email = auth.currentUser?.email))
    val userData: LiveData<UserData>
        get() = _userData

    private val _validInputs = MutableLiveData(OperationResult(true))
    val validInputs: LiveData<OperationResult>
        get() = _validInputs

    private val _userDataSaved = MutableLiveData(OperationResult(false))
    val userDataSaved: LiveData<OperationResult>
        get() = _userDataSaved


    fun handleSaveUserData(userData: UserData) {
        _userData.value = userData
        val validationResult = OperationResult(true, null)

        if (userData.name!!.length <= 10 || userData.username!!.length <= 10) {
            validationResult.valid = false
            validationResult.message = "Name/Username length must be greater than 10"
        }

        _validInputs.value = validationResult
        if (validationResult.valid) {
            viewModelScope.launch {
                saveUserData()
            }
        }
    }

    private suspend fun saveUserData() {
        val userData = _userData.value

        withContext(Dispatchers.IO) {
            db.collection(Collections.user)
                .add(userData!!)
                .addOnSuccessListener { documentReference ->
                    Log.d("userdata", "DocumentSnapshot added with ID: ${documentReference.id}")
                    _userDataSaved.value = OperationResult(true, "")
                }
                .addOnFailureListener { e ->
                    Log.w("userdata", "Error adding document", e)
                }
        }
    }


}