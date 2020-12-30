package md.merit.casino.models.CoinModell

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("USD") val USD: USD
)