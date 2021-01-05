package md.merit.casino.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_portofolio.*
import md.merit.casino.Adapters.PortofolioAdapter
import md.merit.casino.R
import md.merit.casino.data.FirestoreDB
import md.merit.casino.models.CoinModell.Coin
import md.merit.casino.models.CoinModell.Data
import okhttp3.*
import java.io.IOException

class TradesPortofolioFragment : Fragment(R.layout.fragment_portofolio) {

    internal lateinit var adapter: PortofolioAdapter
    var listaNoua: MutableList<Data> = ArrayList()
    val fire: FirestoreDB = FirestoreDB()
    var names: MutableList<String> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        names = context?.let { fire.getTrades(it) }!!
        fetchJson()
        if (listaNoua.isEmpty()) {
            Toast.makeText(this.activity, "You dont have any trades yet..", Toast.LENGTH_LONG)
                .show()
        }
        rv_portofolio_coins.layoutManager = LinearLayoutManager(this.context)
        adapter = PortofolioAdapter(listaNoua, this.requireActivity())
        rv_portofolio_coins.adapter = adapter
    }

    fun fetchJson() {
        val url =
            "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=425d9abf-fb90-480a-9121-318168d92546"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("request", "Nu o mers")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val obiectulMeu = gson.fromJson(body, Coin::class.java)
                val lista = obiectulMeu.data
                listaNoua = lista.filter { names.contains(it.name) } as MutableList<Data>
                activity?.runOnUiThread {
                    adapter.updateData(listaNoua)
                }
            }
        })
    }
}