package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentStartBinding
import acc.mobile.comic_app.enums.AsyncResultEnum
import acc.mobile.comic_app.login.viewmodel.AuthData
import acc.mobile.comic_app.login.viewmodel.AuthViewModel
import android.content.Intent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels

class StartFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        _binding!!.mloginBtnSingin.setOnClickListener {
            _binding!!.mloginBtnSingin.onEditorAction(EditorInfo.IME_ACTION_DONE)
            logIn()
        }

        return binding.root
    }


    private fun logIn() {
        val email = binding.mloginEtEmail.editText?.text.toString()
        val password = binding.mloginEtPassword.editText?.text.toString()
        val loginAuthData = AuthData(email, password)
        val loginResult = this.authViewModel.logInWithEmailAndPassword(loginAuthData)
        Toast.makeText(activity, "${loginResult.message}", Toast.LENGTH_SHORT).show()

        when (loginResult.result) {
            AsyncResultEnum.SUCCESS -> {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}