package com.example.projecthallapplication.fragment.splash.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.projecthallapplication.R
import com.example.projecthallapplication.activity.ui.MainActivity
import com.example.projecthallapplication.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)


        /*
        * initialize shared preference and its name like MyPrefs
        */
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        /*
        * THis isLoggedIn Check  shared preference getBoolean value is true
        * Redirect home fragment
        * else take 3000 millisecond and redirect login screen
        *
        * */
        val isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        if (isLoggedIn) {
            val action = SplashFragmentDirections.actionSplashFragmentToMnHome2()
            findNavController().navigate(action)
        } else {
            lifecycleScope.launch {
                delay(3000)
                val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setStatusBarColor(this)

        /*
        * This is rotate animation in splash screen we define one image that image we perform rotation animation for 2900 millisecond
        */
        val anim = RotateAnimation(0f, 350f, 60f, 60f)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        anim.duration = 2900

        binding.ivGroup.startAnimation(anim)
    }

}