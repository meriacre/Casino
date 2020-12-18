package md.merit.casino.data

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import md.merit.casino.ui.MainActivity
import md.merit.casino.utils.Utils

class LogUser {

    lateinit var auth: FirebaseAuth

    fun loginUser(context:Context,login:EditText,password:EditText) {
        val email = login.text.toString()
        val pass = password.text.toString()
        auth = FirebaseAuth.getInstance()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    auth.signInWithEmailAndPassword(email, pass).await()
                    withContext(Dispatchers.Main) {
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


    private fun checkLogedinState(context: Context) {
        if (auth.currentUser == null) {
            Toast.makeText(context, "Please log in!", Toast.LENGTH_SHORT).show()
        } else {
            Utils.startNewActivity(context, MainActivity::class.java)
            Toast.makeText(context, "You are logged in!", Toast.LENGTH_SHORT).show()
        }
    }

}