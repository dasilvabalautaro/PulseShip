package com.globalhiddenodds.pulseship.ui.util

import java.util.regex.Pattern

object EmailValidator{
    private val emailPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isValidEmail(email: CharSequence?): Boolean {
        return email != null && emailPattern.matcher(email).matches()
    }
}