package com.application.stylesync
import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("statusText") val statusText: String,
    @SerializedName("message") val message: String,
    @SerializedName("count") val count: Int,
    @SerializedName("colors") val colors: List<Color>
)

public data class Color(
    @SerializedName("name") val name: String,
    @SerializedName("theme") val theme: String,
    @SerializedName("group") val group: String,
    @SerializedName("hex") val hex: String,
    @SerializedName("rgb") val rgb: String
)
