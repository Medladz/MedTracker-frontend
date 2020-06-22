package com.example.medtracker

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class DrugFeed(
    val data: ArrayList<Drugdata>
)

data class Drugdata (
    val id: Int,
    val type: String,
    val attributes: Attributes
)

data class Attributes (
    val name: String,
    val thumbnailURL:String
)

    //User Deserializer
    class Deserializer : ResponseDeserializable<DrugFeed> {
        override fun deserialize(content: String) = Gson().fromJson(content, DrugFeed::class.java)
    }

