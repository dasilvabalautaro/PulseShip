package com.globalhiddenodds.pulseship.ui.util

data class InputLoginState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false)