package com.example.fitnow.view.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.fitnow.R
import com.example.fitnow.databinding.FragmentLoginBinding
import com.example.fitnow.model.SignIn
import com.example.fitnow.service.MenuListener
import com.example.fitnow.view.MainActivity
import com.example.fitnow.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get()= _binding!!
    lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        observeLiveData(view)


        binding.signUpTextView.setOnClickListener {
            val action= LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.signInButton.setOnClickListener {
            val userEmail=binding.emailText.text.toString()
            val userPassword=binding.passwordLogin.text.toString()
            if(userEmail!="" && userPassword!="") {
                val userInformation = SignIn(userEmail, userPassword)
                viewModel.signInWithFirebase(userInformation)
            }
            else
                Toast.makeText(context,R.string.fill,Toast.LENGTH_LONG).show()

        }

    }

    private fun observeLiveData(view:View) {
        viewModel.loginIsSuccess.observe(viewLifecycleOwner, Observer { isSuccess->
            isSuccess?.let {
                if(it){
                    val action= LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

        viewModel.loginErrorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context,error,Toast.LENGTH_LONG).show()
            }
        })

        viewModel.loginInProgress.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if(it) enableDisableComponents(false)
                else enableDisableComponents(true)
            }
        })
    }

    private fun enableDisableComponents(value:Boolean) {
        binding.signInButton.isEnabled=value
        binding.emailText.isEnabled=value
        binding.passwordLogin.isEnabled=value
        binding.signUpTextView.isClickable=value
        binding.forgotPasswordTextView.isClickable=value
        if(value) binding.loginProgressBar.visibility=View.GONE
        else binding.loginProgressBar.visibility=View.VISIBLE
    }

}