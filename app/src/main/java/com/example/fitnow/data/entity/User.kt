package com.example.fitnow.data.entity

class User {
    var kullanici_id: String? = null
    var isim: String? = null
    var username: String? = null
    var email: String? = null
    var password: String? = null

    var boy: String? = null
    var yas: String? = null
    var kilo:String? = null
    var meslek: String? = null
    var egzersizDurumu: String? = null
    var cinsiyet: String? = null
    var imageURL:String = "https://firebasestorage.googleapis.com/v0/b/fitnow-7c41f.appspot.com/o/nophoto_user.png?alt=media&token=6bb0c0d8-2820-40b4-9819-a3bd03b06ce1"


    constructor() {}
    constructor(
        kullanici_id: String?,
        isim: String?,
        username: String?,
        email: String?,
        password: String?,
        boy:String?,
        yas:String?,
        meslek:String?,
        egzersizDurumu:String?,
        cinsiyet:String?,
    ) {
        this.kullanici_id = kullanici_id
        this.isim = isim
        this.username = username
        this.email = email
        this.password = password
        this.boy = boy
        this.yas = yas
        this.meslek = meslek
        this.egzersizDurumu = egzersizDurumu
        this.cinsiyet = cinsiyet
    }

}