package md.merit.casino.models.CoinModell

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("credit_count") val credit_count: Int,
    @SerializedName("elapsed") val elapsed: Int,
    @SerializedName("error_code") val error_code: Int,
    @SerializedName("error_message") val error_message: Any,
    @SerializedName("notice") val notice: Any,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("total_count") val total_count: Int
)