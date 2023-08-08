package com.example.projecthallapplication.utils

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern


object Validation {


    /*
    * This function check input email is matches with email pattern if matches return true else return false
    * */
    fun isEmailValid(email: String): Boolean {
        return when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                true
            }

            else -> false
        }
    }

    /*
    * This function check input mobile is matches with mobile regex if matches return true else return false
    * */

    fun isMobileValidation(mobileNo: String): Boolean {
        return when {
            !Patterns.PHONE.matcher(mobileNo.trim()).matches() -> {
                true
            }

            else -> false
        }
    }

    /*
    * This function check our mobile number greater then 8 but less then 14 return true else false
    * */

    fun isMobileValidate(mobileNo: String): Boolean {
        return when {
            mobileNo.length < 8 || mobileNo.length > 14 -> {
                true
            }

            else -> false
        }
    }

    /*
    * This function check our password length ,small case, upper case ,special  char in our input it return true else return false
    * */

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$"
        pattern = Pattern.compile(passwordRegex)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }


    /*
    * This Function Check
    * 1.Our Field is empty or not
    * 2.our field getting space
    * 3.Our field null
    * 4.our field is equals(null)
    *
    * getting  true we get error from user side else false and matches with regex or input
    * */

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
}