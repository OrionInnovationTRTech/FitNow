package com.example.fitnow.view.splashScreen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.fitnow.R


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        Handler().postDelayed({
            if (onBoardingFinished()){

                val action= SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                Navigation.findNavController(view).navigate(action)
                /*val action= SplashFragmentDirections.actionSplashFragmentToProfileFragment()
                 Navigation.findNavController(view).navigate(action)*/

            }else{
                val action= SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment()
                Navigation.findNavController(view).navigate(action)
            }

        },1500)

        return view
    }

    private fun onBoardingFinished() : Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished",false)
    }

}