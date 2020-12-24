package md.merit.casino.data
import android.content.Context
import android.content.SharedPreferences

class SaveData(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("file", Context.MODE_PRIVATE)

    fun setMoney(totalMoney: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("Money", totalMoney)
        editor.apply()
    }

    fun loadMoney(): Int {
        val totalMoney = sharedPreferences.getInt("Money", 1500)
        return (totalMoney)
    }
}