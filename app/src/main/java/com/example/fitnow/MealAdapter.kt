package com.example.fitnow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnow.data.entity.FavoritesItem
import com.example.fitnow.data.entity.MealData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.onefoodlayout.view.*


class MealAdapter(mealInfo: List<MealData.Results>?) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {
    var database = FirebaseDatabase.getInstance().reference
    var allFoods = mealInfo // gelen verileri initialize edip allFoods içerisine aktarıyorum.

    override fun getItemCount(): Int {
        return allFoods!!.size
    }   // Gelen verilerin sayısını bildiriyorum.

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val createFoodLineNow = allFoods?.get(position) // Oluşturulan nesnenin bilgilerini tek alıp yollama kısmı
        holder.setData(createFoodLineNow, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val oneLineFood = inflater.inflate(R.layout.onefoodlayout, parent, false)
        return MealViewHolder(oneLineFood)
        //Recycler viewi oluştururken her bir itemin hangi xml dosyası formatında olacağını bildirdim.
    }


    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var oneLineFood = itemView as CardView      // itemView'in CardView özelliklerini kullanmasını sağladım
        var foodName = oneLineFood.foodNameText     // itemview'i initialize ettim
        var foodImage = oneLineFood.imgFood
        var favBtn = oneLineFood.favCheck

        // burada Gelen verilerin card içerisindeki yerlerine doldurdum.
        fun setData(creatingFoodLineNow: MealData.Results?, position: Int) {

            val query = database.child("kullanici")
                .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                .child("Favorites")
                .orderByKey()
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (singleSnapshot in snapshot.children) {
                        if (singleSnapshot.child("itemId").value == creatingFoodLineNow?.mId.toString()) {
                            favBtn.isChecked = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


            foodName.text = creatingFoodLineNow?.resultName
            favBtn.setOnClickListener {

                if (favBtn.isChecked) {
                    val newFavoriteItem = FavoritesItem()
                    newFavoriteItem.itemId=creatingFoodLineNow?.mId.toString()
                    newFavoriteItem.itemName=creatingFoodLineNow?.resultName
                    newFavoriteItem.itemImage=creatingFoodLineNow?.image
                    database
                        .child("kullanici")
                        .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                        .child("Favorites")
                        .child(foodName.text.toString())
                        .setValue(newFavoriteItem)

                } else {
                    database
                        .child("kullanici")
                        .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                        .child("Favorites")
                        .child(foodName.text.toString())
                        .removeValue()
                }

            }

            Picasso.get().load(creatingFoodLineNow?.image).into(foodImage)
        }


    }


}