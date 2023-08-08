package com.example.projecthallapplication.activity.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.projecthallapplication.R
import com.example.projecthallapplication.databinding.ActivityMainBinding
import com.example.projecthallapplication.fragment.home.ui.HomeFragment
import com.example.projecthallapplication.fragment.login.ui.LoginFragment
import com.example.projecthallapplication.fragment.profile.ui.ProfileFragment
import com.example.projecthallapplication.fragment.signup.ui.SignUpFragment
import com.example.projecthallapplication.fragment.splash.ui.SplashFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var navController: NavController? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFrag: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bottomNavigationView = binding.bottomNavigation

        /*
        * Connect a navHost fragment set up with nav controller
        * */

        navHostFrag =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFrag.navController

        NavigationUI.setupWithNavController(bottomNavigationView, navController!!)


        /*
        * on destination change defien our bottom navigation visibility here
        * */
        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.mnHome -> {
                    showBottomNav()
                }

                R.id.mnAccount -> {
                    showBottomNav()

                }

                R.id.splashFragment -> {
                    hideBottomNav()

                }

                R.id.signUpFragment -> {
                    hideBottomNav()

                }

                R.id.loginFragment -> {
                    hideBottomNav()

                }

                else -> {
                    hideBottomNav()
                }
            }
        }

        //call setup bottomnavigation
        setUpBottomNavigation()

    }

    /*
     * Hide BottomNavigation
     * */

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE

    }

    /*
    * Show BottomNavigation
    */

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE

    }


    /*
    * Set bottom navigation clickable and return true or check true open that only fragment
    * */
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


    /*
    * on back-pressed our fragment is profile that profile fregment redirect to home and clear all backsatck fragment we are exists
    * if home fragment directly finish app
    * else back pressed on back button work until  when all fragment not clear
    * */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        when (navHostFrag.childFragmentManager.fragments[0]) {
            is ProfileFragment -> {
                navController!!.navigate(R.id.mnHome)
                navController!!.clearBackStack(R.id.mnHome)
            }

            is HomeFragment -> {
                finish()

            }

            else -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }


    /*
    * Status bar color set here function
    * */
    fun setStatusBarColor(fragment: Fragment) {
        val colorResId = when (fragment) {
            is HomeFragment -> R.color.white
            is ProfileFragment -> R.color.white
            is SignUpFragment -> R.color.white
            is LoginFragment -> R.color.white
            is SplashFragment -> R.color.sky_blue
            else -> R.color.white
        }

        window.apply {
            statusBarColor = ContextCompat.getColor(this@MainActivity, colorResId)
        }
    }


}