package com.example.foodapp.ui.Home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.User
import com.example.foodapp.Data.mealsR
import com.example.foodapp.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class HomeFragment : Fragment() {
    private lateinit var adapter: CategoriesAdapter
    private lateinit var Madapter: MealsAdapter
    private lateinit var Radapter: RandomAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeModel by viewModels<HomeModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get a reference to the Firebase database
        database = FirebaseDatabase.getInstance().reference

        // Set up adapters and RecyclerViews
        setupAdaptersAndRecyclerViews()

        // Observe ViewModel for categories list updates
        observeViewModel()

        // Handle search input changes
        binding.search.addTextChangedListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFragmentSearch()
            findNavController().navigate(action)
        }

        // Read data from the Firebase Realtime Database
        readData()

        binding.ImageCountries.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSavedFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupAdaptersAndRecyclerViews() {
        adapter = CategoriesAdapter { category ->
            onItemClick(category)
        }
        binding.categoriesList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesList.adapter = adapter

        Madapter = MealsAdapter(this::onClickFood)
        binding.MealsList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.MealsList.adapter = Madapter

        Radapter = RandomAdapter(this::onClickRandom)
        binding.RandomRecycle.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.RandomRecycle.adapter = Radapter
    }

    private fun observeViewModel() {
        viewModel.categoriesList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.categories = it
            }
        }

        viewModel.getFoodByCategories("Beef")
        viewModel.mealsList.observe(viewLifecycleOwner) {
            if (it != null) {
                Madapter.meals = it
            }
        }

        viewModel.mealsList1.observe(viewLifecycleOwner) {
            if (it != null) {
                Radapter.meals = it
            }
        }
    }

    private fun onItemClick(category: String) {
        viewModel.getFoodByCategories(category)
    }

    private fun onClickFood(mealId: Meals) {
        val action = HomeFragmentDirections.actionHomeFragmentToFoodFragment(mealId.idMeal.toString())
        findNavController().navigate(action)
    }

    private fun onClickRandom(mealId: mealsR) {
        val action = HomeFragmentDirections.actionHomeFragmentToFoodFragment(mealId.idMeal.toString())
        findNavController().navigate(action)
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

                Log.d(TAG, "$name/$email")
            } else {
                Log.d(TAG, "${it.exception?.message}") //Never ignore potential errors!
            }
        }
    }

}
