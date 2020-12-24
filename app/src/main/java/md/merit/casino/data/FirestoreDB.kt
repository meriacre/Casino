package md.merit.casino.data

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import md.merit.casino.models.Game1
import md.merit.casino.models.Money

class FirestoreDB() {
    val authUser = FirebaseAuth.getInstance().currentUser?.email
    private val slotsCollectionRef = Firebase.firestore.collection("$authUser")
    private val profileCollectionRef = Firebase.firestore.collection("$authUser-money")


  fun saveSlots(context:Context, game1:Game1) = CoroutineScope(Dispatchers.IO).launch {
        try {
            slotsCollectionRef.add(game1).await()
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveMoney(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val saveData = SaveData(context)
            val money = Money(saveData.loadMoney()!!)
            profileCollectionRef.document("money").set(money)
        } catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}