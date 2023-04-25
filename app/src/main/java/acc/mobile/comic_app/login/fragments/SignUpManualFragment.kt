package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentSignUpManualBinding
import acc.mobile.comic_app.login.data.UserData
import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpManualFragment : Fragment() {
    private var _binding: FragmentSignUpManualBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var imm: InputMethodManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpManualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authBtnSignup.setOnClickListener {
            imm.hideSoftInputFromWindow(requireActivity().window.currentFocus!!.windowToken, 0)
            this.signUpHandler()
        }
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

    private fun handleErrorMessage(message: String?) {
        binding.authTfFeedback.text = message
        if (message == null) {
            binding.authTfFeedback.visibility = View.GONE
            return
        }
        binding.authTfFeedback.visibility = View.VISIBLE
    }

    private fun emailPasswordSignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                createUserDocument(it.user!!)
            }
            .addOnFailureListener {
                handleErrorMessage(it.message.toString())
            }
    }

    private fun createUserDocument(user: FirebaseUser) {
        val userDoc = UserData(email = user.email, userId = user.uid)
        db.collection(Collections.user)
            .add(userDoc)
            .addOnSuccessListener {
                Toast.makeText(activity, "User Created", Toast.LENGTH_SHORT).show()
                val action = SignUpManualFragmentDirections.actionStartToUserdata()
                activity?.findNavController(R.id.navhost_fragment_auth)?.navigate(action)
            }.addOnFailureListener {
                handleErrorMessage(it.message.toString())
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
