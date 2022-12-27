package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.fitnow.R
import com.example.fitnow.data.entity.MealData
import com.example.fitnow.databinding.FragmentOneFoodBinding
import com.example.fitnow.viewmodel.OneFoodViewModel
import com.squareup.picasso.Picasso

class OneFoodFragment : Fragment() {
    private var _binding:FragmentOneFoodBinding? = null
    private val binding get()=_binding!!
    private lateinit var viewModel:OneFoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentOneFoodBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProviders.of(this)[OneFoodViewModel::class.java]
        arguments?.let {
            val foodInformation=MealData.Results()
            foodInformation.mId=OneFoodFragmentArgs.fromBundle(it).mId
            foodInformation.resultName=OneFoodFragmentArgs.fromBundle(it).foodName
            foodInformation.image=OneFoodFragmentArgs.fromBundle(it).imageURL
            foodInformation.content=OneFoodFragmentArgs.fromBundle(it).foodContent
            viewModel.getDataFromRoom(foodInformation)
        }
        observeLiveData(view)
        binding.btnBack.setOnClickListener {
            val action=OneFoodFragmentDirections.actionOneFoodFragmentToFoodFragment()
            Navigation.findNavController(it).navigate(action)
        }



    }

    private fun observeLiveData(view: View) {
        viewModel.foodLiveData.observe(viewLifecycleOwner, Observer {   oneFood ->
            oneFood?.let {
                binding.foodNameText.text=it.resultName.toString()
                binding.foodContentText.text=it.content.toString()
                Picasso.get().load(it.image.toString()).into(binding.foodImage)
            }
        })
    }

}