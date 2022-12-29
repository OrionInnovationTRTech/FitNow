package com.example.fitnow.view.bottomMenuFragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fitnow.R
import com.example.fitnow.databinding.FragmentSettingsBinding
import com.example.fitnow.databinding.SlideHeaderBinding
import com.example.fitnow.model.IMAGE_APPLE
import com.example.fitnow.model.SettingsModel
import com.example.fitnow.service.getImage
import com.example.fitnow.service.progressDrawable
import com.example.fitnow.viewmodel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess

class SettingsFragment : Fragment() {
    private var _binding:FragmentSettingsBinding?=null
    private val binding get()=_binding!!
    lateinit var viewModel:SettingsViewModel
    private var userGender="Erkek"

    //TODO(fragment açıldığında drawer clicked itemleri sıfırla)
    //TODO(Drawer kullanıc bilgilerini yap ------------                                             -> Olmuyor)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSettingsBinding.inflate(inflater,container,false)
        viewModel=ViewModelProviders.of(this)[SettingsViewModel::class.java]
        initializeUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fillDatas()
        observeLiveData()
        // TODO(KLAVYE AÇILINCA SCROLL OLAYINA ÇÖZÜM BUL -----------------> Olmuyor)
    }
    private fun observeLiveData() {
        viewModel.emailSituation.observe(viewLifecycleOwner, Observer {response->
            response?.let {
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })



        viewModel.userDetails.observe(viewLifecycleOwner, Observer { user->
            user?.let {
                binding.uiAge.setText(it.age)
                binding.uiHeight.setText(it.height)
                binding.uiJob.setText(it.job)
                binding.uiWeight.setText(it.weight)
                if(it.gender=="Kadın"){
                    userGender="Kadın"
                    binding.genderRadioGroup.check(R.id.radioButtonFemale)
                } else {
                    userGender="Erkek"
                    binding.genderRadioGroup.check(R.id.radioButtonMale)
                }
                viewModel.fillSpinner(it.exerciseSituation)
            }
        })

        viewModel.spinnerList.observe(viewLifecycleOwner, Observer { exercise->
            exercise?.let {
                val spinner = binding.uispinnerExercise
                val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,it)
                spinner.adapter=arrayAdapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        viewModel.exercise.value=it[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it){
                    binding.settingsProgressBar.visibility=View.VISIBLE
                    enableDisableComponents(false)
                }else{
                    binding.settingsProgressBar.visibility=View.GONE
                    enableDisableComponents(true)
                }
            }
        })

        viewModel.update.observe(viewLifecycleOwner, Observer { update->
            update?.let {
                if (it) Toast.makeText(context,R.string.updateSuccess,Toast.LENGTH_LONG).show()
                else Toast.makeText(context,R.string.updateFailed,Toast.LENGTH_LONG).show()

            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun enableDisableComponents(value: Boolean) {
        if(value) binding.settingsFragmentViewGroup.visibility=View.VISIBLE
        else binding.settingsFragmentViewGroup.visibility=View.GONE
    }


    private fun initializeUI() {
        binding.navigationFragmentContainer.visibility=View.GONE
        binding.slideImage.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        binding.navigationView.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.menuHesapAyarlari -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    binding.settingFragmentContainer.visibility=View.GONE
                    replaceFragment(UpdateAccountFragment())
                }
                R.id.emailOnayla -> verifiedEmail(it)
                R.id.deleteAccount -> deleteAcc()
                R.id.menuCikisYap -> {
                    it.isChecked=false
                    logout()
                }
            }
            true
        }
        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            userGender = if(R.id.radioButtonMale ==checkedId) binding.radioButtonMale.text.toString()
            else binding.radioButtonFemale.text.toString()

        }
        binding.btnSave.setOnClickListener {
            val userHeight=binding.uiHeight.text.toString()
            val userAge=binding.uiAge.text.toString()
            val userWeight=binding.uiWeight.text.toString()
            val userJob=binding.uiJob.text.toString()
            val userExercise=viewModel.exercise.value.toString()
            if(userAge!="" && userJob !="" && userHeight!="" && userWeight !="")
                viewModel.updateUser(SettingsModel(userHeight,userWeight,userAge,userJob,userExercise,userGender),it)
            else viewModel.errorMessage.value= getString(R.string.fill)
        }
    }

    private fun deleteAcc() {
        val alert= AlertDialog.Builder(context)
        alert.setTitle("Hesabı tamamen silmek istediğinizden emin misiniz?")
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton("Evet", DialogInterface.OnClickListener{ dialog, which ->
                viewModel.deleteAccount()
            })
            .setNegativeButton("Hayır",DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            .create().show()
    }


    private fun verifiedEmail(view: MenuItem) {
        viewModel.verifiedEmail()
        view.isChecked=false

    }

    private fun logout() {
        val alert= AlertDialog.Builder(context)
        alert.setTitle("Çıkmak istediğinizden emin misiniz?")
            .setIcon(R.drawable.ic_logout)
            .setPositiveButton("Evet", DialogInterface.OnClickListener{ dialog, which ->
                FirebaseAuth.getInstance().signOut()
                exitProcess(-1)
            })
            .setNegativeButton("Hayır",DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            .create().show()

    }

    private fun replaceFragment(fragment: Fragment) {
        binding.navigationFragmentContainer.visibility=View.VISIBLE
        binding.settingsBGText.text=getString(R.string.mainMenuGoAccSettings)
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navigationFragmentContainer,fragment)
        fragmentTransaction.commit()
    }







}