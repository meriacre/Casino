package md.merit.casino.data

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import md.merit.casino.models.Money
import md.merit.casino.ui.MainActivity
import md.merit.casino.utils.Utils

class LogUser {

    lateinit var auth: FirebaseAuth

    fun loginUser(context: Context, login: EditText, password: EditText) {
        val email = login.text.toString()
        val pass = password.text.toString()
        auth = FirebaseAuth.getInstance()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    auth.signInWithEmailAndPassword(email, pass).await()

                    val db = Firebase.firestore
                    var money: Money
                    val authUser = auth.currentUser?.email
                    val sb = StringBuilder()
                    val docRef = db.collection("$authUser-money").document("money")
                    docRef.get().addOnSuccessListener {
                        money = it.toObject<Money>()!!
                        sb.append("${money.money}")
                    }.await()
                    val saveData = SaveData(context)
                    saveData.setMoney(sb.toString())
                    withContext(Dispatchers.Main) {
                       // Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show()
                        checkLogedinState(context)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "Please complete all empty inputs!", Toast.LENGTH_SHORT).show()
        }
    }

    fun googleAuthForFirebase(context: Context, account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                val db = Firebase.firestore
                var money = Money("10000")
                val authUser = auth.currentUser?.email
                val sb = StringBuilder()
                val docRef = db.collection("$authUser-money").document("money")
                docRef.get().addOnSuccessListener {
                    if (it.exists()) {
                        money = it.toObject<Money>()!!
                        sb.append("${money.money}")
                        val saveData = SaveData(context)
                        saveData.setMoney(sb.toString())
                    } else {
                        db.collection("$authUser-money").document("money").set(money)
                        val saveData = SaveData(context)
                        saveData.setMoney("10000")
                    }
                }.await()

                withContext(Dispatchers.Main) {
                    Utils.startNewActivity(context, MainActivity::class.java)
                    Toast.makeText(
                        context,
                        "Successfully logged in!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun checkLogedinState(context: Context) {
        if (auth.currentUser == null) {
            Toast.makeText(context, "Please log in!", Toast.LENGTH_SHORT).show()
        } else {
            Utils.startNewActivity(context, MainActivity::class.java)
            Toast.makeText(context, "You are logged in!", Toast.LENGTH_SHORT).show()
        }
    }

}