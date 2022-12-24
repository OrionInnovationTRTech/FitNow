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
import kotlinx.android.synthetic.main.fragment_first_screen.view.*

class FirstScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_first_screen, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.vwpagerfrag)

        //First screen 0 olarak geçiyor ileri butonuna basıldığında sıradaki fragment indexi 1 olduğundan
        // burada onu belirtiyorum fragment manager ile bu işlem sağlanıyor.

        view.btn_getstarted.setOnClickListener {
            viewPager?.currentItem =1
        }

        // skip işlemi yapıldığında sharedpref ayarlarına onboarding bittiğine dair true döndürüyorum.
        view.first_screen_skip.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("Finished",true)
            editor.apply()
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
        }

        return view
    }

}