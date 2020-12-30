package md.merit.casino.models.CoinModell

import com.google.gson.annotations.SerializedName

data class USD(
    @SerializedName("last_updated") val last_updated: String,
    @SerializedName("market_cap") val market_cap: String,
    @SerializedName("percent_change_1h") val percent_change_1h: String,
    @SerializedName("percent_change_24h") val percent_change_24h: String,
    @SerializedName("percent_change_7d") val percent_change_7d: String,
    @SerializedName("price") val price: String,
    @SerializedName("volume_24h") val volume_24h: String
)