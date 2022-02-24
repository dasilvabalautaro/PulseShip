package com.globalhiddenodds.pulseship

import com.globalhiddenodds.pulseship.ui.util.EmailValidator
import org.junit.Assert
import org.junit.Test

class EmailValidatorTest {
    private val emailValidator: EmailValidator = EmailValidator()

    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        Assert.assertTrue(emailValidator.isValidEmail("name@email.com"))
    }

    @Test
    fun emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        Assert.assertTrue(emailValidator.isValidEmail("name@email.co.uk"))
    }

    @Test
    fun emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        Assert.assertFalse(emailValidator.isValidEmail("name@email"))
    }

    @Test
    fun emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        Assert.assertFalse(emailValidator.isValidEmail("name@email..com"))
    }

    @Test
    fun emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        Assert.assertFalse(emailValidator.isValidEmail("@email.com"))
    }

    @Test
    fun emailValidator_EmptyString_ReturnsFalse() {
        Assert.assertFalse(emailValidator.isValidEmail(""))
    }

    @Test
    fun emailValidator_NullEmail_ReturnsFalse() {
        Assert.assertFalse(emailValidator.isValidEmail(null))
    }

}