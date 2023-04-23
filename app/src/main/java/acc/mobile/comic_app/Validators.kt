package acc.mobile.comic_app

import android.util.Patterns


fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String): Boolean =
    "^(?=.*\\d)(?=.*[A-Z]).{8,}$".toRegex().matches(password)

fun areValuedStrings(list: List<String>): Boolean {
    if (list.isEmpty()) {
        return false
    }
    for (string in list) {
        if (string.isBlank()) {
            return false
        }
    }
    return true
}