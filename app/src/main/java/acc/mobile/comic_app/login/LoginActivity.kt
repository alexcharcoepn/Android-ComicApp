package acc.mobile.comic_app.login

import acc.mobile.comic_app.R
import acc.mobile.comic_app.databinding.ActivityLoginBinding
import acc.mobile.comic_app.databinding.ActivityMainBinding
import acc.mobile.comic_app.login.fragments.SignUpManualFragmentDirections
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fragment Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navhost_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}