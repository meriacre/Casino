package md.merit.casino.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_game2.*
import md.merit.casino.R
import md.merit.casino.data.SaveData
import md.merit.casino.ui.fragments.BaseFragment
import md.merit.casino.ui.fragments.DisplayCryptoFragment
import md.merit.casino.ui.fragments.SearchFragment
import md.merit.casino.ui.fragments.TradesPortofolioFragment

class Game2Activity : FragmentActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var saveData: SaveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        saveData = SaveData(this)
        auth = FirebaseAuth.getInstance()
        tv_total_money_game2.text = saveData.loadMoney().toString()
        tv_profile.text = auth.currentUser?.email.toString()
        val baseFragment = BaseFragment()
        val portoFragment = TradesPortofolioFragment()
        val searchFragment = SearchFragment()


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.crypto_frame, baseFragment)
            commit()
        }
        base_page.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.crypto_frame, baseFragment)
                commit()
            }
        }
        porto_page.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.crypto_frame, portoFragment)
                commit()
            }
        }

        search_page.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.crypto_frame, searchFragment)
                commit()
            }
        }
    }
}