package com.example.fitnow

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnow.model.FavoritesItem
import com.example.fitnow.view.bottomMenuFragments.FavoritesFragment
import com.example.fitnow.view.bottomMenuFragments.FavoritesFragmentDirections
import com.example.fitnow.view.bottomMenuFragments.FoodFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.onefoodlayout.view.*


class FavAdapter(mFragment: Fragment,private val favList:ArrayList<FavoritesItem>) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
        val myFragment=mFragment
        private val firebaseDatabase = FirebaseDatabase.getInstance().reference
        inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private var foodName=itemView.foodNameText
            private var foodImage = itemView.imgFood
            private var favBtn = itemView.favCheck
            fun setData(createFoodLineNow: FavoritesItem, position: Int) {

                Picasso.get().load(createFoodLineNow.itemImage).into(foodImage)
                foodName.text=createFoodLineNow.itemName
                favBtn.isChecked=true
                favBtn.setOnClickListener {
                    favList.removeAt(position)
                    firebaseDatabase
                            .child("Users")
                            .child((FirebaseAuth.getInstance().currentUser?.uid).toString())
                            .child("Favorites")
                            .child(foodName.text.toString())
                            .removeValue()
                    println("Silindi updateFavList Çağırılıyor")
                    (myFragment as FavoritesFragment).recyclerOlustur(favList)

                }
               // TODO("view'a info tuşu koy ki kullanıcı tıklayabildiğini anlasın")
            }

        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.FavViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val oneLineFood = inflater.inflate(R.layout.onefoodlayout, parent, false)
            return FavViewHolder(oneLineFood)

        }

        override fun getItemCount(): Int {
            return favList.size
        }

        override fun onBindViewHolder(holder: FavAdapter.FavViewHolder, position: Int) {
            val createFoodLineNow = favList[position] // Oluşturulan nesnenin bilgilerini tek alıp yollama kısmı
            holder.setData(createFoodLineNow,position)

            holder.itemView.setOnClickListener {
                // TODO(geri dönüldüğünde aynı gıdadan recycler yüklenmesini hallet.)
                val foodName=createFoodLineNow.itemName
                val imageUri=createFoodLineNow.itemImage
                val foodContent=createFoodLineNow.itemContent
                val mId= createFoodLineNow.itemId.toInt()
                val action= FavoritesFragmentDirections.actionFavoritesFragmentToOneFoodFragment(foodName,imageUri,foodContent,mId,false)
                Navigation.findNavController(it).navigate(action)
            }
        }



    @SuppressLint("NotifyDataSetChanged")
    fun updateFavList(newFavList: ArrayList<FavoritesItem>){
        for(i in newFavList)
            println(i.itemName)
        favList.clear()
        favList.addAll(newFavList)
        notifyDataSetChanged()

    }


}


