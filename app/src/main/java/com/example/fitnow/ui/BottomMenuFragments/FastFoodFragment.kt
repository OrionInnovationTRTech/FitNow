package com.example.fitnow.ui.BottomMenuFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnow.data.api.ApiInterface
import com.example.fitnow.MealAdapter
import com.example.fitnow.data.entity.MealData
import com.example.fitnow.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_fast_food.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FastFoodFragment : Fragment() {
    var myAdapter: MealAdapter?=null
    lateinit var queryListener:List<MealData.SearchResults>
    lateinit var apiInterface: ApiInterface
    val API_KEY = "3f09afdb73bf4850a5c7a09e32ec7994"

    override fun onCreate(savedInstanceState: Bundle?) {    // Fragment oluşturulduğunda Retrofit nesnesi oluşturuluyor.
        super.onCreate(savedInstanceState)
        val BASE_URL = "https://api.spoonacular.com/food/"

        /*
        Retrofit nesnesi oluşturmak için bir interface, bir base url ve veri sınıfına ihtiyacımız var.
        Verileri çekeceğim sınıfı MealData.kt dosyası olarak ayarladım.
        Ayrıca JSON Dosyasını çevirmek DTO çevirmek için Retrofit kütüphanesinin yardımını aldım.
         */
        apiInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)

        val apiCall = apiInterface.getFoodDetails("apple",API_KEY,5)
        // Yukarıda çağrımızı oluşturduk, bu çağrıyı işleme almak için enqueue işlemi uyguluyoruz.
        startApiCall(apiCall)
    }

    private fun startApiCall(apiCall: Call<MealData>) {
        // Çağrı başarılı olduğunda onResponse durumu başarısız çağrı olduğunda Failure kısmı çalışıyor.
        apiCall.enqueue(object : Callback<MealData?> {
            override fun onResponse(call: Call<MealData?>, response: Response<MealData?>) {
                // Bu kısıma gelindiğinde veriler başarılı şekilde çekiliyor, response içerisinde çağrı sonucu dönen veriler bulunuyor.
                queryListener= response.body()?.searchResults!!
                //Aşağıda Recyclerview için hazırlıklar yapılıyor.
                val myLayoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                recyclerviewFood.layoutManager=myLayoutManager
                //Üstte recycler viewin dizaynını belirleyip bildiriyoruz.
                myAdapter =MealAdapter(queryListener[0].results)
                recyclerviewFood.adapter=myAdapter
                // recycyler view içeriğini düzenlemek için adapterimize verileri yolluyoruz.


            }

            override fun onFailure(call: Call<MealData?>, t: Throwable) {
                Log.e("Failed", t.stackTraceToString())
            }

        })

    }



    // onCreateView içerisinde üstteki menünün tasarımı oluşturuluyor.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_fast_food, container, false)
        val appleImage=v.findViewById<ImageView>(R.id.menuAppleImage)
        val bananaImage=v.findViewById<ImageView>(R.id.menuBananaImage)
        val watermelonImage=v.findViewById<ImageView>(R.id.menuWatermelonImage)
        val lemonImage=v.findViewById<ImageView>(R.id.menuLemonImage)
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/apple.jpg").into(appleImage)
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/bananas.jpg").into(bananaImage)
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/watermelon.png").into(watermelonImage)
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/lemon.png").into(lemonImage)
        return v
    }


    // OnviewCreated içerisinde üstteki tıklanabilir menü oluşturuluyor. Her menü için ayrı interface çağrısı sağlanıyor.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuAppleImage.setOnClickListener{
            val apiCall= apiInterface.getFoodDetails("apple",API_KEY,5)
            startApiCall(apiCall)
        }
        menuBananaImage.setOnClickListener {
            val apiCall= apiInterface.getFoodDetails("banana",API_KEY,5)
            startApiCall(apiCall)
        }
        menuWatermelonImage.setOnClickListener {
            val apiCall= apiInterface.getFoodDetails("watermelon",API_KEY,5)
            startApiCall(apiCall)
        }
        menuLemonImage.setOnClickListener {
            val apiCall= apiInterface.getFoodDetails("lemon",API_KEY,5)
            startApiCall(apiCall)
        }
    }


}