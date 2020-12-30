package md.merit.casino.models.CoinModell

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("circulating_supply") val circulating_supply: String,
    @SerializedName("cmc_rank") val cmc_rank: Int,
    @SerializedName("date_added") val date_added: String,
    @SerializedName("id") val id: Int,
    @SerializedName("last_updated") val last_updated: String,
    @SerializedName("max_supply") val max_supply: String,
    @SerializedName("name") val name: String,
    @SerializedName("num_market_pairs") val num_market_pairs: Int,
    @SerializedName("platform") val platform: Any,
    @SerializedName("quote") val quote: Quote,
    @SerializedName("slug") val slug: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("total_supply") val total_supply: String
)