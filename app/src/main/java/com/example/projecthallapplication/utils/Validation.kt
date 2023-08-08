package com.example.projecthallapplication.utils

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern


object Validation {


    fun isEmailValid(email: String): Boolean {
        return when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                true
            }
            else -> false
        }
    }

    fun isMobileValidation(mobileNo: String): Boolean {
        return when {
            !Patterns.PHONE.matcher(mobileNo.trim()).matches() -> {
                true
            }
            else -> false
        }
    }

    fun isMobileValidate(mobileNo: String): Boolean {
        return when {
            mobileNo.length < 8 || mobileNo.length > 14 -> {
                true
            }
            else -> false
        }
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern

        val passwordRegex ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$"


        pattern = Pattern.compile(passwordRegex)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isFieldEmpty(field: String): Boolean {
        return when {
            field.isEmpty() -> {
                true
            }
            field == " " -> {
                true
            }
            field == "null" -> {
                true
            }
            field.equals(null) -> {
                true
            }
            else -> false
        }


    }

    fun isNameValidate(name: String): Boolean {
        return when {
            name.length < 2 || name.length > 100 -> {
                true
            }
            else -> false
        }
    }
}