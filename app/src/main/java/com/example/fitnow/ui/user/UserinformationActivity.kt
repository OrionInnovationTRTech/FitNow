package com.example.fitnow.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.fitnow.R
import com.example.fitnow.data.entity.User
import com.example.fitnow.ui.authentication.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_userinformation.*

class UserinformationActivity : AppCompatActivity() {
    lateinit var myAuthStateListener: FirebaseAuth.AuthStateListener
    private var gender:String = "Erkek"
    var exerciseStatus = "Hiç"
    lateinit var radioBtnMale:RadioButton
    val exerciseTimes= arrayOf("Hiç","1-2 Kez","3-5 Kez","7 Kez","7+ Kez")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinformation)
        initAuthStateListener()
        fillDatas()
        val spinner = findViewById<Spinner>(R.id.uispinner_exercise)
        val arrayAdapter =ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,exerciseTimes)
        spinner.adapter=arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               exerciseStatus=exerciseTimes[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Burada birşey yapılmayacak")
            }

        }
        radioBtnMale = findViewById(R.id.radioButtonMale)
        genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if(R.id.radioButtonMale ==checkedId){
                gender=radioButtonMale.text.toString()
            }else{
                gender=radioButtonFemale.text.toString()
            }
        }

        btnSave.setOnClickListener {
            if (ui_height.text.isNullOrBlank() || ui_job.text.isNullOrBlank() || ui_age.text.isNullOrBlank() || ui_weight.text.isNullOrBlank()) {
                Toast.makeText(this, "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show()
            } else {
                val newUserDatas = User()
                newUserDatas.boy = ui_height.text.toString()
                newUserDatas.yas = ui_age.text.toString()
                newUserDatas.kilo = ui_weight.text.toString()
                newUserDatas.meslek = ui_job.text.toString()
                newUserDatas.egzersizDurumu = exerciseStatus
                newUserDatas.cinsiyet = gender
                FirebaseDatabase.getInstance().reference
                    .child("kullanici")
                    .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                    .child("ekstra")
                    .setValue(newUserDatas).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this@UserinformationActivity,
                                "Bilgileri ekleme başarılı",
                                Toast.LENGTH_SHORT
                            ).show()
                            FirebaseDatabase.getInstance().reference
                                .child("kullanici")
                                .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                                .child("ekstra").child("imageURL").removeValue()
                            startActivity(
                                Intent(
                                    this@UserinformationActivity,
                                    UserHomeActivity::class.java
                                )
                            )
                        }
                    }
            }
        }
    }

    private fun fillDatas() {
        val referans = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        val query = referans.child("kullanici")
            .orderByKey()
            .equalTo(user?.uid.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    val oldHeight = singleSnapshot.child("ekstra").child("boy").value
                    val oldAge = singleSnapshot.child("ekstra").child("yas").value
                    val oldWeight = singleSnapshot.child("ekstra").child("kilo").value
                    val oldJob = singleSnapshot.child("ekstra").child("meslek").value
                    val oldExercise = singleSnapshot.child("ekstra").child("egzersizDurumu").value
                    val oldGender = singleSnapshot.child("ekstra").child("cinsiyet").value
                    if (oldHeight != null && oldAge != null && oldExercise != null && oldJob != null && oldGender != null && oldWeight != null) {
                        ui_height.setText(oldHeight.toString())
                        ui_age.setText(oldAge.toString())
                        ui_weight.setText(oldWeight.toString())
                        ui_job.setText(oldJob.toString())
                        if(oldGender=="Kadın"){
                            genderRadioGroup.check(R.id.radioButtonFemale)
                        }else if (oldGender=="Erkek"){
                            genderRadioGroup.check(R.id.radioButtonMale)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("loadPost:onCancelled", error.toException())
            }
        })
    }

    private fun initAuthStateListener() {
        myAuthStateListener = FirebaseAuth.AuthStateListener { p0 ->
            val kullanici = p0.currentUser
            if (kullanici == null) {
                val intent = Intent(this@UserinformationActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }                           // Otomatik giriş sağlayıcı

    override fun onStart() {
        FirebaseAuth.getInstance().addAuthStateListener(myAuthStateListener)
        super.onStart()
    }

    override fun onStop() {
        FirebaseAuth.getInstance().removeAuthStateListener(myAuthStateListener)
        super.onStop()
    }

    // Hesap ayarları menüsü görünmemesi için ayrı bir menü gösteriyoruz.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.ui_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuCikisYap -> {
                cikisYap()
                return true
            }
            R.id.menuSifreDegistir -> {
                mailSifreDegistir()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun mailSifreDegistir() {
        val showDialogFragment= updateAccountFragment()
        showDialogFragment.show(supportFragmentManager,"updateAccountFragment")
    }

    private fun cikisYap() {
        FirebaseAuth.getInstance().signOut()
    }


}