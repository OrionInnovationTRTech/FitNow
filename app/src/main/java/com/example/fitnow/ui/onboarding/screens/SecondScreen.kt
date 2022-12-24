package com.example.fitnow.ui.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fitnow.R
import kotlinx.android.synthetic.main.fragment_second_screen.view.*

class SecondScreen : Fragment() {
    /*override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            val a: Activity
            a= activity!!
            if(a != null){
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
            }
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second_screen, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.vwpagerfrag)

        view.sec_screen_nextbtn.setOnClickListener {
            viewPager?.currentItem =2
        }
        view.sec_screen_skip.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("Finished",true)
            editor.apply()
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }

        return view
    }

}


