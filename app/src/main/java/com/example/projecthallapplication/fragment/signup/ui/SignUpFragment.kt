package com.example.projecthallapplication.fragment.signup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projecthallapplication.R
import com.example.projecthallapplication.activity.ui.MainActivity
import com.example.projecthallapplication.database.DatabaseHelper
import com.example.projecthallapplication.databinding.FragmentSignUpBinding
import com.example.projecthallapplication.utils.Validation.isEmailValid
import com.example.projecthallapplication.utils.Validation.isFieldEmpty
import com.example.projecthallapplication.utils.Validation.isMobileValidate
import com.example.projecthallapplication.utils.Validation.isValidPassword


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setStatusBarColor(this)

        databaseHelper = DatabaseHelper(requireContext())


        /*
        * This function also check our validation
        * */

        binding.btnSignUp.setOnClickListener {
            val firstName = binding.etFirstNameSignUp.text.toString().trim()
            val lastName = binding.etLastNameSignUp.text.toString().trim()
            val mobileNumber = binding.etMobileSignUp.text.toString().trim()
            val email = binding.etEmailSignUp.text.toString().trim()
            val password = binding.etPasswordSignUp.text.toString().trim()
            val confirmPassword = binding.etConfirmPasswordSignUp.text.toString().trim()
            val receiveEmailSms = binding.cbSignUpEmailSms.isChecked

            when {
                isFieldEmpty(firstName) -> {
                    toastMsg("Please Enter First Name")
                    binding.etFirstNameSignUp.requestFocus()
                }

                isFieldEmpty(lastName) -> {
                    toastMsg("Please Enter Last Name")
                    binding.etLastNameSignUp.requestFocus()

                }

                isFieldEmpty(mobileNumber) -> {
                    toastMsg("Please Enter Mobile Number")
                    binding.etMobileSignUp.requestFocus()

                }

                isMobileValidate(mobileNumber) -> {
                    toastMsg("Please Enter a Valid Mobile Number")
                    binding.etMobileSignUp.requestFocus()

                }

                isFieldEmpty(email) -> {
                    toastMsg("Please Enter Email")
                    binding.etEmailSignUp.requestFocus()

                }

                isEmailValid(email) -> {
                    toastMsg("Please Enter a Valid Email")
                    binding.etEmailSignUp.requestFocus()

                }

                isFieldEmpty(password) -> {
                    toastMsg("Please Enter Password")
                    binding.etPasswordSignUp.requestFocus()

                }

                !isValidPassword(password) -> {
                    toastMsg(
                        "Password must contain 3 type of this 4 option" + "(1 upper case,1 lower case,1 special character,1 digit)" +
                                "& minimum 8 characters."
                    )
                    binding.etPasswordSignUp.requestFocus()

                }

                isFieldEmpty(confirmPassword) -> {
                    toastMsg("Please Enter Confirm Password")
                    binding.etConfirmPasswordSignUp.requestFocus()

                }

                /*
                * check password and confirm password match or not
                * */
                password != confirmPassword -> {
                    toastMsg("Passwords do not match")
                    binding.etConfirmPasswordSignUp.requestFocus()

                }

                else -> {
                    when {

                        /*
                        * This database helper method check our mobile number already exists or not
                        * if exists its gives toast
                        * else go on next steps
                        *
                        * */

                        databaseHelper.isMobileNumberExists(mobileNumber) -> {
                            toastMsg("Mobile number is already registered")
                            binding.etMobileSignUp.requestFocus()

                        }

                        /*
                       * This database helper method check our email id  already exists or not
                       * if exists its gives toast
                       * else go on next steps
                       *
                       * */

                        databaseHelper.isEmailExists(email) -> {
                            toastMsg("Email is already registered")
                            binding.etEmailSignUp.requestFocus()

                        }

                        else -> {
                            val userId = databaseHelper.insertSignUpUser(
                                firstName,
                                lastName,
                                mobileNumber,
                                email,
                                password,
                                receiveEmailSms
                            )

                            /*
                            * This user id check that if user id greater then -1 data inserted successfully redirect login fragment  else un successfully
                            * */

                            if (userId > -1) {

                                val action =
                                    SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                                findNavController().popBackStack()
                                toastMsg("Sign Up SuccessFully")

                            } else {
                                toastMsg("Sign Up UnSuccessFully")
                            }
                        }
                    }
                }
            }
        }


        /*
        * Back arrow we define pop bck stack
        * */
        binding.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

    }


    // Toast Msg Function
    private fun toastMsg(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}