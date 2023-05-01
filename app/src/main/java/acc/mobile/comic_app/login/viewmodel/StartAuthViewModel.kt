package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.BuildConfig
import acc.mobile.comic_app.areValuedStrings
import acc.mobile.comic_app.isValidEmail
import acc.mobile.comic_app.isValidPassword
import acc.mobile.comic_app.login.data.OperationResult
import acc.mobile.comic_app.room.dao.UserDao
import acc.mobile.comic_app.room.entity.User
import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class StartAuthViewModel(private val userDao: UserDao) : ViewModel() {
    private val auth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient
    val signInIntent: Intent
        get() = googleSignInClient.signInIntent

    private val _validInputs = MutableLiveData(OperationResult(true))
    val validInputs: LiveData<OperationResult>
        get() = _validInputs

    private val _authResult = MutableLiveData(OperationResult(false))
    val authResult: LiveData<OperationResult>
        get() = _authResult

    fun configureGoogleSignInClient(context: Context) {
        this.googleSignInClient = GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.SERVER_SECRET_CLIENT_ID)
                .requestEmail()
                .build()
        )
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        validateInputs(email, password)
        if (_validInputs.value!!.valid) {
            val authResult = auth.signInWithEmailAndPassword(email, password)
            handleAuthData(authResult)
        }
    }

    fun handleGoogleSignIn(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val authResult = auth.signInWithCredential(credential)
                handleAuthData(authResult)
            }
        }
    }

    private fun handleAuthData(authResult: Task<AuthResult>) = authResult
        .addOnSuccessListener {
            val newUser = User(userId = it.user!!.uid, email = it.user!!.email)
            viewModelScope.launch {
                userDao.insert(newUser)
            }
            _authResult.value = OperationResult(true)
        }.addOnFailureListener {
            _authResult.value = OperationResult(false, it.message.toString())
        }


    private fun validateInputs(email: String, password: String) {
        if (!areValuedStrings(listOf(email, password))) {
            _validInputs.value = OperationResult(false, "Field(s) empty")
            return
        }
        if (!isValidEmail(email)) {
            _validInputs.value = OperationResult(false, "Email is not valid")
            return
        }
        if (!isValidPassword(password)) {
            _validInputs.value = OperationResult(false, "Invalid password")
            return
        }
        _validInputs.value = OperationResult(true, null)
    }

}

class StartAuthViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartAuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartAuthViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}