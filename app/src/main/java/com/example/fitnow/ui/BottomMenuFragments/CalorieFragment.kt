package com.example.fitnow.ui.BottomMenuFragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnow.FavAdapter
import com.example.fitnow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_calorie.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_profile.*

class CalorieFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calorie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateIndex()
    }

    private fun calculateIndex() {
        var ifComplete=false
        val referans = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        val query = referans.child("kullanici")
            .orderByKey()
            .equalTo(user?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {

                    if(singleSnapshot.child("ekstra").child("kilo").value!=null){
                        ifComplete=true
                    val userWeight = singleSnapshot.child("ekstra").child("kilo").value.toString().toDouble()
                    val userHeight = singleSnapshot.child("ekstra").child("boy").value.toString().toDouble()
                        val index:Double= userWeight/((userHeight*userHeight)/10000)
                        if(index<18.5){
                            healthChecker.text="Zayıfsınız, biraz daha düzgün beslenip kilo almanız sizin için daha güzel olur"
                        }else if(index<25){
                            healthChecker.text="İdeal kilodasınız. Düzgün beslenmeye devam edin, en iyi vücüda sahip olabilirsiniz :)"
                        }else if(index<30){
                            healthChecker.text="İdealin üstü kilodasınız, biraz çaba ile düzgün bir vücuda sahip olabilirsiniz"
                        }else if(index<35){
                            healthChecker.text="Obezlik kilosundasınız, yeme içmenize çok daha fazla dikkat etmeniz gerekmekte! "
                        }else if(index>=35){
                            healthChecker.text="Kötü durumdasınız, tıbbi yardıma muhtaç durumdasınız. Kendinizi üzmeden en yakın sağlık kuruluşundan yardım isteyiniz "
                        }else{
                            healthChecker.text="Düzgün veri girişi yapmanızı rica ederim"
                        }

                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("loadPost:onCancelled", error.toException())
            }
        })


            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if(!ifComplete){
                healthChecker.text="Vücut kitle endeksinizi hesaplamamız için sağ üstteki menüden hesap ayarlarına gidip bilgilerinizi güncellemeniz gerekmektedir."
                }

            }, 200)





    }

}