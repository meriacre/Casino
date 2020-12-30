package md.merit.casino.models.CoinModell

import com.google.gson.annotations.SerializedName

data class Coin(
    @SerializedName("data") val `data`: List<Data> ,
    @SerializedName("status") val status: Status?
)