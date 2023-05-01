package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentSignUpManualBinding
import acc.mobile.comic_app.login.viewmodel.AuthViewModel
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

class SignUpManualFragment : Fragment() {
    private var _binding: FragmentSignUpManualBinding? = null
    private val binding get() = _binding!!

    private lateinit var imm: InputMethodManager
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
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

        authViewModel.authResult.observe(viewLifecycleOwner) {
            if (it) {
                val action = SignUpManualFragmentDirections.actionStartToUserdata()
                requireActivity().findNavController(R.id.navhost_fragment_auth).navigate(action)
            }
        }
    }

    private fun signUpHandler() {
        val email = binding.authSignupEtEmail.editText?.text.toString()
        val password = binding.authSignupEtPassword.editText?.text.toString()
        val passwordVerify = binding.authSignupEtPasswordVerify.editText?.text.toString()

        this.authViewModel.handleManualSignUp(email,password,passwordVerify)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
