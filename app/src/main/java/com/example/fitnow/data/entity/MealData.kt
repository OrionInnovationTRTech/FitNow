package com.example.fitnow.data.entity

import com.google.gson.annotations.SerializedName


class MealData {

    @SerializedName("query")
    var query: String? = null
    @SerializedName("totalResults")
    var totalResults: Int? = null
    @SerializedName("limit")
    var limit: Int? = null
    @SerializedName("searchResults")
    var searchResults: List<SearchResults>? = null



    class SearchResults {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("results")
        var results: List<Results>? = null
    }

        class Results {
            @SerializedName("id")
            var mId: Int? = null
            @SerializedName("name")
            var resultName: String? = null
            @SerializedName("image")
            var image: String? = null
            @SerializedName("link")
            var link: String? = null
            @SerializedName("content")
            var content: String? = null
        }

    }

