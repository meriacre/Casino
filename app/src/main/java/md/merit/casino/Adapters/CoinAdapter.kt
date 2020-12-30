package md.merit.casino.Adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*
import md.merit.casino.R
import md.merit.casino.data.LoadMore
import md.merit.casino.models.CoinModell.Data
import md.merit.casino.utils.Common

class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var coinIcon = itemView.coinIcon
    var coinSymbol = itemView.coin_symbol
    var coinName = itemView.coin_name
    var coinPrice = itemView.priceUsd
    var oneHourChange = itemView.oneHour
    var twentyFourChange = itemView.twentyFourHour
    var sevenDayChange = itemView.sevenDays
}

class CoinAdapter(recyclerView: RecyclerView, internal var activity: Activity, var items: List<Data>) : RecyclerView.Adapter<CoinViewHolder>(){
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
        holder.coinPrice.text = coin.quote.USD.price
        holder.oneHourChange.text = coin.quote.USD.percent_change_1h + "%"
        holder.twentyFourChange.text = coin.quote.USD.percent_change_24h + "%"
        holder.sevenDayChange.text = coin.quote.USD.percent_change_7d + "%"

        Picasso.with(activity.baseContext)
            .load(StringBuilder(Common.imageUrl)
                .append(coin.symbol.toLowerCase())
                .append(".png")
                .toString())
            .into(holder.coinIcon)

        //Set color
        holder.oneHourChange.setTextColor(if (coin.quote.USD.percent_change_1h.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

        holder.twentyFourChange.setTextColor(if (coin.quote.USD.percent_change_24h.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

        holder.sevenDayChange.setTextColor(if (coin.quote.USD.percent_change_7d.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

    }

    fun  setLoaded(){
        isLoading = false
    }

    fun updateData(coinModels: List<Data>)
    {
        this.items = coinModels
        notifyDataSetChanged()
    }





}