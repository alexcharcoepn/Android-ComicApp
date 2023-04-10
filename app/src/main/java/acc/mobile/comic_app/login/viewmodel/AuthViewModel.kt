package acc.mobile.comic_app.login.viewmodel

import androidx.lifecycle.ViewModel

class AuthViewModel: ViewModel() {
    fun logIn(authData: AuthData):String{
        return "Auth: ${authData.email} -> ${authData.password}"
    }
}