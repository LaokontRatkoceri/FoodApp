package com.example.foodapp.ui.Account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.Data.User
import com.example.foodapp.databinding.SignupFragmentBinding
import com.example.foodapp.ui.Login.LoginFragment
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class RegisterFragment: AppCompatActivity() {

    private lateinit var binding: SignupFragmentBinding

    private lateinit var auth: FirebaseAuth

    lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupFragmentBinding.inflate(layoutInflater)

        setContentView(binding.root)


        auth = Firebase.auth

        FirebaseApp.initializeApp(this)
        database = Firebase.database.reference



        binding.RegisterButton.setOnClickListener {
            signUpUser()
        }

        binding.LoginText.setOnClickListener {
            val intent = Intent(this, LoginFragment::class.java)
            startActivity(intent)
        }
    }
    private fun signUpUser() {
        val email =binding.EmailEditText.text
        val pass = binding.PassEditText.text
        val confirmPassword = binding.PassConEditText.text.toString()


        // check pass
        if (email.toString().isBlank() || pass.toString().isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()

        }

        if (pass.toString() != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()

        }

        auth.createUserWithEmailAndPassword(
            email.toString(),pass.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                val user = auth.currentUser
                println("Register successfull ${user.toString()}")
                val intent = Intent(this, LoginFragment::class.java)
                startActivity(intent)


                user?.let {
                    val userRef = database.child("users").child(it.uid)
                    val userName = it.email?.split("@")?.get(0)
                    userRef.setValue(User(userName, it.email))
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}