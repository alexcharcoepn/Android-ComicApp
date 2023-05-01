package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.R
import acc.mobile.comic_app.areValuedStrings
import acc.mobile.comic_app.databinding.FragmentStartBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.isValidEmail
import acc.mobile.comic_app.isValidPassword
import acc.mobile.comic_app.login.viewmodel.AuthGoogleViewModel
import android.app.Activity
import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private lateinit var imm: InputMethodManager

    private lateinit var authGoogleViewModel: AuthGoogleViewModel

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                authGoogleViewModel.handleGoogleSignIn(task)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authGoogleViewModel = ViewModelProvider(this)[AuthGoogleViewModel::class.java]
        authGoogleViewModel.configureGoogleSignInClient(requireContext())
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)

        binding.mloginBtnSingin.setOnClickListener {
            binding.mloginBtnSingin.onEditorAction(EditorInfo.IME_ACTION_DONE)
            handleEmailPasswordSignIn()
        }

        binding.mloginBtnAuthGoogle.setOnClickListener {
            val signInIntent = authGoogleViewModel.signInIntent
            launcher.launch(signInIntent)
        }

        binding.mloginBtnSignUpEmail.setOnClickListener {
            val action = StartFragmentDirections.actionStartToManualSignup()
            requireActivity().findNavController(R.id.navhost_fragment_auth)?.navigate(action)
        }

        authGoogleViewModel.validInputs.observe(viewLifecycleOwner) {
            if (!it.valid) {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        authGoogleViewModel.authResult.observe(viewLifecycleOwner){
            if(it.valid){
                //TODO: travel to Main vs UserData
            }
        }

        return binding.root
    }


    private fun handleEmailPasswordSignIn() {
        val email = binding.mloginEtEmail.editText?.text.toString()
        val password = binding.mloginEtPassword.editText?.text.toString()

        authGoogleViewModel.signInWithEmailAndPassword(email, password)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}