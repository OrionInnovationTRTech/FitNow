package com.example.fitnow.view.onBoarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fitnow.R
import com.example.fitnow.databinding.FragmentSecondScreenBinding

class SecondScreen : Fragment() {
    private var _binding:FragmentSecondScreenBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSecondScreenBinding.inflate(inflater,container,false)
        val viewPager= activity?.findViewById<ViewPager2>(R.id.viewPagerFrag)

        binding.secScreenNextbtn.setOnClickListener {
            viewPager?.currentItem=2
        }

        binding.secScreenSkip.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("Finished",true)
            editor.apply()
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }

        return binding.root
    }


}