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
import com.example.fitnow.service.getImage
import com.example.fitnow.service.progressDrawable
import com.example.fitnow.viewmodel.OneFoodViewModel

class OneFoodFragment : Fragment() {
    private var _binding:FragmentOneFoodBinding? = null
    private val binding get()=_binding!!
    private lateinit var viewModel:OneFoodViewModel

    private lateinit var foodContentOrj:String


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
        viewModel.loading.value=true
        arguments?.let {
            val foodId=OneFoodFragmentArgs.fromBundle(it).mId
            val foodName=OneFoodFragmentArgs.fromBundle(it).foodName
            val foodImage=OneFoodFragmentArgs.fromBundle(it).imageURL
            foodContentOrj=OneFoodFragmentArgs.fromBundle(it).foodContent
            val foodContent=Html.fromHtml(foodContentOrj)
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
        binding.favCheck.setOnClickListener {
            viewModel.deleteOrFavFood(binding.favCheck.isChecked,foodContentOrj)
        }

    }

    private fun observeLiveData(view: View) {
        viewModel.foodLiveData.observe(viewLifecycleOwner, Observer {   oneFood ->
            oneFood?.let {
                binding.foodNameText.text=it.itemName
                binding.foodContentText.text = it.parseContent
                binding.foodImage.getImage(it.itemImage, progressDrawable(requireContext()))
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if(it){
                    binding.oneFoodGroup.visibility=View.GONE
                    binding.oneFoodProgressBar.visibility=View.VISIBLE
                }else{
                    binding.oneFoodGroup.visibility=View.VISIBLE
                    binding.oneFoodProgressBar.visibility=View.GONE
                }
            }
        })
        viewModel.response.observe(viewLifecycleOwner, Observer {fav->
            fav?.let {
                if(it=="Fav")   binding.favCheck.isChecked=true
                else if(it=="NotFav") binding.favCheck.isChecked=false
                viewModel.loading.value=false
            }
        } )
    }

}