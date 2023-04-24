package acc.mobile.comic_app.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import acc.mobile.comic_app.databinding.FragmentAuthDataBinding
import acc.mobile.comic_app.login.createDatePicker
import android.util.Log
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
    ): View? {
        _binding = FragmentAuthDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userdataEtBirthday.editText!!.setOnClickListener {
            datePicker.show(parentFragmentManager, "birthday-datepicker");
        }

        binding.userdataBtnSave.setOnClickListener {
            Log.d("frag-userdate", "saving")
        }

        datePicker.addOnPositiveButtonClickListener {
            setBirthdate(it)
        }

        setBirthdate(MaterialDatePicker.todayInUtcMilliseconds())

    }


    private fun setBirthdate(dateMillis: Long) {
        val date = Date(dateMillis)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        binding.userdataEtBirthday.editText?.setText(dateFormat.format(date))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}