package com.globalhiddenodds.pulseship.ui.fragments.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: String? = null,
    val error: Int? = null
)