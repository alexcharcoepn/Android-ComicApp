package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.R
import acc.mobile.comic_app.areValuedStrings
import acc.mobile.comic_app.databinding.FragmentAuthDataBinding
import acc.mobile.comic_app.login.createDatePicker
import acc.mobile.comic_app.login.data.UserData
import acc.mobile.comic_app.login.viewmodel.AuthViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class AuthDataFragment : Fragment() {
    private var _binding: FragmentAuthDataBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var datePicker: MaterialDatePicker<Long>

    private lateinit var userDataViewModel: AuthViewModel

    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        datePicker = createDatePicker()
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_data, container, false)
        userDataViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userDataViewModel = userDataViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        binding.userdataEtBirthday.editText?.setOnClickListener {
            datePicker.show(parentFragmentManager, "birthday-datepicker")
        }

        binding.userdataBtnSave.setOnClickListener {
            imm.hideSoftInputFromWindow(requireActivity().window.currentFocus!!.windowToken, 0)
            this.saveUserData()
        }

        datePicker.addOnPositiveButtonClickListener {
            setBirthdateUI(it)
        }

        setBirthdateUI(MaterialDatePicker.todayInUtcMilliseconds())
    }

    private fun saveUserData() {
        val username = binding.userdataEtUsername.editText?.text.toString()
        val name = binding.userdataEtName.editText?.text.toString()
        val birthdayDateLong = datePicker.selection
        val genreRadioSelected =
            binding.root.findViewById<RadioButton>(binding.userdataRbtnGenre.checkedRadioButtonId)

        if (!areValuedStrings(listOf(username, name))
            || birthdayDateLong == null || genreRadioSelected == null
        ) {
            Toast.makeText(activity, "Field(s) empty", Toast.LENGTH_SHORT).show()
            return
        }
        val userData =
            UserData(name, username, Date(birthdayDateLong), genreRadioSelected.text.toString())
        userDataViewModel.validateUserData(userData)
    }

    private fun setBirthdateUI(dateMillis: Long) {
        val date = Date(dateMillis)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        binding.userdataEtBirthday.editText?.setText(dateFormat.format(date))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}