package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnow.FavAdapter
import com.example.fitnow.databinding.FragmentFavoritesBinding
import com.example.fitnow.model.FavoritesItem
import com.example.fitnow.viewmodel.FavoritesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {
    private var _binding:FragmentFavoritesBinding?=null
    private val binding get()=_binding!!
    lateinit var viewModel:FavoritesViewModel
    private val favAdapter=FavAdapter(this, arrayListOf())
    var myLayoutManager: LinearLayoutManager?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentFavoritesBinding.inflate(inflater,container,false)
        viewModel=ViewModelProviders.of(this)[FavoritesViewModel::class.java]
        viewModel.loading.value=true
        myLayoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.favRecyclerView.layoutManager=myLayoutManager
        binding.favRecyclerView.adapter=favAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        viewModel.getDatas()
    }

    private fun observeLiveData() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {loading->
            loading?.let {
                if(it){
                    binding.emptyFav.visibility=View.GONE
                    binding.favCountTextView.visibility=View.GONE
                }else{

                    if(viewModel.itemCount.value==0){
                        binding.emptyFav.visibility=View.VISIBLE
                        binding.favCountTextView.visibility=View.GONE
                    }else{
                        binding.emptyFav.visibility=View.GONE
                        val string="Favorilerinizde ${viewModel.itemCount.value} tane gıda listelenmiştir"
                        binding.favCountTextView.text=string
                        binding.favCountTextView.visibility=View.VISIBLE
                    }
                }
            }
        })

        viewModel.itemCount.observe(viewLifecycleOwner, Observer { count->
            count?.let {
                if(it>0){
                    binding.emptyFav.visibility=View.GONE
                    val string="Favorilerinizde ${viewModel.itemCount.value} tane gıda listelenmiştir"
                    binding.favCountTextView.text=string
                    binding.favCountTextView.visibility=View.VISIBLE
                }
                else if(viewModel.loading.value==false){
                    binding.favCountTextView.visibility=View.GONE
                    binding.emptyFav.visibility=View.VISIBLE
                }
            }
        })




        viewModel.items.observe(viewLifecycleOwner, Observer { items->
            items?.let {
                if(viewModel.itemCount.value!!>0)
                    for(i in 0 until viewModel.itemCount.value!!){
                        favAdapter.updateFavList(it)
                        break
                    }
            }
        })

    }

    fun recyclerOlustur(newFavList: ArrayList<FavoritesItem>){
        viewModel.itemCount.value=newFavList.size
        val myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favRecyclerView.layoutManager = myLayoutManager
        val myAdapter = FavAdapter(this,newFavList)
        favRecyclerView.adapter = myAdapter

    }


}