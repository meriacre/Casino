package md.merit.casino.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*
import md.merit.casino.R
import md.merit.casino.data.LoadMore
import md.merit.casino.models.CoinModell.Data
import md.merit.casino.ui.Game2Activity
import md.merit.casino.ui.fragments.BaseFragment
import md.merit.casino.ui.fragments.DisplayCryptoFragment
import md.merit.casino.utils.Common

class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var coinIcon = itemView.coinIcon
    var coinSymbol = itemView.coin_symbol
    var coinName = itemView.coin_name
    var coinPrice = itemView.priceUsd
    var oneHourChange = itemView.oneHour
    var twentyFourChange = itemView.twentyFourHour
    var sevenDayChange = itemView.sevenDays
}

class CoinAdapter(
    recyclerView: RecyclerView,
    internal var activity: Activity,
    var items: List<Data>
) : RecyclerView.Adapter<CoinViewHolder>() {

    internal var loadMore: LoadMore? = null
    var isLoading: Boolean = false
    var visibleThreshold = 5
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }
            }
        })
    }

    fun setLoadMore(loadMore: LoadMore) {
        this.loadMore = loadMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.coin_layout, parent, false)
        return CoinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = items[position]

        holder.coinName.text = coin.name
        holder.coinSymbol.text = coin.symbol
        holder.coinPrice.text = coin.quote.USD.price.toString()
        holder.oneHourChange.text = coin.quote.USD.percent_change_1h + "%"
        holder.twentyFourChange.text = coin.quote.USD.percent_change_24h + "%"
        holder.sevenDayChange.text = coin.quote.USD.percent_change_7d + "%"

        holder.itemView.setOnClickListener {
            val gItem = items[position]
            val gname = gItem.name
            val gprice = gItem.quote.USD.price
            val g1hour = gItem.quote.USD.percent_change_1h + "%"
            val g24hour = gItem.quote.USD.percent_change_24h + "%"
            val g7day = gItem.quote.USD.percent_change_7d + "%"
            val gid = gItem.id.toString()
            val grank = gItem.cmc_rank.toString()
            val gmarketCap = gItem.quote.USD.market_cap
            val gvolume24h = gItem.quote.USD.volume_24h
            val gcircSup = gItem.circulating_supply
            val gmaxSup = gItem.max_supply
            val gtotalSup = gItem.total_supply

            val bundle = Bundle()
            bundle.putString("rname", gname)
            bundle.putString("rprice", gprice)
            bundle.putString("r1hour", g1hour)
            bundle.putString("r24hour", g24hour)
            bundle.putString("r7day", g7day)
            bundle.putString("rId", gid)
            bundle.putString("rRank", grank)
            bundle.putString("rMarketCap", gmarketCap)
            bundle.putString("rVolume24h", gvolume24h)
            bundle.putString("rCircultionSupply", gcircSup)
            bundle.putString("rMaximSupply", gmaxSup)
            bundle.putString("rTotalSupply", gtotalSup)
            val fragmentDisplay = DisplayCryptoFragment()
            fragmentDisplay.arguments = bundle

            val game2 = activity as Game2Activity
            (game2).supportFragmentManager.beginTransaction().apply {
                replace(R.id.crypto_frame, fragmentDisplay)
                addToBackStack("attachBase")
                commit()
            }

        }
        Picasso.with(activity.baseContext)
            .load(
                StringBuilder(Common.imageUrl3)
                    .append(coin.id.toString())
                    .append(".png")
                    .toString()
            )
            .into(holder.coinIcon)

        //Set color
        holder.oneHourChange.setTextColor(
            if (coin.quote.USD.percent_change_1h.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )

        holder.twentyFourChange.setTextColor(
            if (coin.quote.USD.percent_change_24h.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )

        holder.sevenDayChange.setTextColor(
            if (coin.quote.USD.percent_change_7d.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )

    }

    fun setLoaded() {
        isLoading = false
    }

    fun updateData(coinModels: List<Data>) {
        this.items = coinModels
        notifyDataSetChanged()
    }


}