package com.example.fitnow.view.authentication

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
import com.example.fitnow.databinding.FragmentRegisterBinding
import com.example.fitnow.model.SignUp
import com.example.fitnow.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get()=_binding!!
    lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProviders.of(this)[RegisterViewModel::class.java]
        observeLiveData(view)

        binding.btnRegister.setOnClickListener {
            val userNameSurname= binding.registerName.text.toString()
            val userUsername=binding.registerUsername.text.toString()
            val userEmail=binding.registerEmail.text.toString()
            val userPassword=binding.registerPassword.text.toString()
            val userPassword2=binding.registerPassword2.text.toString()
            if(userNameSurname!=""&& userEmail!="" && userUsername!="" &&
                userPassword!="" && userPassword2!=""){
                if(userPassword==userPassword2){
                    val userInformation= SignUp(userNameSurname,userUsername,userEmail,userPassword)
                    viewModel.signUpWithFirebase(userInformation)
                }else{
                    Toast.makeText(context,R.string.passNotMatch, Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context,R.string.fill, Toast.LENGTH_LONG).show()
            }

        }
        binding.btnBack.setOnClickListener {
            val action= RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }

    private fun observeLiveData(view: View) {
        viewModel.registerIsSuccess.observe(viewLifecycleOwner, Observer { isSuccess->
            isSuccess?.let {
                if (it){
                    Toast.makeText(context,R.string.registerSuccess,Toast.LENGTH_LONG).show()
                    val action=RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

        viewModel.registerInProgress.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if(it) enableDisableComponents(false)
                else enableDisableComponents(true)
            }
        })

        viewModel.registerErrorMessage.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                Toast.makeText(context,error,Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun enableDisableComponents(value: Boolean) {
        binding.registerName.isEnabled=value
        binding.registerUsername.isEnabled=value
        binding.registerEmail.isEnabled=value
        binding.registerPassword.isEnabled=value
        binding.registerPassword2.isEnabled=value
        binding.btnRegister.isClickable=value
        binding.btnBack.isClickable=value
        if(value) binding.progressBarRegister.visibility=View.GONE
        else binding.progressBarRegister.visibility=View.VISIBLE
    }
}