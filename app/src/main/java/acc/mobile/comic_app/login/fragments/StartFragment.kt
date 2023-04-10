package acc.mobile.comic_app.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentStartBinding
import acc.mobile.comic_app.login.viewmodel.AuthData
import acc.mobile.comic_app.login.viewmodel.AuthViewModel
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

        //View actions
        _binding!!.mloginBtnSingin.setOnClickListener {
            _binding!!.mloginBtnSingin.onEditorAction(EditorInfo.IME_ACTION_DONE)
            logIn()
        }

        return binding.root
    }


    private fun logIn(){
        val email = binding.mloginEtEmail.editText?.text.toString()
        val password = binding.mloginEtPassword.editText?.text.toString()
        val auth = AuthData(email, password)
        val res = this.authViewModel.logIn(auth)
        Toast.makeText(activity,res,Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}