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
import md.merit.casino.data.LogUser

const val REQUIEST_CODE_SIGN_IN = 0

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        val logUser = LogUser()

        tv_register.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        btn_login.setOnClickListener {
           logUser.loginUser(this, edt_login, edt_password)
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
       val logUser = LogUser()
        if (requestCode == REQUIEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
              logUser.googleAuthForFirebase(this, it)
            }
        }
    }
    

    override fun onStart() {
        super.onStart()
        checkLogedinState()
    }

    private fun checkLogedinState() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "Please log in!", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            Toast.makeText(this@LoginActivity, "You are logged in!", Toast.LENGTH_SHORT).show()
        }
    }

}