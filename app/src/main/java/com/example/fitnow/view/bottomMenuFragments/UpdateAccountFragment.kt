package com.example.fitnow.view.bottomMenuFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.fitnow.R
import com.example.fitnow.databinding.FragmentUpdateAccountBinding
import com.example.fitnow.viewmodel.UpdateAccountViewModel
import com.google.firebase.auth.FirebaseAuth

class UpdateAccountFragment : Fragment() {
    private var _binding:FragmentUpdateAccountBinding?=null
    private val binding get()=_binding!!
    lateinit var viewModel:UpdateAccountViewModel
    lateinit var job: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentUpdateAccountBinding.inflate(inflater,container, false)
        viewModel=ViewModelProviders.of(this)[UpdateAccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseAuth.getInstance().currentUser?.let {
            binding.emailEditText.setText(it.email.toString())
        }

        binding.changePassBtn.setOnClickListener {
            binding.changeEmailConstraint.visibility=View.GONE
            job="Pass"
            val userEmail=binding.emailEditText.text.toString()
            val userPassword=binding.passwordEditText.text.toString()
            if(userEmail!="" && userPassword!="") {
                binding.emailEditText.isEnabled=false
                binding.passwordEditText.isEnabled=false
                viewModel.checkEmailPassword(userEmail,userPassword)
            }else viewModel.errorMessage.value=getString(R.string.fill)
        }
        binding.changeEmailBtn.setOnClickListener {
            binding.changePassConstraint.visibility=View.GONE
            job="Email"
            val userEmail=binding.emailEditText.text.toString()
            val userPassword=binding.passwordEditText.text.toString()
            if(userEmail!="" && userPassword!="") {
                binding.emailEditText.isEnabled=false
                binding.passwordEditText.isEnabled=false
                viewModel.checkEmailPassword(userEmail,userPassword)
            }else viewModel.errorMessage.value=getString(R.string.fill)
        }
        binding.changeNewPassBtn.setOnClickListener {
            val newPassword=binding.newPassEditText.text.toString()
            if(newPassword!="") viewModel.changePassword(newPassword)
            else viewModel.errorMessage.value=getString(R.string.fill)
        }
        binding.changeNewEmailBtn.setOnClickListener {
            val newEmail=binding.newEmailEditText.text.toString()
            if(newEmail!="") viewModel.changeEmail(newEmail)
            else viewModel.errorMessage.value=getString(R.string.fill)
        }
        observeLiveData()
    }

    //TODO(şifre eşleşmesi yap)



    private fun observeLiveData() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {error->
            error?.let {
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })
        viewModel.response.observe(viewLifecycleOwner, Observer { response->
            response?.let {
                if (it){
                    if (job=="Pass") binding.changePassConstraint.visibility=View.VISIBLE
                    else binding.changeEmailConstraint.visibility=View.VISIBLE
                }
            }



        })
        viewModel.process.observe(viewLifecycleOwner, Observer { process->
            process?.let {
                binding.changePassConstraint.visibility=View.GONE
                binding.changeEmailConstraint.visibility=View.GONE
                binding.emailEditText.isEnabled=true
                binding.passwordEditText.isEnabled=true
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                binding.updateAccountGroup.isEnabled = !it
            }
        })

    }




}