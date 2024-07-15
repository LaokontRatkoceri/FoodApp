package com.example.foodapp.ui.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.Data.User
import com.example.foodapp.databinding.LoginFragmentBinding
import com.example.foodapp.ui.Account.RegisterFragment
import com.example.foodapp.ui.Intro.IntroFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : AppCompatActivity() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()

        FirebaseApp.initializeApp(this)

        database = FirebaseDatabase.getInstance().reference


        binding.LoginButton.setOnClickListener {
            login()
        }

        binding.SignupButton.setOnClickListener {
            val intent = Intent(this, RegisterFragment::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val email = binding.EmailEditText.text.toString()
        val pass = binding.PasswordEditText.text.toString()

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    println("Login successful ${user.toString()}")

                    val intent = Intent(this, IntroFragment::class.java)
                    startActivity(intent)
                    user?.let {
                        val userRef = database.child("users").child(it.uid)
                        userRef.get().addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                val userData = snapshot.getValue(User::class.java)
                                Log.d("SignIn", "User data: $userData")
                            } else {
                                Log.d("SignIn", "No data available")
                            }
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}
