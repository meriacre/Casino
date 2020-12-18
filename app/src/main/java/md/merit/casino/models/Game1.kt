package md.merit.casino.models

import android.app.Activity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import md.merit.casino.R
import kotlin.random.Random.Default.nextInt

class Game1(
    val slot1:Int = -1,
    val slot2:Int = -1,
    val slot3:Int = -1
) {
     fun spinIt(context: Activity, img1:ImageView, img2:ImageView, img3:ImageView):Game1{
        var slot1 = nextInt(1,7)
        var slot2 = nextInt(1,7)
        var slot3 = nextInt(1,7)
        when(slot1){
            1 -> img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig1))
            2 -> img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig2))
            3 -> img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig3))
            4 -> img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig4))
            5 -> img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig5))
            else -> img1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig6))
        }

        when(slot2){
            1 -> img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig1))
            2 -> img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig2))
            3 -> img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig3))
            4 -> img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig4))
            5 -> img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig5))
            6 -> img2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig6))
        }

        when(slot3){
            1 -> img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig1))
            2 -> img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig2))
            3 -> img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig3))
            4 -> img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig4))
            5 -> img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig5))
            6 -> img3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fig6))
        }

return Game1(slot1, slot2, slot3)
    }

     fun sumIt(total:TextView, bidul:TextView, obj:Game1){
         var totalMoney = total.text.toString().toInt()
         var bid = bidul.text.toString().toInt()
         totalMoney -= bid
         total.text = totalMoney.toString()
         if (obj.slot1 == 1 && obj.slot2 == 1 && obj.slot3 == 1){
             totalMoney += (bid * 30)
         }
         if (obj.slot1 != 1 && obj.slot2 == obj.slot1 && obj.slot3 == obj.slot1){
             totalMoney += (bid * 10)
         }
         if ((obj.slot1 == 1 && obj.slot2 == 1 && obj.slot3 != 1)
             || (obj.slot1 != 1 && obj.slot2 == 1 && obj.slot3 == 1)
             || (obj.slot1 == 1 && obj.slot2 != 1 && obj.slot3 == 1)){
             totalMoney += (bid * 4)
         }
         if ((obj.slot1 == 1 && obj.slot2 != 1 && obj.slot3 != 1)
             || (obj.slot1 != 1 && obj.slot2 != 1 && obj.slot3 == 1)
             || (obj.slot1 != 1 && obj.slot2 == 1 && obj.slot3 != 1)){
             totalMoney += bid
         }

         total.text = totalMoney.toString()
     }

}