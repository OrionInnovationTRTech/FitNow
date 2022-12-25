package com.example.fitnow.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.fitnow.R
import androidx.navigation.fragment.findNavController
import com.example.fitnow.data.entity.User
import com.example.fitnow.data.userOperations.Register
import com.example.fitnow.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel:RegisterViewModel by viewModels()
    private var userOperation: Register? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userOperation = Register(requireContext())
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding  = FragmentRegisterBinding.inflate(inflater,container,false)
        val view = binding.root
        initilazeUI()
        subscribeUI()
        return view
    }


    private fun initilazeUI(){
        binding.btnRegister.setOnClickListener {
            val newUser= User()
            newUser.isim = binding.registerName.text.toString()
            newUser.username = binding.registerUsername.text.toString()
            newUser.email= binding.registerEmail.text.toString()
            newUser.password=binding.registerPassword.text.toString()
            if(binding.registerPassword2.text.toString()==binding.registerPassword.text.toString()){
                viewModel.register(newUser)
            }else{
                Toast.makeText(context,"Şifreler Eşleşmiyor.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            onDestroy()
            TODO("Fragment geri döndürmeyi nasıl yapılır olmuyorsa bak")
        }

    }



    private fun subscribeUI(){
        viewModel.uiState.observe(viewLifecycleOwner){state->
            when(state){
                RegisterViewModel.UIState.IDLE -> {
                    enableDisableComponents(true)
                }
                RegisterViewModel.UIState.IN_PROGRESS -> {
                    enableDisableComponents(false)
                }
                RegisterViewModel.UIState.REGISTER_FAILED -> {
                    enableDisableComponents(true)
                    Toast.makeText(context, "Kayıt Başarısız", Toast.LENGTH_SHORT).show()
                }
                RegisterViewModel.UIState.REGISTER_SUCCESS -> {
                    findNavController().navigate(R.id.action_registerFragment_to_userHomeActivity)
                }
                else -> {
                    Toast.makeText(context, "RegisterFragment UIstate unhandled: $state", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun enableDisableComponents(value:Boolean) {
        binding.registerEmail.isEnabled=value
        binding.registerName.isEnabled=value
        binding.registerUsername.isEnabled=value
        binding.registerPassword2.isEnabled=value
        binding.registerPassword.isEnabled=value
        binding.btnBack.isEnabled=value
        binding.btnRegister.isEnabled=value
        if(value) binding.progressBarRegister.visibility=View.INVISIBLE
        else binding.progressBarRegister.visibility= View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}