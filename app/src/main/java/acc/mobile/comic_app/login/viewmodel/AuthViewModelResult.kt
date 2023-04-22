package acc.mobile.comic_app.login.viewmodel

import acc.mobile.comic_app.enums.AsyncResultEnum

data class AuthViewModelResult(
    var result: AsyncResultEnum? = null,
    var message: String? = null
)