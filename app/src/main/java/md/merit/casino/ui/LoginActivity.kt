package md.merit.casino.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import md.merit.casino.R

const val REQUIEST_CODE_SIGN_IN = 0

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

       tv_register.setOnClickListener {
           startActivity(Intent(this, RegistrationActivity::class.java))
       }

        btn_login.setOnClickListener {
            loginUser()
        }

        btn_connect_google.setOnClickListener {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(this, options)
        signInClient.signInIntent.also {
            startActivityForResult(it, REQUIEST_CODE_SIGN_IN)
        }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUIEST_CODE_SIGN_IN){
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount){
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main){
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    Toast.makeText(this@LoginActivity, "Successfully logged in!", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkLogedinState()
    }
    private fun loginUser(){
        val email = edt_login.text.toString()
        val pass = edt_password.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        auth.signInWithEmailAndPassword(email, pass).await()
                        withContext(Dispatchers.Main){
                            checkLogedinState()
                        }
                    }
                    catch (e:Exception){
                        withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }}
                }
            }
        else{
            Toast.makeText(this, "Please complete all empty inputs!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun checkLogedinState(){
        if (auth.currentUser == null){
            Toast.makeText(this, "Please log in!", Toast.LENGTH_SHORT).show()
        }else
        {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            Toast.makeText(this@LoginActivity, "You are logged in!", Toast.LENGTH_SHORT).show()
        }
    }

}