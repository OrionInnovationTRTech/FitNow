package com.example.fitnow.view.bottomMenuFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fitnow.databinding.FragmentProfileBinding
import com.example.fitnow.service.MainActivityListener
import com.example.fitnow.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding?=null
    private val binding get()= _binding!!
    lateinit var viewModel:ProfileViewModel
    lateinit var activityListener:MainActivityListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //TODO(Progress bar koy fotoya)

        _binding=FragmentProfileBinding.inflate(inflater,container,false)
        viewModel= ViewModelProviders.of(this)[ProfileViewModel::class.java]

        activityListener= activity as MainActivityListener
        activityListener.showOrHide(true)
        activityListener.changeBackBtn()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profilescPicture.setOnClickListener {
            selectImage()
        }
        viewModel.getData()
        observeLiveData()
    }



    private fun observeLiveData() {
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it) {
                    enableDisableComponents(true)
                }else{
                    enableDisableComponents(false)
                }
            }
        })

        viewModel.isHaveInput.observe(viewLifecycleOwner, Observer { input->
            input?.let {
                if (!it && viewModel.loading.value==false) {
                    binding.textViewGroup.visibility=View.GONE
                    binding.profilescInfo.visibility=View.VISIBLE
                }else{
                    binding.profilescInfo.visibility=View.GONE
                }

            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage->
            errorMessage?.let {
                if (it!=""){
                    println("Hata var $it")
                }else{
                    println("Hata yok")
                }
            }
        })

        viewModel.userInformation.observe(viewLifecycleOwner, Observer { userDetails->
            userDetails?.let { user->
                Picasso.get().load(user.imageURL).into(binding.profilescPicture)
                binding.profilescName.text=user.nameSurname
                binding.profilescAge.text=user.age
                binding.profilescHeight.text=user.height
                binding.profilescWeight.text=user.weight
                binding.jobTv.text=user.job
                binding.exerciseTv.text=user.exercise
                enableDisableComponents(false)
            }
        })
    }

    private fun enableDisableComponents(value:Boolean) {
        if(value){
            binding.textViewGroup.visibility=View.GONE
            binding.profileFragmentProgressBar.visibility=View.VISIBLE
        }else{
            binding.textViewGroup.visibility=View.VISIBLE
            binding.profileFragmentProgressBar.visibility=View.GONE
        }
    }






//  AŞAĞISI PROFİL FOTOĞRAFI İÇİN İZİN İSTEME VE UPLOAD ETME İŞLEMLERİYLE İLGİLİ

    private fun selectImage() {
        activity?.let {
            getContent.launch("image/*")
        }
    }

    private val uCropContract = object : ActivityResultContract<List<Uri>,Uri>(){
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri=input[0]
            val outputUri=input[1]

            val uCrop= UCrop.of(inputUri,outputUri)
                .withAspectRatio(8F, 8F)
                .withMaxResultSize(800,800)
            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }

    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
        val outputUri=activityListener.getFilesDirBenim().toUri()
        uri?.let {
            val listUri= listOf(it, outputUri)
            cropImage.launch(listUri)
        }

    }

    private val cropImage= registerForActivityResult(uCropContract){ uri->
        binding.profilescPicture.setImageURI(uri)
        viewModel.selectingImage.value=uri
        uri?.let {
            viewModel.uploadImage(viewModel.selectingImage.value!!,requireContext())
        }
    }



    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intentt =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentt, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



}