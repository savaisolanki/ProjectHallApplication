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
        signUpForgot()

        databaseHelper = DatabaseHelper(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        binding.btnSignIn.setOnClickListener {

            when {
                Validation.isFieldEmpty(binding.etEmailSignIn.text.toString().trim()) -> {
                    toastMsg("Please Enter Email")
                    binding.etEmailSignIn.requestFocus()

                }

                Validation.isEmailValid(binding.etEmailSignIn.text.toString().trim()) -> {
                    toastMsg("Please Enter Valid email")
                    binding.etEmailSignIn.requestFocus()

                }

                Validation.isFieldEmpty(binding.etPasswordSignIn.text.toString().trim()) -> {
                    toastMsg("Please password")
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
                        databaseHelper.loginUser(
                            binding.etEmailSignIn.text.toString(),
                            binding.etPasswordSignIn.text.toString()
                        ) -> {
                            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                            findNavController().navigate(action)
                            toastMsg("Sign In Successfully")
                            saveLoginState(true)

                        }

                        else -> {
                            toastMsg("Sign In Unsuccessfully")
                        }
                    }

                }
            }


        }

    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn)
        editor.apply()
    }


    //Spannable String Function
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

    // Toast Msg Function
    private fun toastMsg(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}