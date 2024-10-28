package com.ikki.dicodingevent

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListEventsItem(

    @SerializedName("summary")
    val summary: String,

    @SerializedName("mediaCover")
    val mediaCover: String,

    @SerializedName("registrants")
    val registrants: Int,

    @SerializedName("imageLogo")
    val imageLogo: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("ownerName")
    val ownerName: String,

    @SerializedName("cityName")
    val cityName: String,

    @SerializedName("quota")
    val quota: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("beginTime")
    val beginTime: String,

    @SerializedName("endTime")
    val endTime: String,

    @SerializedName("category")
    val category: String

) : Parcelable
