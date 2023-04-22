package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.MainActivity
import acc.mobile.comic_app.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentStartBinding
import acc.mobile.comic_app.enums.AsyncResultEnum
import acc.mobile.comic_app.login.data.AuthData
import acc.mobile.comic_app.login.viewmodel.AuthViewModel
import acc.mobile.comic_app.login.data.AuthViewModelResult
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d("start-frag","Activity Result: ${it.data}")
            if (it.resultCode == Activity.RESULT_OK) {
                Log.d("start-frag","LogIn Google SUCESSFUL: ${it.data}")
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignIn(task)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.secret_server_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        _binding!!.mloginBtnSingin.setOnClickListener {
            _binding!!.mloginBtnSingin.onEditorAction(EditorInfo.IME_ACTION_DONE)
            email_passwordLogIn()
        }

        _binding!!.mloginBtnAuthGoogle.setOnClickListener {
            Log.d("start-frag","LogIn Google")
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        auth = Firebase.auth

        //Observers
        val authObserver = Observer<AuthViewModelResult> {
            Log.d("start-frag", "Observer res: ${it}")

            Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
            when (it.result) {
                AsyncResultEnum.SUCCESS -> {
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                else -> {
                }
            }
        }

        //Subscriptions to observables
        authViewModel.authResult.observe(this, authObserver)

        return binding.root
    }


    private fun handleSignIn(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result

            Log.d("start-frag","LogIn Google::handleSignIn: ${task.result}")
            if (account != null) {
                updateUI(account)
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        Log.d("start-frag","LogIn Google::udpateUI: ${credential}")
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "INSIDE", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun email_passwordLogIn() {
        val email = binding.mloginEtEmail.editText?.text.toString()
        val password = binding.mloginEtPassword.editText?.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Invalid values", Toast.LENGTH_SHORT).show()
            return
        }

        val loginAuthData = AuthData(email, password)

        this.authViewModel.logInWithEmailAndPassword(loginAuthData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}