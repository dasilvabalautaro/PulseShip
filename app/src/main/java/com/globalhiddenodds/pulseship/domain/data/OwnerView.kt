package com.globalhiddenodds.pulseship.domain.data

data class OwnerView(val uid: String = "",
                     val email: String,
                     val token: String = "",
                     val name: String = "",
                     val isEmailVerified: Boolean = false)