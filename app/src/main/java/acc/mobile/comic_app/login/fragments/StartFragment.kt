package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.R
import acc.mobile.comic_app.areValuedStrings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentStartBinding
import acc.mobile.comic_app.isValidEmail
import acc.mobile.comic_app.isValidPassword
import acc.mobile.comic_app.login.LoginActivity
import android.app.Activity
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleGoogleSignIn(task)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.secret_server_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        _binding!!.mloginBtnSingin.setOnClickListener {
            _binding!!.mloginBtnSingin.onEditorAction(EditorInfo.IME_ACTION_DONE)
            handleEmailPasswordSignIn()
        }

        _binding!!.mloginBtnAuthGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        _binding!!.mloginBtnSignUpEmail.setOnClickListener {
            val action = StartFragmentDirections.actionStartToManualSignup()
            activity?.findNavController(R.id.navhost_fragment_auth)?.navigate(action)
        }

        auth = Firebase.auth

        return binding.root
    }


    private fun handleGoogleSignIn(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val authResult = auth.signInWithCredential(credential)
                navigateToUserData(authResult)
            }
        }
    }

    private fun handleEmailPasswordSignIn() {
        val email = binding.mloginEtEmail.editText?.text.toString()
        val password = binding.mloginEtPassword.editText?.text.toString()

        if (!areValuedStrings(listOf(email, password))) {
            Toast.makeText(activity, "Field(s) empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidEmail(email)) {
            Toast.makeText(activity, "Email is not valid", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidPassword(password)) {
            Toast.makeText(activity, "Invalid password", Toast.LENGTH_SHORT).show()
            return
        }
        val authResult = auth.signInWithEmailAndPassword(email, password)

    }

    private fun navigateToUserData(authResult: Task<AuthResult>) {
        authResult.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "INSIDE", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}