package md.merit.casino.data

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import md.merit.casino.ui.MainActivity
import md.merit.casino.utils.Utils

class RegUser {

    lateinit var auth: FirebaseAuth

    fun registerUser(context: Context, emaill:TextView, pass1: TextView, passs2: TextView){
        val email = emaill.text.toString()
        val pass = pass1.text.toString()
        val pass2 = passs2.text.toString()
        auth = FirebaseAuth.getInstance()

        if (email.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()){
            if (pass == pass2){
                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        auth.createUserWithEmailAndPassword(email, pass).await()
                        withContext(Dispatchers.Main){
                            checkLogedinState(context)
                        }
                    }
                    catch (e:Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }else{
            Toast.makeText(context, "Please complete all empty inputs!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLogedinState(context: Context){
        if (auth.currentUser == null){
            Toast.makeText(context, "Please log in!", Toast.LENGTH_SHORT).show()
        }else{
            Utils.startNewActivity(context, MainActivity::class.java )
            Toast.makeText(context, "You are logged in!", Toast.LENGTH_SHORT).show()
        }
    }
}