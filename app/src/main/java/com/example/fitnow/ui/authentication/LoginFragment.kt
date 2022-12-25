package com.example.fitnow.ui.authentication



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fitnow.R
import com.example.fitnow.data.userOperations.Login
import com.example.fitnow.databinding.FragmentLoginBinding
import com.example.fitnow.ui.user.ForgotPasswordFragment


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get()= _binding!!
    private val viewModel:LoginViewModel by viewModels()
    private var userOperation: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userOperation = Login(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        initiliazeUI()
        subscribeUI()
        return view
    }

    private fun initiliazeUI(){
       binding.btnGiris.setOnClickListener {
           val email= binding.emailText.text.toString()
           val password = binding.girisSifre.text.toString()
           viewModel.login(email,password)
        }

        // Şifremi unuttuma basıldığında ekrana ilgili dialog penceresini açıyorum.
        binding.forgotPasswordTextView.setOnClickListener{
            ForgotPasswordFragment().show(childFragmentManager,"forgotPasswordFragment")
        }

        // Kayıt ola bastığında ise registerActivitye yönlendiriyorum
        binding.kayitOlText.setOnClickListener {
           findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun subscribeUI(){
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when(state){
                LoginViewModel.UiState.IDLE -> {
                    enableDisableComponents(true)
                }
                LoginViewModel.UiState.LOGIN_SUCCESS -> {
                    Toast.makeText(context, "Giriş başarılı. Hoşgeldiniz ", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_userHomeActivity)
                }
                LoginViewModel.UiState.LOGIN_FAILED -> {
                    Toast.makeText(context, "Hatalı giriş.", Toast.LENGTH_SHORT).show()
                    enableDisableComponents(true)
                }
                LoginViewModel.UiState.IN_PROGRESS -> {
                    enableDisableComponents(false)
                }
                else -> {
                    Toast.makeText(context, "LoginFragment UIstate unhandled: $state", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    private fun enableDisableComponents(value:Boolean){
        binding.btnGiris.isEnabled = value
        binding.emailText.isEnabled = value
        binding.girisSifre.isEnabled = value
        if(value) binding.progressBar2.visibility= View.INVISIBLE
        else binding.progressBar2.visibility= View.VISIBLE


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}