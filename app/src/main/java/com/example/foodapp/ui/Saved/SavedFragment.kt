package com.example.foodapp.ui.Saved

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.MealsList
import com.example.foodapp.Data.mealsR
import com.example.foodapp.MainActivity
import com.example.foodapp.databinding.SavedFragmentBinding
import com.example.foodapp.databinding.SearchFragmentBinding
import com.example.foodapp.ui.Home.HomeModel
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

class SavedFragment:Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: SavedAdapter
    private lateinit var database: DatabaseReference
    private lateinit var binding: SavedFragmentBinding
    val viewModel: HomeModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SavedFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        adapter = SavedAdapter()
        binding.RecycleSaved.layoutManager = LinearLayoutManager(requireContext())
        binding.RecycleSaved.adapter = adapter




        viewModel.mealsList1.observe(viewLifecycleOwner, Observer{
            adapter.meals = it
            adapter.notifyDataSetChanged()
        })

        binding.BackArrow.setOnClickListener {
            val action = SavedFragmentDirections.actionSavedFragmentToHomeFragment()
            findNavController().navigate(action)
        }


        readData()

    }


    private fun readData() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Log.e(TAG, "User not authenticated")
            return
        }

        val uidRef = database.child("users").child(uid)

        uidRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val favoritesList = snapshot.child("favorites").getValue(object : GenericTypeIndicator<List<String>>() {})
                    if (favoritesList != null) {
                        for (favoriteId in favoritesList) {
                            viewModel.getFavoriteById(favoriteId)
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

}
