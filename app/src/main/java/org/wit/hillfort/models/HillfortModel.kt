package org.wit.hillfort.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class HillfortModel(@PrimaryKey(autoGenerate = true)var id:Long = 0,
                         var title: String = "",
                         var description: String = "",
                         @Embedded var images: ArrayList<String> = ArrayList(),
                         @Embedded var location: Location = Location(),
                         var additionalNotes: String = "",
                         var visitedCheckbox: Boolean = false,
                         var day: Int = 1,
                         var month: Int = 1,
                         var year: Int = 2018,
                         var favorite: Boolean = false,
                         var rating: Float = 0.0f):Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable