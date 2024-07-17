package com.example.foodapp.ui.Profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.databinding.ProfileFragmentBinding
import com.example.foodapp.ui.Home.HomeModel
import com.example.foodapp.ui.Intro.IntroFragment
import com.example.foodapp.ui.Login.LoginFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

class ProfileFragment:Fragment() {

    private lateinit var binding: ProfileFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ProfileAdapter
    private lateinit var database: DatabaseReference
    val viewModel: HomeModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Get a reference to the Firebase database
        database = FirebaseDatabase.getInstance().reference

        adapter = ProfileAdapter()
        binding.RecView.layoutManager = LinearLayoutManager(requireContext())
        binding.RecView.adapter = adapter

        readDataFavorites()
        observeViewModel()
        readData()


        binding.LogoutButton.setOnClickListener {
            logout()
        }
    }


    private fun logout() {
        auth.signOut()
        viewModel.clearFavoriteMeals()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        // Navigate to the login fragment or activity
        val intent = Intent(requireContext(), LoginFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
    fun readData(){

        val uid = Firebase.auth.currentUser?.uid
        val uidRef = Firebase.database.reference.child("users").child(uid.toString())
        uidRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val snapshot = it.result
                val name = snapshot.child("userName").getValue(String::class.java)
                val email = snapshot.child("email").getValue(String::class.java)
                binding.NameText.text = name
                binding.EmailText.text = email

                Log.d(TAG, "$name/$email")
            } else {
                Log.d(TAG, "${it.exception?.message}") //Never ignore potential errors!
            }
        }
    }
    private fun readDataFavorites() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Log.e(TAG, "User not authenticated")
            return
        }

        val uidRef = database.child("users").child(uid)

        uidRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val favoritesList = snapshot.child("favorites")
                        .getValue(object : GenericTypeIndicator<List<String>>() {})
                    if (!favoritesList.isNullOrEmpty()) {
                        for (id in favoritesList ) {
                            viewModel.getFavoriteById(id)

                            viewModel.clearFavoriteMeals()
                        }
                    } else {
                        Log.d(TAG, "No favorites found for user")
                    }
                } else {
                    Log.d(TAG, "Snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error reading data", error.toException())
            }
        })
    }

    private fun observeViewModel() {
        viewModel.favoriteMeals.observe(viewLifecycleOwner, Observer { meals ->
            meals?.let {
                adapter.meals = it
            }
        })
    }


}
