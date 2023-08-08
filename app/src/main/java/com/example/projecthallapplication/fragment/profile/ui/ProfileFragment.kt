package com.example.projecthallapplication.fragment.profile.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.projecthallapplication.R
import com.example.projecthallapplication.activity.ui.MainActivity
import com.example.projecthallapplication.database.DatabaseHelper
import com.example.projecthallapplication.databinding.FragmentProfileBinding
import com.example.projecthallapplication.dataclass.User

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setStatusBarColor(this)

        /*
        * Shared Preference we have to use because we can get user mobile number  and display all data
        * */
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentUserEmail = sharedPreferences.getString("CURRENT_USER_MOBILE", null)


        databaseHelper = DatabaseHelper(requireContext())

        /*here we can get current user data */
        val currentUser = currentUserEmail?.let { databaseHelper.getCurrentUser(it) }


        /*we have use safe args here because of any click on particular user we can get that particular user all information via safe args*/
        val user: User? = args.user


        /*
        * This Function Check user is null but current user not null that mean may be our safe args null
        *  but in shared preference our current user data  mobile or email not be null
        * we have to display current users information via shared preference
        *
        * else  we define that is no any user login
        *
        * else  display safe args data of particular click user name data
        * */
        if (user == null) {
            if (currentUser != null) {
                displayUserDetails(currentUser)
            } else {
                binding.cvProfileDetails.visibility = View.GONE
                Toast.makeText(requireContext(), "There is no logged-in user", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            displayUserDetails(user)
        }

    }


    /*
    * Here we can display data we are stone in database
    * */
    @SuppressLint("SetTextI18n")
    private fun displayUserDetails(user: User?) {
        binding.tvName.text = "${user?.firstName} ${user?.lastName}"
        binding.tvNameData.text = user?.firstName
        binding.tvLastNameData.text = user?.lastName
        binding.tvEmailData.text = user?.email
        binding.tvMobileData.text = user?.mobileNumber

    }


}