package com.example.projecthallapplication.fragment.profile.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.projecthallapplication.R
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
        databaseHelper = DatabaseHelper(requireContext())

        val user: User? = args.user


        if (user == null) {
            val currentUser = databaseHelper.getCurrentUser()
            if (currentUser != null) {
                displayUserDetails(currentUser)
            } else {
                binding.cvProfileDetails.visibility=View.GONE
                Toast.makeText(requireContext(), "There is no logged-in user", Toast.LENGTH_SHORT).show()
            }
        } else {
            displayUserDetails(user)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayUserDetails(user: User?) {
        binding.tvName.text = "${user?.firstName} ${user?.lastName}"
        binding.tvNameData.text = user?.firstName
        binding.tvLastNameData.text = user?.lastName
        binding.tvEmailData.text = user?.email
    }


}