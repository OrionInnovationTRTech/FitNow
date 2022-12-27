package com.example.fitnow.view.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnow.R
import com.example.fitnow.adapter.OnBoardingAdapter
import com.example.fitnow.databinding.FragmentOnBoardingBinding
import com.example.fitnow.view.onBoarding.screens.*
import com.example.fitnow.view.onBoarding.*

class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get()=_binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentOnBoardingBinding.inflate(inflater,container,false)

        // Önce kaç tane fragment olduğunu bir dizide tutuyoruz.

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        //Sonra listemizi Adapterimize yollayarak view ile adapter arası bağlantı kuruyoruz.
        val adapter = OnBoardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPagerFrag.adapter= adapter
        return binding.root
    }


}