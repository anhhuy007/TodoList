package com.example.todolist.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.ui.theme.TodoListTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Suppress("DEPRECATION")
class AuthenticationActivity : ComponentActivity() {

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user != null) {
            startActivity(Intent(this, TodoListActivity::class.java))
            finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            TodoListTheme {
                AuthenticationContent()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun AuthenticationContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp),
                painter = painterResource(id = R.mipmap.todo),
                contentDescription = "Todo List Logo",
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Sign In",
                fontSize = 40.sp,
                style = MaterialTheme.typography.h1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Access to your todo list account",
                color = Color.Gray,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            SignInMethods()
        }
    }

    @Composable
    private fun SignInMethods() {
        Column {
            SignInItem(
                iconRes = R.mipmap.google,
                text = "Google",
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                }
            )

            SignInItem(
                iconRes = R.mipmap.facebook,
                text = "Facebook",
                onClick = {
                    /*Sign in with FaceBook*/
                }
            )
        }
    }

    @Composable
    private fun SignInItem(
        iconRes: Int,
        text: String,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .clickable { onClick() },
                shape = RoundedCornerShape(10.dp),
            ) {
                Box{
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = text,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(15.dp)
                    )

                    Text(
                        text = text,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            if (task.isSuccessful) {
                try {
                    val account = task.getResult(Exception::class.java)!!
                    Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                    fireBaseAuthWithGoogle(account.idToken!!)
                } catch (e: Exception) {
                    Log.d("AnhHuy", "onActivityResult: ${e.message}")
                }
            } else {
                Log.w("TAG", "signInWithCredential:failure", task.exception)
            }
        }
    }

    private fun fireBaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    startActivity(Intent(this, TodoListActivity::class.java))
                    finish()
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }
}