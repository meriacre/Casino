package md.merit.casino.ui.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.net.HttpHeaders
import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.fragment_base.*
import md.merit.casino.Adapters.CoinAdapter
import md.merit.casino.R
import md.merit.casino.data.LoadMore
import md.merit.casino.models.CoinModell.Coin
import md.merit.casino.models.CoinModell.Data
import md.merit.casino.utils.Common
import okhttp3.*
import java.io.IOException

class BaseFragment : Fragment(R.layout.fragment_base), LoadMore {
    internal var items: MutableList<Data> = ArrayList()
    internal lateinit var adapter: CoinAdapter
    internal lateinit var client: OkHttpClient
    internal lateinit var request: Request
    val apiKey = "425d9abf-fb90-480a-9121-318168d92546"

    override fun onLoadMore() {
        if (items.size <= Common.MAX_COIN_LOAD)
            loadNext10Coin(items.size)
        else
            Toast.makeText(this.activity, "Data max is " + Common.MAX_COIN_LOAD, Toast.LENGTH_SHORT)
                .show()
    }

    private fun loadNext10Coin(index: Int) {
        client = OkHttpClient()
        request = Request.Builder()
            .url(
                String.format(
                    "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=%d&limit=10",
                    index + 1
                )
            )
            .header(HttpHeaders.ACCEPT, "application/json")
            .addHeader("X-CMC_PRO_API_KEY", apiKey)
            .build()

        swipeRefresh.isRefreshing = true // SHow refresh
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("ERROR", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    val gson = GsonBuilder().create()
                    val obiectul = gson.fromJson(body, Coin::class.java)
                    val newItems = obiectul.data
                    activity?.runOnUiThread {
                        items.addAll(newItems)
                        adapter.setLoaded()
                        adapter.updateData(items)

                        swipeRefresh.isRefreshing = false
                    }
                }
            })

    }

    private fun loadFrist10Coin() {
        client = OkHttpClient()
        request = Request.Builder()
            .url(String.format("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=10"))
            .header(HttpHeaders.ACCEPT, "application/json")
            .addHeader("X-CMC_PRO_API_KEY", apiKey)
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("ERROR", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    val gson = GsonBuilder().create()
                    val obiectul = gson.fromJson(body, Coin::class.java)
                    items = obiectul.data as MutableList<Data>
                    activity?.runOnUiThread {
                        adapter.updateData(items)


                    }
                }
            })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefresh.post { loadFrist10Coin() }

        swipeRefresh.setOnRefreshListener {
            items.clear() // Remove all item
            loadFrist10Coin()
            setUpAdapter()
        }
        rv_coins.layoutManager = LinearLayoutManager(this.context)
        setUpAdapter()

    }

    private fun setUpAdapter() {
        adapter = CoinAdapter(rv_coins, this.requireActivity(), items)
        rv_coins.adapter = adapter
        adapter.setLoadMore(this)
    }


}