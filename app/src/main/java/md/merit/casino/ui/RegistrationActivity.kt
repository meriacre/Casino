package md.merit.casino.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import md.merit.casino.R

class RegistrationActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser(){
        val email = edt_email_reg.text.toString()
        val pass = edt_pass_reg.text.toString()
        val pass2 = edt_pass_reg2.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()){
            if (pass == pass2){
                CoroutineScope(Dispatchers.IO).launch {
                   try {

                    auth.createUserWithEmailAndPassword(email, pass).await()
                    withContext(Dispatchers.Main){
                        checkLogedinState()
                    }
                   }
                   catch (e:Exception){
                       withContext(Dispatchers.Main){
                       Toast.makeText(this@RegistrationActivity, e.message, Toast.LENGTH_SHORT).show()
                   }}
                }
            }
        }else{
            Toast.makeText(this, "Please complete all empty inputs!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLogedinState(){
        if (auth.currentUser == null){
            Toast.makeText(this, "Please log in!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
            Toast.makeText(this@RegistrationActivity, "You are logged in!", Toast.LENGTH_SHORT).show()
        }
    }

}