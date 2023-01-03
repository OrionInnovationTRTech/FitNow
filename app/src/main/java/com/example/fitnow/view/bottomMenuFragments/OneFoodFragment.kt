package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.fitnow.databinding.FragmentOneFoodBinding
import com.example.fitnow.model.OneFoodItem
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
        var whereToGo=true
        arguments?.let {

            val foodId=OneFoodFragmentArgs.fromBundle(it).mId
            val foodName=OneFoodFragmentArgs.fromBundle(it).foodName
            val foodImage=OneFoodFragmentArgs.fromBundle(it).imageURL
            val foodContent=Html.fromHtml(OneFoodFragmentArgs.fromBundle(it).foodContent)
            whereToGo=OneFoodFragmentArgs.fromBundle(it).fromWhere
            val foodInformation=OneFoodItem(foodId.toString(),foodName,foodImage,foodContent)
            viewModel.getData(foodInformation)
        }
        observeLiveData(view)
        binding.btnBack.setOnClickListener {
            if(whereToGo){
                val action=OneFoodFragmentDirections.actionOneFoodFragmentToFoodFragment()
                Navigation.findNavController(it).navigate(action)
            } else {
                val action=OneFoodFragmentDirections.actionOneFoodFragmentToFavoritesFragment()
                Navigation.findNavController(it).navigate(action)
            }

        }



    }

    private fun observeLiveData(view: View) {
        viewModel.foodLiveData.observe(viewLifecycleOwner, Observer {   oneFood ->
            oneFood?.let {
                binding.foodNameText.text=it.itemName
                binding.foodContentText.text = it.parseContent
                Picasso.get().load(it.itemImage).into(binding.foodImage)
            }
        })
    }

}