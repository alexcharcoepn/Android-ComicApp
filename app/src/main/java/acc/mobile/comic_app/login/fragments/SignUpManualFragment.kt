package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.R
import acc.mobile.comic_app.areValuedStrings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentSignUpManualBinding
import acc.mobile.comic_app.isValidEmail
import acc.mobile.comic_app.isValidPassword
import android.graphics.Color
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpManualFragment : Fragment() {
    private lateinit var _binding: FragmentSignUpManualBinding
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpManualBinding.inflate(inflater, container, false)

        _binding.authBtnSignup.setOnClickListener {
            _binding.authBtnSignup.onEditorAction(EditorInfo.IME_ACTION_DONE)
            this.signUpHandler()
        }

        auth = Firebase.auth

        return binding.root
    }

    private fun signUpHandler() {
        val email = binding.authSignupEtEmail.editText?.text.toString()
        val password = binding.authSignupEtPassword.editText?.text.toString()
        val passwordVerify = binding.authSignupEtPasswordVerify.editText?.text.toString()

        if (!areValuedStrings(listOf(email, password, passwordVerify))) {
            handleErrorMessage("Field(s) empty")
            return
        }
        if (!isValidEmail(email)) {
            handleErrorMessage("Email is not valid")
            return
        }
        if (password != passwordVerify) {
            handleErrorMessage("Password does not match")
            return
        }
        if (!isValidPassword(password)) {
            handleErrorMessage("Invalid password")
            return
        }

        this.emailPasswordSignUp(email, password)
    }

    private fun handleErrorMessage(message:String?){
        binding.authTfFeedback.text = message
        if(message == null){
            binding.authTfFeedback.visibility = View.GONE
            return
        }
        binding.authTfFeedback.visibility = View.VISIBLE
    }

    private fun emailPasswordSignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.auth_fsignup_success),
                    Toast.LENGTH_SHORT
                ).show()
                val action = SignUpManualFragmentDirections.actionStartToUserdata()
                activity?.findNavController(R.id.navhost_fragment_auth)?.navigate(action)
            }
            .addOnFailureListener {
                handleErrorMessage(it.message.toString())
            }
    }
}
