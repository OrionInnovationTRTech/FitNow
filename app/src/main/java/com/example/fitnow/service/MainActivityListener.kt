package com.example.fitnow.service

import java.io.File

interface MainActivityListener {

    fun showOrHide(value:Boolean)
    fun getFilesDirBenim(): File
    fun changeBackBtn()

}