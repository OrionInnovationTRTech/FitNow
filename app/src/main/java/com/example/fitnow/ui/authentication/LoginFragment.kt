package com.example.fitnow.ui.authentication


import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_home.*

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
           Log.e("LoginFragment",email+password)
                viewModel.login(email,password)
        }

        /*// Şifremi unuttuma basıldığında ekrana ilgili dialog penceresini açıyorum.
        forgotPasswordTextView.setOnClickListener{
            ForgotPasswordFragment().show(supportFragmentManager,"forgotPasswordFragment")
        }*/

        // Kayıt ola bastığında ise registerActivitye yönlendiriyorum
        binding.kayitOlText.setOnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
            TODO("registerFragment oluştur sonrasında burada mvvm uyarla")
        }
    }

    private fun subscribeUI(){
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when(state){
                LoginViewModel.UiState.IDLE -> {
                    enableDisableComponents(true)
                    binding.progressBar2.visibility= View.INVISIBLE
                }
                LoginViewModel.UiState.LOGIN_SUCCESS -> {
                    Toast.makeText(context, "Giriş başarılı. Hoşgeldiniz ", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_userHomeActivity)
                }
                LoginViewModel.UiState.LOGIN_FAILED -> {
                    Toast.makeText(context, "Hatalı giriş.", Toast.LENGTH_SHORT).show()
                    enableDisableComponents(true)
                    binding.progressBar2.visibility= View.INVISIBLE
                }
                LoginViewModel.UiState.IN_PROGRESS -> {
                    enableDisableComponents(false)
                    binding.progressBar2.visibility= View.VISIBLE
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}