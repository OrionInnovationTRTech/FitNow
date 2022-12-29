package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnow.R
import com.example.fitnow.adapter.FoodAdapter
import com.example.fitnow.databinding.FragmentFoodBinding
import com.example.fitnow.model.*
import com.example.fitnow.viewmodel.FoodViewModel
import com.squareup.picasso.Picasso

class FoodFragment : Fragment() {
    private var _binding:FragmentFoodBinding?=null
    private val binding get()=_binding!!
    private lateinit var viewModel:FoodViewModel
    private val foodAdapter= FoodAdapter(arrayListOf())
    var myLayoutManager:LinearLayoutManager?=null
    //TODO(JSoup html parse edici)

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
                    Toast.makeText(context, R.string.cantFoundFood, Toast.LENGTH_LONG).show()
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
        Picasso.get().load(IMAGE_APPLE).into(binding.menuAppleImage)
        Picasso.get().load(IMAGE_BANANA).into(binding.menuBananaImage)
        Picasso.get().load(IMAGE_WATERMELON).into(binding.menuWatermelonImage)
        Picasso.get().load(IMAGE_LEMON).into(binding.menuLemonImage)
    }

}