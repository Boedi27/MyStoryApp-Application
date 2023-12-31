package com.example.mystoryapp.data.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class GetAllStoriesResponse(
    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem>? = null,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

@Parcelize
@Entity(tableName = "story")
data class ListStoryItem(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("lat")
    val lat: Double? = null,

    @field:SerializedName("lon")
    val lon: Double? = null

) : Parcelable
