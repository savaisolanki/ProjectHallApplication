package com.example.projecthallapplication.activity.ui

import AlphabeticalExpandableAdapter
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.projecthallapplication.R
import com.example.projecthallapplication.database.DatabaseHelper
import com.example.projecthallapplication.databinding.ActivityMainBinding
import com.example.projecthallapplication.databinding.DrawerHeaderLayoutBinding
import com.example.projecthallapplication.dataclass.User
import com.example.projecthallapplication.fragment.home.ui.HomeFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), AlphabeticalExpandableAdapter.OnUserClickListener {

    private lateinit var binding: ActivityMainBinding
    var navController: NavController? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationView: NavigationView
    private lateinit var navHostFrag: NavHostFragment
    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var expandableAdapter: AlphabeticalExpandableAdapter
    /*
        private var userList = arrayListOf<User>()
    */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bottomNavigationView = binding.bottomNavigation
        navigationView = binding.navigationView

        navHostFrag =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFrag.navController

        NavigationUI.setupWithNavController(bottomNavigationView, navController!!)
        NavigationUI.setupWithNavController(navigationView, navController!!)


        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.mnHome -> {
                    showBottomNav()
                    unlockDrawer()
                }

                R.id.mnAccount -> {
                    showBottomNav()
                    unlockDrawer()

                }

                R.id.splashFragment -> {
                    binding.toolBar.visibility = View.GONE
                    hideBottomNav()

                }

                R.id.signUpFragment -> {
                    binding.toolBar.visibility = View.GONE
                    hideBottomNav()


                }

                R.id.loginFragment -> {
                    binding.toolBar.visibility = View.GONE
                    hideBottomNav()


                }

                else -> {
                    hideBottomNav()
                }
            }
        }

        databaseHelper = DatabaseHelper(this)

        val userList = databaseHelper.getAllUsers()

        val headerView: View = navigationView.getHeaderView(0)

        val navViewHeaderBinding: DrawerHeaderLayoutBinding =
            DrawerHeaderLayoutBinding.bind(headerView)

        expandableAdapter =
            AlphabeticalExpandableAdapter(this, userList, getGroupList(userList), this)
        navViewHeaderBinding.rvHeaderRecyclerView.adapter = expandableAdapter



        setUpBottomNavigation()

        navViewHeaderBinding.ivLogout.setOnClickListener {
            val currentUser = databaseHelper.getCurrentUser()
            if (currentUser != null) {
                databaseHelper.deleteUserById(currentUser.id)
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                binding.drawerLayout.closeDrawer(GravityCompat.START)

                val newList = databaseHelper.getAllUsers()
                expandableAdapter.updateData(newList)


                navController?.navigate(R.id.splashFragment)
            }

        }


    }

    private fun getGroupList(userList: List<User>): List<Char> {
        return userList.map { it.firstName.first().uppercaseChar() }.distinct().sorted()
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE

    }

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.toolBar.visibility = View.VISIBLE

    }

    private fun unlockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else if (navController?.currentDestination?.id == R.id.mnHome) {
            finish()
        } else {
            navController?.navigateUp()
        }
    }

    override fun onUserClick(user: User) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        val action = HomeFragmentDirections.actionMnHomeToMnAccount(user)
        findNavController(R.id.navHostFragment).navigate(action)
    }

    private fun setUpBottomNavigation() {

        binding.bottomNavigation.menu.findItem(R.id.mnHome).setOnMenuItemClickListener {
            binding.bottomNavigation.menu.findItem(R.id.mnHome).isChecked = true
            navController?.navigate(R.id.mnHome)
            true
        }

        binding.bottomNavigation.menu.findItem(R.id.mnAccount).setOnMenuItemClickListener {
            binding.bottomNavigation.menu.findItem(R.id.mnAccount).isChecked = true
            navController?.navigate(R.id.mnAccount)
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }


}