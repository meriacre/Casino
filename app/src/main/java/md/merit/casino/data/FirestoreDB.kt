package md.merit.casino.data

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import md.merit.casino.models.Game1
import md.merit.casino.models.Money
import md.merit.casino.models.Trade
import java.lang.StringBuilder

class FirestoreDB() {
    val authUser = FirebaseAuth.getInstance().currentUser?.email
    private val slotsCollectionRef = Firebase.firestore.collection("$authUser")
    private val profileCollectionRef = Firebase.firestore.collection("$authUser-money")
    private val tradesCollectionRef = Firebase.firestore.collection("$authUser-Trades")

    fun saveSlots(context: Context, game1: Game1) = CoroutineScope(Dispatchers.IO).launch {
        try {
            slotsCollectionRef.add(game1).await()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveMoney(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val saveData = SaveData(context)
            val money = saveData.loadMoney()?.let { Money(it) }
            if (money != null) {
                profileCollectionRef.document("money").set(money).await()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveTrade(context: Context, name: String, trade: Trade) =
        CoroutineScope(Dispatchers.IO).launch {
            val db = Firebase.firestore
            var trade1: Trade
            val sb = StringBuilder()
            val docRef = db.collection("$authUser-Trades").document(name)
            try {
                docRef.get().addOnSuccessListener {
                    if (it.exists()) {
                        trade1 = it.toObject(Trade::class.java)!!
                        sb.append(trade1.shares)
                        trade1.shares = sb.toString().toInt() + trade.shares
                        tradesCollectionRef.document(name).set(trade1)
                        Log.d("sadfsfasfa", "DocumentSnapshot data: ${sb}")
                    } else {
                        tradesCollectionRef.document(name).set(trade)
                    }
                }.await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "The trade was added successfully", Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    fun sellTrade(context: Context, name: String, trade: Trade) =
        CoroutineScope(Dispatchers.IO).launch {
            val db = Firebase.firestore
            var trade1: Trade
            val sb = StringBuilder()
            val docRef = db.collection("$authUser-Trades").document(name)
            try {
                docRef.get().addOnSuccessListener {
                    if (it.exists()) {
                        trade1 = it.toObject(Trade::class.java)!!
                        sb.append(trade1.shares)
                        trade1.shares = sb.toString().toInt() - trade.shares
                        tradesCollectionRef.document(name).set(trade1)
                        Log.d("sadfsfasfa", "DocumentSnapshot data: ${sb}")
                    } else {
                        tradesCollectionRef.document(name).set(trade)
                    }
                }.await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "The trade was added successfully", Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    fun getTrades(context: Context): MutableList<String> {
        val lista: MutableList<String> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                tradesCollectionRef.get()
                    .addOnSuccessListener {
                        for (doc in it) {
                            if (doc.getDouble("shares")!! > 0)
                                lista.add(doc.id)
                        }
                    }.await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "$e", Toast.LENGTH_LONG).show()
                }
            }
        }
        return lista
    }

    fun getSharesNumber(context: Context, name: String, tv: TextView) {
        var trade1: Trade
        var sharesNumber: Int
        tradesCollectionRef.document(name)
            .addSnapshotListener { value, error ->
                error?.let {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                value?.let {
                    if (it.exists()) {
                        trade1 = it.toObject(Trade::class.java)!!
                        sharesNumber = trade1.shares
                        tv.text = sharesNumber.toString()
                    } else {
                        tv.text = 0.toString()
                    }
                }
            }
    }

}