package com.example.fitnow.ui.BottomMenuFragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnow.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var itemCount = 0
        val itemNameArray = ArrayList<String>()
        val itemImageArray = ArrayList<String>()

        val query1 = FirebaseDatabase.getInstance().reference.child("kullanici")
            .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
            .child("Favorites")
            .orderByKey()
        query1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    if (singleSnapshot.value != null) {
                        itemCount++
                        itemNameArray.add(singleSnapshot.child("itemName").value.toString())
                        itemImageArray.add(singleSnapshot.child("itemImage").value.toString())
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if(itemCount==0){
                emptyFav.visibility=View.VISIBLE
            }else{
                emptyFav.visibility=View.INVISIBLE
            }
            val myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            favRecyclerView.layoutManager = myLayoutManager
            val myAdapter = FavAdapter(this,itemCount,itemNameArray,itemImageArray)
            favRecyclerView.adapter = myAdapter

        }, 500)


    }

    fun recyclerOlustur(itemCount:Int,itemNameArray:ArrayList<String>,itemImageArray:ArrayList<String>){
        if(itemCount==0){
            emptyFav.visibility=View.VISIBLE
        }else{
            emptyFav.visibility=View.INVISIBLE
        }
        val myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favRecyclerView.layoutManager = myLayoutManager
        val myAdapter = FavAdapter(this,itemCount,itemNameArray,itemImageArray)
        favRecyclerView.adapter = myAdapter

    }


}