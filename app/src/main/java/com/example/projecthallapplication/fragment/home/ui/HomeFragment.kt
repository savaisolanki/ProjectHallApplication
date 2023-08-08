package com.example.projecthallapplication.fragment.home.ui

import AlphabeticalExpandableAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projecthallapplication.R
import com.example.projecthallapplication.activity.ui.MainActivity
import com.example.projecthallapplication.database.DatabaseHelper
import com.example.projecthallapplication.databinding.FragmentHomeBinding
import com.example.projecthallapplication.dataclass.User


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var expandableAdapter: AlphabeticalExpandableAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userList: List<User>
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setStatusBarColor(this)


        /*
        * initialize shared preference and database helper class
        */

        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val currentUserMobileNo = sharedPreferences.getString("CURRENT_USER_MOBILE", null)


        databaseHelper = DatabaseHelper(requireContext())

        /*
        * Get all user we are registerd
        * */

        userList = databaseHelper.getAllUsers()

        /*
        * get current User information in display
        */

        val currentUser = currentUserMobileNo?.let { databaseHelper.getCurrentUser(it) }

        /*
        * check current user mobile number  empty or not null
        * inside current user not null
        * we can set data in textview
        * */
        if (!currentUserMobileNo.isNullOrEmpty()) {
            if (currentUser != null) {
                binding.tvSignInName.text = "${currentUser.firstName} ${currentUser.lastName}"
                binding.tvSignInEmail.text = currentUser.email

            }
        }

        /*
        * Set our Recycler view
        */
        recyclerView()


        /*
        * On Logout btn we open alert dialog
        * depend on user if click on yes  user logout
        * if no  alert dialog close
        * */
        binding.ivLogout.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("LogOut")
            builder.setMessage("Do You Want To Logout Here?")

            builder.setPositiveButton("Yes") { _, _ ->

                /*
                * check current user not null delete current user it shared preference clear
                * and generate new user list and update it
                */

                if (currentUser != null) {
                    databaseHelper.deleteUserById(currentUser.id)

                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    val newList = databaseHelper.getAllUsers()
                    expandableAdapter.updateData(newList)
                    findNavController().navigate(R.id.action_to_loginFragment)
                }

            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()

            }

            builder.show()


        }


    }

    /*
    * On Resume Call RecyclerView*/
    override fun onResume() {
        super.onResume()
        recyclerView()

    }


    /*
    * This function map that if we define name is savai it district with firstname first char with Uppercase and then Sort it
    * Like savai->firstName.first.uppercase(S).district.sort
    * */
    private fun getGroupList(userList: List<User>): List<Char> {
        return userList.map { it.firstName.first().uppercaseChar() }.distinct().sorted()
    }


    /*
    * In This Function We set our recycler view with adapter
    * we pass data in constructor context,all user list and char and define click event we direct that particular profile fragment
    * */
    private fun recyclerView() {
        expandableAdapter =
            AlphabeticalExpandableAdapter(
                requireContext(),
                userList,
                getGroupList(userList),
                object : AlphabeticalExpandableAdapter.OnUserClickListener {
                    override fun onUserClick(user: User) {
                        val action = HomeFragmentDirections.actionMnHomeToMnAccount(user)
                        findNavController().navigate(action)
                    }
                })
        binding.rvHeaderRecyclerView.adapter = expandableAdapter

    }


}