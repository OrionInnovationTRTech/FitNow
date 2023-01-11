package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnow.R
import com.example.fitnow.adapter.FoodAdapter
import com.example.fitnow.databinding.FragmentFoodBinding
import com.example.fitnow.model.*
import com.example.fitnow.service.getImage
import com.example.fitnow.service.progressDrawable
import com.example.fitnow.viewmodel.FoodViewModel

class FoodFragment : Fragment() {
    private var _binding:FragmentFoodBinding?=null
    private val binding get()=_binding!!
    private lateinit var viewModel:FoodViewModel
    private val foodAdapter= FoodAdapter(arrayListOf())
    var myLayoutManager:LinearLayoutManager?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentFoodBinding.inflate(inflater,container,false)
        viewModel= ViewModelProviders.of(this)[FoodViewModel::class.java]
        viewModel.refreshData(lastClickedItem)
        setClickableItems()
        myLayoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewFood.layoutManager = myLayoutManager
        binding.recyclerviewFood.adapter= foodAdapter
        observeLiveData()
        initiliazeMenu()

        return binding.root
    }

    private fun setClickableItems() {
        binding.menuAppleImage.setOnClickListener {
            lastClickedItem="apple"
            viewModel.refreshData("apple")
        }
        binding.menuBananaImage.setOnClickListener {
            lastClickedItem="banana"
            viewModel.refreshData("banana")
        }
        binding.menuLemonImage.setOnClickListener {
            lastClickedItem="lemon"
            viewModel.refreshData("lemon")
        }
        binding.menuWatermelonImage.setOnClickListener {
            lastClickedItem="watermelon"
            viewModel.refreshData("watermelon")
        }
        binding.searchFoodBtn.setOnClickListener {
            val foodName=binding.searchByNameEditText.text.toString()
            if(foodName!=""){
                lastClickedItem="foodName"
                viewModel.refreshData(foodName)
            }
            else Toast.makeText(context,R.string.cantFoundFood,Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeLiveData() {
        viewModel.foodLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it){
                    binding.recyclerviewFood.visibility=View.GONE
                    binding.searchProgressBar.visibility=View.VISIBLE
                }else{
                    binding.recyclerviewFood.visibility=View.VISIBLE
                    binding.searchProgressBar.visibility=View.GONE
                }
            }

        })

        viewModel.foodList.observe(viewLifecycleOwner, Observer { foods->
            foods?.let {
                foodAdapter.updateFoodList(foods)
            }

        })

        viewModel.foodError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.searchFailedTextView.visibility=View.VISIBLE
                    binding.recyclerviewFood.visibility=View.GONE
                }else{
                    binding.recyclerviewFood.visibility=View.VISIBLE
                    binding.searchFailedTextView.visibility=View.GONE
                }
            }
        })
    }

    private fun initiliazeMenu() {
        val progressDrawable= progressDrawable(requireContext())
        binding.menuAppleImage.getImage(IMAGE_APPLE, progressDrawable)
        binding.menuBananaImage.getImage(IMAGE_BANANA,progressDrawable)
        binding.menuWatermelonImage.getImage(IMAGE_WATERMELON,progressDrawable)
        binding.menuLemonImage.getImage(IMAGE_LEMON,progressDrawable)
    }

}