package acc.mobile.comic_app.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.R
import acc.mobile.comic_app.databinding.FragmentSignUpManualBinding
import acc.mobile.comic_app.databinding.FragmentStartBinding
import android.view.inputmethod.EditorInfo
import androidx.navigation.findNavController
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

        auth = Firebase.auth

        return binding.root
    }

    private fun signUp(){
    }
}