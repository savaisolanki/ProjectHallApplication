package com.example.projecthallapplication.fragment.login.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projecthallapplication.R
import com.example.projecthallapplication.activity.ui.MainActivity
import com.example.projecthallapplication.database.DatabaseHelper
import com.example.projecthallapplication.databinding.FragmentLoginBinding
import com.example.projecthallapplication.utils.Validation

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setStatusBarColor(this)

        signUpForgot()

        /*
        * initialize Database helper in login fragment
        * */

        databaseHelper = DatabaseHelper(requireContext())

        /*
        * Define shared preference given name and get data via shared preference one time of session manage
        * */
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        /*
        * This function is perform of our validation
        * */

        binding.btnSignIn.setOnClickListener {

            when {

                Validation.isFieldEmpty(binding.etMobileSignIn.text.toString().trim()) -> {
                    toastMsg("Please Enter Mobile No")
                    binding.etMobileSignIn.requestFocus()

                }


                Validation.isMobileValidation(binding.etMobileSignIn.text.toString().trim()) -> {
                    toastMsg("Please Enter Valid Mobile Number ")
                    binding.etMobileSignIn.requestFocus()

                }

                Validation.isMobileValidate(binding.etMobileSignIn.text.toString().trim()) -> {
                    toastMsg("Please enter minimum 8 and maximum 14 digit mobile number ")
                    binding.etMobileSignIn.requestFocus()

                }

                Validation.isFieldEmpty(binding.etPasswordSignIn.text.toString().trim()) -> {
                    toastMsg("Please Enter password")
                    binding.etPasswordSignIn.requestFocus()

                }

                !Validation.isValidPassword(binding.etPasswordSignIn.text.toString().trim()) -> {
                    toastMsg(
                        "Password must contain 3 type of this 4 option" + "(1 upper case,1 lower case,1 special character,1 digit)" +
                                "& minimum 8 characters."
                    )
                    binding.etPasswordSignIn.requestFocus()

                }

                else -> {

                    when {

                        /*
                        * Login with mobile and password which we register our insertuser method
                        * */
                        databaseHelper.loginUser(
                            binding.etMobileSignIn.text.toString(),
                            binding.etPasswordSignIn.text.toString()
                        ) -> {

                            /*
                            * here we can login with mobile number  that check our mobile number is null or not
                            * if null login uncessfully
                            * else save info in shard preference and session is true and login sucessfully
                            * */

                            val loggedInUser =
                                databaseHelper.getUserByMobile(binding.etMobileSignIn.text.toString())

                            if (loggedInUser != null) {

                                val loggedInUserId = loggedInUser.id

                                /*
                                * set current user id
                                *  */
                                databaseHelper.setCurrentUserId(loggedInUserId)

                                val action =
                                    LoginFragmentDirections.actionLoginFragmentToMnHome()
                                findNavController().navigate(action)

                                toastMsg("Sign In Successfully")

                                saveLoginState(true)

                                /*
                                 * put string home fragment we can get this mobile number information get
                                 * */
                                val editor = sharedPreferences.edit()
                                editor.putString(
                                    "CURRENT_USER_MOBILE",
                                    binding.etMobileSignIn.text.toString()
                                )
                                editor.apply()

                            } else {
                                toastMsg("User not found")
                            }
                        }

                        else -> {
                            toastMsg("Sign In Unsuccessfully")
                        }
                    }

                }
            }


        }

    }


    /*
    * This function put boolean value that means our user is login if user kill app and come again start with  home page not with splash screen
    * */
    private fun saveLoginState(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn)
        editor.apply()
    }


    /*
    * This Function define that specific length starting length to ending length we can change color
    * Remove underline false
    * on particular length starting index and ending index perform click event
    * particular string we perform above all operation
    * */
    private fun signUpForgot() {
        binding.tvSignUp.movementMethod = LinkMovementMethod.getInstance()
        val signUp: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }

            override fun onClick(widget: View) {
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                findNavController().navigate(action)
            }


        }


        val spannable =
            SpannableString(resources.getString(R.string.sign_in_text_dont_account_forgot_password))
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red_dark)),
            23,
            29,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )


        spannable.setSpan(
            signUp,
            23,
            29,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        binding.tvSignUp.text = spannable
    }


    /*
    * Define Toast function define our toast method
    * */

    private fun toastMsg(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}