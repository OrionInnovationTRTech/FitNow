package com.example.fitnow.data.entity

class FavoritesItem {
    var itemId: String? = null
    var itemName: String? = null
    var itemImage: String? = null

    constructor() {}
    constructor(itemId: String?,itemName: String?,itemImage:String?){
        this.itemId=itemId
        this.itemName=itemName
        this.itemImage=itemImage
    }


}