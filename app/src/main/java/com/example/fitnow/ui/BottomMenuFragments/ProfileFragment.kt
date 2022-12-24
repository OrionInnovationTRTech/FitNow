package com.example.fitnow.ui.BottomMenuFragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.fitnow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception

class ProfileFragment : Fragment() {
    lateinit var selectingImage: Uri
    var selectedImage: Uri? = null              // Resim upload işlemlerinde kullanılacaklar
    var selectedBitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Bu fragment açıldığında yapılacaklar
        fillDatas()
        profilesc_picture.setOnClickListener {
            selectImage(it)
        }
    }

    private fun selectImage(view: View?) {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it.applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intentt =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentt, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.data
            selectingImage = data.data!!
        }
        try {
            context?.let {
                if (selectedImage != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        val source = ImageDecoder.createSource(it.contentResolver, selectedImage!!)
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        profilesc_picture.setImageBitmap(selectedBitmap)
                        Thread.sleep(1000)
                        uploadFile(selectingImage)

                    } else {
                        uploadFile(selectingImage)
                        selectedBitmap =
                            MediaStore.Images.Media.getBitmap(it.contentResolver, selectedImage)
                        Thread.sleep(1000)
                        profilesc_picture.setImageBitmap(selectedBitmap)

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadFile(path: Uri) {
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val imageRef =
            FirebaseStorage.getInstance().reference.child("$userID/images/profilepicture.jpg")
        val pd = ProgressDialog(requireContext())
        pd.setTitle("Resminiz hazırlanıyor")
        pd.show()
        imageRef.putFile(path)
            .addOnSuccessListener {
                pd.dismiss()
                imageRef.downloadUrl
                    .addOnCompleteListener {
                        val imageDatabaseURL = it.result.toString()
                        Log.e("Upload", "Success, imageUrl= $imageDatabaseURL")
                        FirebaseDatabase.getInstance().reference
                            .child("kullanici")
                            .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                            .child("imageURL")
                            .setValue(imageDatabaseURL)
                    }
            }
            .addOnFailureListener {
                pd.dismiss()
                Log.e("Upload", "Failed. ${it.message}")
            }
            .addOnProgressListener {
                val progress = (100 * it.bytesTransferred) / it.totalByteCount
                pd.setMessage("Uploaded: ${progress.toInt()}%")
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    private fun fillDatas() {
        val referans = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        val query = referans.child("kullanici")
            .orderByKey()
            .equalTo(user?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val userName = singleSnapshot.child("isim").value
                    val userAge = singleSnapshot.child("ekstra").child("yas").value
                    val userWeight = singleSnapshot.child("ekstra").child("kilo").value
                    val userHeight = singleSnapshot.child("ekstra").child("boy").value
                    val userJob = singleSnapshot.child("ekstra").child("meslek").value
                    val userExercise = singleSnapshot.child("ekstra").child("egzersizDurumu").value
                    val profilePictureURL = singleSnapshot.child("imageURL").value
                    profilesc_name.text = userName.toString()
                    Picasso.get().load(profilePictureURL.toString()).into(profilesc_picture)
                    if (userAge == null || userHeight == null || userWeight == null) {
                        hideGroup(profilesc_heightTextView, profilesc_height)
                        hideGroup(profilesc_ageTextView, profilesc_age)
                        hideGroup(profilesc_weightTextView, profilesc_weight)
                        hideGroup(jobtvinfo,jobTv)
                        hideGroup(exerciseTv,exerciseStatusTv)
                        profilesc_info.visibility = View.VISIBLE
                    } else {
                        profilesc_info.visibility = View.INVISIBLE
                        showGroup(profilesc_heightTextView, profilesc_height)
                        showGroup(profilesc_ageTextView, profilesc_age)
                        showGroup(profilesc_weightTextView, profilesc_weight)
                        showGroup(jobtvinfo,jobTv)
                        showGroup(exerciseStatusTv,exerciseTv)
                        profilesc_age.setText(userAge.toString())
                        profilesc_height.setText(userHeight.toString())
                        profilesc_weight.setText(userWeight.toString())
                        jobTv.setText(userJob.toString())
                        exerciseTv.setText(userExercise.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("loadPost:onCancelled", error.toException())
            }
        })
    }

    private fun showGroup(texView: TextView, plainText: TextView) {
        texView.visibility = View.VISIBLE
        plainText.visibility = View.VISIBLE
    }

    private fun hideGroup(texView: TextView, plainText: TextView) {
        texView.visibility = View.INVISIBLE
        plainText.visibility = View.INVISIBLE
    }

}

