package acc.mobile.comic_app.login.fragments

import acc.mobile.comic_app.areValuedStrings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentAuthDataBinding
import acc.mobile.comic_app.login.createDatePicker
import acc.mobile.comic_app.login.data.UserData
import android.widget.RadioButton
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        datePicker = createDatePicker()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userdataEtBirthday.editText?.setOnClickListener {
            datePicker.show(parentFragmentManager, "birthday-datepicker")
        }

        binding.userdataBtnSave.setOnClickListener {
            this.saveUserData()
        }

        datePicker.addOnPositiveButtonClickListener {
            setBirthdateUI(it)
        }

        setBirthdateUI(MaterialDatePicker.todayInUtcMilliseconds())

    }

    private fun saveUserData() {
        val username = binding.userdataEtUsername.editText?.text.toString()
        val name = binding.userdataEtUsername.editText?.text.toString()
        val birthdayDateLong = datePicker.selection
        val genreRadioButtonId = binding.userdataRbtnGenre.checkedRadioButtonId
        val genre = binding.root.findViewById<RadioButton>(genreRadioButtonId).text.toString()

        if (!areValuedStrings(listOf(username, name, genre)) || birthdayDateLong == null) {
            Toast.makeText(activity, "Field(s) empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userData = UserData(name, username, Date(birthdayDateLong), genre)

        //TODO: use viewmodel to send request

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