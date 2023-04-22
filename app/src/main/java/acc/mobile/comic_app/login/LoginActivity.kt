package acc.mobile.comic_app.login

import acc.mobile.comic_app.R
import acc.mobile.comic_app.databinding.ActivityLoginBinding
import acc.mobile.comic_app.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fragment Navigation
        val navController = findNavController(R.id.nav_host_fragment_content_main)
    }
}