package com.example.foodapp.ui.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Data.Categories
import com.example.foodapp.Data.Meals
import com.example.foodapp.Data.mealsR
import com.example.foodapp.databinding.FragmentHomeBinding

class HomeFragment:Fragment() {
    private lateinit var adapter: CategoriesAdapter
    private lateinit var Madapter: MealsAdapter
    private lateinit var Radapter: RandomAdapter


    private lateinit var binding : FragmentHomeBinding
    val viewModel: HomeModel by viewModels<HomeModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoriesAdapter { category ->
            onItemClick(category)
        }

        // Set up categories RecyclerView
        binding.categoriesList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesList.adapter = adapter

        // Initialize MealsAdapter
        Madapter = MealsAdapter(this::onClickFood)
        binding.MealsList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.MealsList.adapter = Madapter

        // Observe ViewModel for categories list updates
        viewModel.categoriesList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.categories = it
            }
        }

        // Fetch meals by default category (e.g., "Beef")
        viewModel.getFoodByCategories("Beef")
        viewModel.mealsList.observe(viewLifecycleOwner) {
            if (it != null) {
                Madapter.meals = it
            }
        }

        Radapter = RandomAdapter(this::onClickRandom)
        binding.RandomRecycle.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.RandomRecycle.adapter = Radapter


        viewModel.mealsList1.observe(viewLifecycleOwner){
            if (it != null) {
                Radapter.meals = it
            }
        }
    }

     fun onItemClick(category: String) {
         viewModel.getFoodByCategories(category)

     }

    fun onClickFood(mealId: Meals){
        val action = HomeFragmentDirections.actionHomeFragmentToFoodFragment(mealId.idMeal.toString())
        findNavController().navigate(action)
    }
    fun onClickRandom(mealId: mealsR){
        val action = HomeFragmentDirections.actionHomeFragmentToFoodFragment(mealId.idMeal.toString())
        findNavController().navigate(action)
    }


}