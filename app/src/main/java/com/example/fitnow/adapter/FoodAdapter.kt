package com.example.fitnow.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnow.R
import com.example.fitnow.data.entity.MealData
import com.example.fitnow.model.FavoritesItem
import com.example.fitnow.view.bottomMenuFragments.FoodFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.onefoodlayout.view.*


class FoodAdapter(private val foodList: ArrayList<MealData.Results>): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    val database = FirebaseDatabase.getInstance().reference

    inner class FoodViewHolder(var view:View): RecyclerView.ViewHolder(view) {
        var foodName=view.foodNameText
        var foodImage = view.imgFood
        var favBtn = view.favCheck
        fun setData(createFoodLineNow: MealData.Results, position: Int) {
            checkFav(createFoodLineNow)
            foodName.text= createFoodLineNow.resultName
            Picasso.get().load(createFoodLineNow.image).into(foodImage)
            favBtn.setOnClickListener {
                if (favBtn.isChecked) {
                    val itemId=createFoodLineNow.mId.toString()
                    val itemName=createFoodLineNow.resultName.toString()
                    val itemImage=createFoodLineNow.image.toString()
                    val newFavoriteItem = FavoritesItem(itemId,itemName,itemImage)
                    database
                        .child("Users")
                        .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                        .child("Favorites")
                        .child(foodName.text.toString())
                        .setValue(newFavoriteItem)

                } else {
                    database
                        .child("Users")
                        .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                        .child("Favorites")
                        .child(foodName.text.toString())
                        .removeValue()
                }

            }
        }

        private fun checkFav(createFoodLineNow: MealData.Results) {
            favBtn.isChecked=false
            val query = database.child("Users")
                .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                .child("Favorites")
                .orderByKey()
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        if (singleSnapshot.child("itemId").value == createFoodLineNow.mId.toString()) {
                            favBtn.isChecked = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val viewCreate= inflater.inflate(R.layout.onefoodlayout,parent,false)
        return FoodViewHolder(viewCreate)
    }
    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val createFoodLineNow = foodList[position] // Oluşturulan nesnenin bilgilerini tek alıp yollama kısmı
        holder.setData(createFoodLineNow,position)

        holder.view.setOnClickListener {
            val foodName=createFoodLineNow.resultName.toString()
            val imageUri=createFoodLineNow.image.toString()
            val foodContent=createFoodLineNow.content.toString()
            val mId=createFoodLineNow.mId
            val action=FoodFragmentDirections.actionFoodFragmentToOneFoodFragment(foodName,imageUri,foodContent, mId!!)
            Navigation.findNavController(it).navigate(action)
        }

    }

    fun updateFoodList(newFoodList: List<MealData.Results>){
        foodList.clear()
        foodList.addAll(newFoodList)
        notifyDataSetChanged()
    }


}