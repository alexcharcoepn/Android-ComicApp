package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.AppAplication
import acc.mobile.comic_app.R
import acc.mobile.comic_app.databinding.FragmentStartBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.login.viewmodel.StartAuthViewModel
import acc.mobile.comic_app.login.viewmodel.StartAuthViewModelFactory
import android.app.Activity
import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private lateinit var imm: InputMethodManager

    private val startAuthViewModel: StartAuthViewModel by activityViewModels {
        StartAuthViewModelFactory(
            (activity?.application as AppAplication).database.itemDao()
        //TODO: pass into the factory the Context
        )
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                startAuthViewModel.handleGoogleSignIn(task)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAuthViewModel.configureGoogleSignInClient(requireContext())
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
            val signInIntent = startAuthViewModel.signInIntent
            launcher.launch(signInIntent)
        }

        binding.mloginBtnSignUpEmail.setOnClickListener {
            val action = StartFragmentDirections.actionStartToManualSignup()
            requireActivity().findNavController(R.id.navhost_fragment_auth).navigate(action)
        }

        startAuthViewModel.validInputs.observe(viewLifecycleOwner) {
            if (!it.valid) {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        startAuthViewModel.authResult.observe(viewLifecycleOwner) {
            if (it.valid) {
                /*
                TODO: travel to Main vs UserData: check local data is existent, also try to retrieve it
                      Try to retrieve data from back, if exists it is an login else an signup
                      Create the LocalStorage layer with Repo using Room then come back and connect
                 */
            }
        }

        return binding.root
    }


    private fun handleEmailPasswordSignIn() {
        val email = binding.mloginEtEmail.editText?.text.toString()
        val password = binding.mloginEtPassword.editText?.text.toString()

        startAuthViewModel.signInWithEmailAndPassword(email, password)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}