package com.example.fitnow.service

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fitnow.R


fun ImageView.getImage(url:String,progressDrawable: CircularProgressDrawable){
    val options= RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.nophoto_user)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun progressDrawable(context: Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth=8f
        centerRadius=40f
        start()
    }
}