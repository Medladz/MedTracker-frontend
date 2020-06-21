package com.example.medtracker

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

class CalenderClass : ArrayList<CalenderClassItem>()

data class CalenderClassItem(
    val consumedAt: ConsumedAt,
    val container: Container,
    val creator: Any,
    val drug: Drug,
    val id: Int,
    val measurementUnit: String,
    val note: Any,
    val quantity: Int,
    val title: String
)

data class ConsumedAt(
    val afterNow: Boolean,
    val beforeNow: Boolean,
    val centuryOfEra: Int,
    val chronology: Chronology,
    val dayOfMonth: Int,
    val dayOfWeek: Int,
    val dayOfYear: Int,
    val equalNow: Boolean,
    val era: Int,
    val hourOfDay: Int,
    val millis: Long,
    val millisOfDay: Int,
    val millisOfSecond: Int,
    val minuteOfDay: Int,
    val minuteOfHour: Int,
    val monthOfYear: Int,
    val secondOfDay: Int,
    val secondOfMinute: Int,
    val weekOfWeekyear: Int,
    val weekyear: Int,
    val year: Int,
    val yearOfCentury: Int,
    val yearOfEra: Int,
    val zone: ZoneX
)

data class Container(
    val creatorID: Any,
    val id: Any,
    val measurementUnit: Any,
    val name: Any,
    val quantity: Any,
    val quantityWithUnit: Any,
    val thumbnailURL: Any
)

data class Drug(
    val brand: Brand,
    val componentOf: Any,
    val components: List<Any>,
    val containers: List<Any>,
    val creator: Any,
    val id: Int,
    val measurementUnit: Any,
    val name: String,
    val purity: Any,
    val purityPercentage: String,
    val quantity: Any,
    val quantityWithUnit: Any,
    val source: Source,
    val thumbnailURL: String
)

data class Chronology(
    val zone: Zone
)

data class ZoneX(
    val fixed: Boolean,
    val id: String
)

data class Zone(
    val fixed: Boolean,
    val id: String
)

data class Brand(
    val creator: Any,
    val id: Int,
    val name: Any
)

data class Source(
    val creator: Any,
    val id: Int,
    val name: Any
)

//User Deserializer
class CalenderDeserializer : ResponseDeserializable<CalenderClass> {
    override fun deserialize(content: String) = Gson().fromJson(content, CalenderClass::class.java)
}