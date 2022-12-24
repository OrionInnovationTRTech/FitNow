package com.example.fitnow.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.fitnow.R
import com.example.fitnow.ui.onboarding.screens.FirstScreen
import com.example.fitnow.ui.onboarding.screens.SecondScreen
import com.example.fitnow.ui.onboarding.screens.ThirdScreen
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        // Önce kaç tane fragment olduğunu bir dizide tutuyoruz.

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()

        )
        //Sonra listemizi Adapterimize yollayarak view ile adapter arası bağlantı kuruyoruz.
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        view.vwpagerfrag.adapter = adapter
        return view
    }

}