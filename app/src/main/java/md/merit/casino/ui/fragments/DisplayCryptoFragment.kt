package md.merit.casino.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game2.*
import kotlinx.android.synthetic.main.fragment_display_crypto.*
import md.merit.casino.R
import md.merit.casino.data.FirestoreDB
import md.merit.casino.data.SaveData
import md.merit.casino.models.Trade
import md.merit.casino.utils.Common

class DisplayCryptoFragment : Fragment(R.layout.fragment_display_crypto) {

    var fire: FirestoreDB = FirestoreDB()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val name = arguments?.getString("rname")
        val price = arguments?.getString("rprice")
        val hour1 = arguments?.getString("r1hour")
        val hour24 = arguments?.getString("r24hour")
        val day7 = arguments?.getString("r7day")
        val id = arguments?.getString("rId")
        val rank = arguments?.getString("rRank")
        val markCap = arguments?.getString("rMarketCap")
        val volume24h = arguments?.getString("rVolume24h")
        val circSupply = arguments?.getString("rCircultionSupply")
        val maxSupply = arguments?.getString("rMaximSupply")
        val totalSupply = arguments?.getString("rTotalSupply")

        this.activity?.let { fire.getSharesNumber(it, name!!, tv_coin_shares) }
        tv_name_display.text = name
        tv_price_display.text = price
        tv_1h_display.text = hour1
        tv_cmc_rank.text = rank
        tv_markey_cap.text = markCap
        tv_volume_24h.text = volume24h
        tv_circ_supply.text = circSupply
        tv_max_supply.text = maxSupply
        tv_total_supply.text = totalSupply
        tv_1h_display.setTextColor(
            if (hour1!!.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )
        tv_24h.text = hour24
        tv_24h.setTextColor(
            if (hour24!!.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )
        tv_7day.text = day7
        tv_7day.setTextColor(
            if (day7!!.contains("-"))
                Color.parseColor("#FF0000")
            else
                Color.parseColor("#32CD32")
        )

        Picasso.with(activity?.baseContext)
            .load(
                StringBuilder(Common.imageUrl3)
                    .append(id)
                    .append(".png")
                    .toString()
            )
            .into(img_logo)

        btn_buy.setOnClickListener {
            val inflater = LayoutInflater.from(this.activity)
            val view = inflater.inflate(R.layout.buy_layout, null)
            val pricePerShare = view.findViewById<TextView>(R.id.tv_price_share)
            val shares = view.findViewById<TextView>(R.id.edt_shares)
            pricePerShare.text = price


            val alertDialog = AlertDialog.Builder(this.activity)
                .setTitle("Buy $name")
                .setMessage("How many $name do you wanna buy?")
                .setView((view))
                .setPositiveButton("Buy", DialogInterface.OnClickListener { dialogInterface, i ->
                    if (shares.text.isNotEmpty() && shares.text.toString().toInt() > 0) {
                        val pret = price?.toDoubleOrNull()?.times(shares.text.toString().toDouble())
                        val alert2 = AlertDialog.Builder(this.activity)
                            .setTitle("Total")
                            .setMessage("Are you sure you want to buy ${shares.text} $name for total price of $pret?")
                            .setPositiveButton(
                                "Yes",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    val trade = Trade(name!!, shares.text.toString().toInt())
                                    this.activity?.let { it1 -> fire.saveTrade(it1, name, trade) }
                                    val saveData = context?.let { it1 -> SaveData(it1) }
                                    var totalMoney = saveData?.loadMoney()?.toDouble()
                                    totalMoney = totalMoney!! - pret!!
                                    saveData?.setMoney(totalMoney.toString())
                                    val tv =
                                        getActivity()?.findViewById<TextView>(R.id.tv_total_money_game2)
                                    tv?.text = saveData?.loadMoney()
                                })
                            .setNegativeButton(
                                "No",
                                DialogInterface.OnClickListener { dialogInterface, i ->

                                })
                            .create()
                            .show()
                    } else {
                        Toast.makeText(this.activity, "You can't buy 0 shares!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

                })
            val alert = alertDialog.create()
            alert.show()
        }

        btn_sell.setOnClickListener {
            val inflater = LayoutInflater.from(this.activity)
            val view = inflater.inflate(R.layout.buy_layout, null)
            val pricePerShare = view.findViewById<TextView>(R.id.tv_price_share)
            val shares = view.findViewById<TextView>(R.id.edt_shares)
            pricePerShare.text = price

            if (tv_coin_shares.text.toString().toInt() > 0) {

                val alertDialog = AlertDialog.Builder(this.activity)
                    .setTitle("Buy $name")
                    .setMessage("How many $name do you wanna sell?")
                    .setView((view))
                    .setPositiveButton(
                        "Sell",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            if (shares.text.isNotEmpty() && shares.text.toString().toInt() > 0) {
                                val pret = price?.toDoubleOrNull()
                                    ?.times(shares.text.toString().toDouble())
                                val alert2 = AlertDialog.Builder(this.activity)
                                    .setTitle("Total")
                                    .setMessage("Are you sure you want to sell ${shares.text} $name for total price of $pret?")
                                    .setPositiveButton(
                                        "Yes",
                                        DialogInterface.OnClickListener { dialogInterface, i ->
                                            val trade =
                                                Trade(name!!, shares.text.toString().toInt())
                                            this.activity?.let { it1 ->
                                                fire.sellTrade(
                                                    it1,
                                                    name,
                                                    trade
                                                )
                                            }
                                            val saveData = context?.let { it1 -> SaveData(it1) }
                                            var totalMoney = saveData?.loadMoney()?.toDouble()
                                            totalMoney = totalMoney!! + pret!!
                                            saveData?.setMoney(totalMoney.toString())
                                            val tv =
                                                getActivity()?.findViewById<TextView>(R.id.tv_total_money_game2)
                                            tv?.text = saveData?.loadMoney()
                                        })
                                    .setNegativeButton(
                                        "No",
                                        DialogInterface.OnClickListener { dialogInterface, i ->

                                        })
                                    .create()
                                    .show()
                            } else {
                                Toast.makeText(
                                    this.activity,
                                    "You can't sell 0 shares!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    .setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                val alert = alertDialog.create()
                alert.show()
            } else {
                Toast.makeText(this.activity, "You don't have shares to sell!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}