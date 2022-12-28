package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fitnow.R
import com.example.fitnow.databinding.FragmentHealthyBinding
import com.example.fitnow.viewmodel.HealthyViewModel


class HealthyFragment : Fragment() {
    private var _binding: FragmentHealthyBinding?=null
    private val binding get()=_binding!!
    lateinit var viewModel: HealthyViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentHealthyBinding.inflate(inflater,container,false)
        viewModel= ViewModelProviders.of(this)[HealthyViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData()
        observeLiveData(view)
    }

    private fun observeLiveData(view: View) {
        viewModel.user.observe(viewLifecycleOwner, Observer { user->
            user?.let {

                println("${it.whIndex} ${it.formattedIndex}")
                if(it.whIndex<18.5){
                    binding.healthChecker.text=getString(R.string.weak)
                        }else if(it.whIndex<25){
                    binding.healthChecker.text=getString(R.string.normal)
                        }else if(it.whIndex<30){
                    binding.healthChecker.text=getString(R.string.normalplus)
                        }else if(it.whIndex<35){
                    binding.healthChecker.text=getString(R.string.obez)
                        }else if(it.whIndex>=35){
                    binding.healthChecker.text=getString(R.string.megaobez)
                        }else{
                    binding.healthChecker.text=getString(R.string.badentry)
                        }
                val newString="Vücut/Kitle Endeksiniz = ${it.formattedIndex}"
                binding.hwindexTextview.text=newString
            }

        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if(it) binding.healthyProgressBar.visibility=View.VISIBLE
                else binding.healthyProgressBar.visibility=View.GONE
            }
        })
        viewModel.isHaveInput.observe(viewLifecycleOwner, Observer { value->
            value?.let {
                if (!it && viewModel.loading.value==false){
                    binding.healthChecker.text=getString(R.string.inputHealth)
                }
            }
        })

    }


}