package com.example.fitnow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnow.ui.BottomMenuFragments.FavoritesFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.onefoodlayout.view.*


class FavAdapter(mFragment:Fragment,itemCounted:Int,itemsName:ArrayList<String>,itemsImage:ArrayList<String>) : RecyclerView.Adapter<FavAdapter.ViewHolder>() {
        val myFragment=mFragment
        val database = FirebaseDatabase.getInstance().reference

        var itemCountFor=itemCounted
        val itemNameArray=itemsName
        val itemImageArray=itemsImage

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.ViewHolder {

            val inflater = LayoutInflater.from(parent.context)
            val oneLineFood = inflater.inflate(R.layout.onefoodlayout, parent, false)
            return ViewHolder(oneLineFood)

        }

        override fun getItemCount(): Int {
            return itemCountFor
        }

        override fun onBindViewHolder(holder: FavAdapter.ViewHolder, position: Int) {
            holder.setData(itemNameArray,itemImageArray,position)

        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var oneLineFood = itemView as CardView
            var foodName = oneLineFood.foodNameText
            var foodImage = oneLineFood.imgFood
            var favBtn = oneLineFood.favCheck

            fun setData(itemsName:ArrayList<String>,itemsImage:ArrayList<String>,position: Int){
                Picasso.get().load(itemsImage[position]).into(foodImage)
                foodName.text=itemsName[position]
                favBtn.isChecked=true
                favBtn.setOnClickListener {
                    if(!favBtn.isChecked){
                        database
                            .child("kullanici")
                            .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                            .child("Favorites")
                            .child(foodName.text.toString())
                            .removeValue()
                        itemNameArray.remove(foodName.text)
                        itemImageArray.remove(oneLineFood.imgFood.toString())
                        (myFragment as FavoritesFragment).recyclerOlustur(--itemCountFor,itemNameArray,itemImageArray)
                    }
                }
            }



        }


}


