package md.merit.casino.Adapters

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import md.merit.casino.R
import md.merit.casino.data.FirestoreDB
import md.merit.casino.models.CoinModell.Data
import md.merit.casino.ui.Game2Activity
import md.merit.casino.ui.fragments.DisplayCryptoFragment
import md.merit.casino.utils.Common


class PortofolioAdapter(private var items: List<Data>, internal var activity: Activity) :
    RecyclerView.Adapter<PortofolioAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var coinIcon = itemView.coinIcon
        var coinSymbol = itemView.coin_symbol
        var coinName = itemView.coin_name
        var coinPrice = itemView.priceUsd
        var oneHourChange = itemView.oneHour
        var twentyFourChange = itemView.twentyFourHour
        var sevenDayChange = itemView.sevenDays
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.coin_layout, parent, false)
        return MyViewHolder(item)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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

            val bundle = Bundle()
            bundle.putString("rname", gname)
            bundle.putString("rprice", gprice)
            bundle.putString("r1hour", g1hour)
            bundle.putString("r24hour", g24hour)
            bundle.putString("r7day", g7day)
            bundle.putString("rId", gid)
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

    override fun getItemCount(): Int = items.size

    fun updateData(coinModels: List<Data>) {
        this.items = coinModels
        notifyDataSetChanged()
    }
}

