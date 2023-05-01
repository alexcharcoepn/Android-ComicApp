package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.Collections
import acc.mobile.comic_app.R
import acc.mobile.comic_app.login.data.UserData
import acc.mobile.comic_app.login.data.ValidationResult
import acc.mobile.comic_app.login.fragments.SignUpManualFragmentDirections
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore




    fun emailPasswordSignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                createUserDocument(it.user!!)
            }
            .addOnFailureListener {

            }
    }


    private fun createUserDocument(user: FirebaseUser) {
        val userDoc = UserData(email = user.email, userId = user.uid)
        db.collection(Collections.user)
            .add(userDoc)
            .addOnSuccessListener {
                //val action = SignUpManualFragmentDirections.actionStartToUserdata()
                //activity?.findNavController(R.id.navhost_fragment_auth)?.navigate(action)
            }.addOnFailureListener {
                //handleErrorMessage(it.message.toString())
            }
    }
}