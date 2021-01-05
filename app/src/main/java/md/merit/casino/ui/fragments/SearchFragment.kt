package md.merit.casino.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_portofolio.*
import kotlinx.android.synthetic.main.fragment_search.*
import md.merit.casino.Adapters.PortofolioAdapter
import md.merit.casino.R
import md.merit.casino.data.FirestoreDB
import md.merit.casino.models.CoinModell.Coin
import md.merit.casino.models.CoinModell.Data
import okhttp3.*
import java.io.IOException

class SearchFragment : Fragment(R.layout.fragment_search) {

    internal lateinit var adapter: PortofolioAdapter
    var listaNoua: MutableList<Data> = ArrayList()
    lateinit var names: String
    lateinit var symbol: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_search.setOnClickListener {
            names = edt_search.text.toString()
            symbol = edt_search.text.toString()

            fetchJson()
            rv_search.layoutManager = LinearLayoutManager(this.context)
            adapter = PortofolioAdapter(listaNoua, this.requireActivity())
            rv_search.adapter = adapter
        }

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
                names = names.capitalize()
                symbol = symbol.toUpperCase()
                listaNoua = lista.filter { names.contains(it.name) } as MutableList<Data>
                listaNoua.plusAssign((lista.filter { symbol.contains(it.symbol) } as MutableList<Data>))
                activity?.runOnUiThread {
                    adapter.updateData(listaNoua)
                }
            }
        })
    }
}