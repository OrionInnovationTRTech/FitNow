package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnow.R
import com.example.fitnow.service.MenuListener

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val listener: MenuListener = activity as MenuListener
        listener.showOrHide(true)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

}