package md.merit.casino.data

import android.content.Context
import android.content.SharedPreferences

class SaveData(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setMoney(totalMoney: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Money", totalMoney)
        editor.apply()
    }

    fun loadMoney(): String? {
        val totalMoney = sharedPreferences.getString("Money", "10000")
        return (totalMoney)
    }

}