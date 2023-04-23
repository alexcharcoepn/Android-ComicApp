package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.areValuedStrings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentSignUpManualBinding
import acc.mobile.comic_app.isValidEmail
import acc.mobile.comic_app.isValidPassword
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpManualFragment : Fragment() {
    private var _binding: FragmentSignUpManualBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpManualBinding.inflate(inflater, container, false)

        _binding!!.authBtnSignup.setOnClickListener {
            _binding!!.authBtnSignup.onEditorAction(EditorInfo.IME_ACTION_DONE)
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
            Toast.makeText(activity, "Field(s) empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidEmail(email)) {
            Toast.makeText(activity, "Email is not valid", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != passwordVerify) {
            Toast.makeText(activity, "Password does not match", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidPassword(password)) {
            Toast.makeText(activity, "Invalid password", Toast.LENGTH_SHORT).show()
            return
        }

        this.emailPasswordSignUp(email, password)
    }

    private fun emailPasswordSignUp(email: String, password: String) {

    }
}