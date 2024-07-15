package com.example.foodapp.ui.Ing

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodapp.Data.mealsR
import com.example.foodapp.Utils.CircularTransformation
import com.example.foodapp.databinding.FoodFragmentBinding
import com.example.foodapp.ui.Home.HomeModel
import com.squareup.picasso.Picasso

class IngFragment : Fragment() {

    private lateinit var binding: FoodFragmentBinding
    private val args: IngFragmentArgs by navArgs()
    private val viewModel: HomeModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FoodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.meal.observe(viewLifecycleOwner) { meal ->
            // Update the UI with the meal data
            meal?.let {
                updateUI(it)
            }
        }

        // Fetch food by ID from arguments
        viewModel.getFoodById(args.mealId)


        binding.BackArrow.setOnClickListener {
            val action = IngFragmentDirections.actionFoodFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun updateUI(meal: mealsR) {

        with(binding) {
            Picasso.get().load(meal.strMealThumb).transform(CircularTransformation()).into(MealImage)

            val imgTextViews = listOf(
                Ing1,
                Ing2,
                Ing3,
                Ing4,
                Ing5,
                Ing6,
                Ing7,
                Ing8,
                Ing9,
                Ing10,
                Ing11,
                Ing12,
                Ing13,
                Ing14,
                Ing15,
                Ing16,
                Ing17,
                Ing18,
                Ing19,
                Ing20,
                Ing21,
                Ing22,
                Ing23
            )
            NameMealTextView.text = meal.strMeal



            for(i in imgTextViews.indices){
                imgTextViews[i].Alpha.visibility = View.GONE
            }


            IngridientsTextView.setOnClickListener {
                showIngredients(meal)
            }


            InstrTextView.setOnClickListener {
                showInstructions(meal)

            }
        }
    }

    private fun showIngredients(meal: mealsR) {
        with(binding) {
            OnClickInst.visibility = View.INVISIBLE
            InstrTextView.setTextColor(Color.parseColor("#129575"))


            IngridientsTextView.setTextColor(Color.WHITE)
            OnClickIng.visibility = View.VISIBLE

            val imgTextViews = listOf(
                Ing1,
                Ing2,
                Ing3,
                Ing4,
                Ing5,
                Ing6,
                Ing7,
                Ing8,
                Ing9,
                Ing10,
                Ing11,
                Ing12,
                Ing13,
                Ing14,
                Ing15,
                Ing16,
                Ing17,
                Ing18,
                Ing19,
                Ing20
            )

            val measures = listOf(
                meal.strMeasure1,
                meal.strMeasure2,
                meal.strMeasure3,
                meal.strMeasure4,
                meal.strMeasure5,
                meal.strMeasure6,
                meal.strMeasure7,
                meal.strMeasure8,
                meal.strMeasure9,
                meal.strMeasure10,
                meal.strMeasure11,
                meal.strMeasure12,
                meal.strMeasure13,
                meal.strMeasure14,
                meal.strMeasure15,
                meal.strMeasure16,
                meal.strMeasure17,
                meal.strMeasure18,
                meal.strMeasure19,
                meal.strMeasure20
            )
            val ingredients = listOf(
                meal.strIngredient1,
                meal.strIngredient2,
                meal.strIngredient3,
                meal.strIngredient4,
                meal.strIngredient5,
                meal.strIngredient6,
                meal.strIngredient7,
                meal.strIngredient8,
                meal.strIngredient9,
                meal.strIngredient10,
                meal.strIngredient11,
                meal.strIngredient12,
                meal.strIngredient13,
                meal.strIngredient14,
                meal.strIngredient15,
                meal.strIngredient16,
                meal.strIngredient17,
                meal.strIngredient18,
                meal.strIngredient19,
                meal.strIngredient20
            )


            for(i in imgTextViews.indices){
                imgTextViews[i].InstrTextView.visibility = View.GONE
                imgTextViews[i].IngMateTextView.visibility = View.VISIBLE
                imgTextViews[i].MesMateTextView.visibility = View.VISIBLE
            }


            for (i in imgTextViews.indices) {
                if (i < ingredients.size && !ingredients[i].isNullOrEmpty()) {
                    imgTextViews[i].IngMateTextView.apply {
                        text = ingredients[i]
                        visibility = View.VISIBLE

                    }
                    imgTextViews[i].InstrTextView.visibility = View.GONE
//                    imgTextViews[i].Alpha.visibility = View.VISIBLE


                } else {
                    imgTextViews[i].Alpha.visibility = View.INVISIBLE
                }

                if (i < measures.size && !measures[i].isNullOrEmpty()) {
                    imgTextViews[i].MesMateTextView.apply {
                        text = measures[i]
                        visibility = View.VISIBLE
                    }
                    imgTextViews[i].InstrTextView.visibility = View.GONE
//                    imgTextViews[i].Alpha.visibility = View.VISIBLE

                } else {
                    imgTextViews[i].Alpha.visibility = View.GONE
                }
            }
        }

    }



    private fun showInstructions(meal: mealsR) {
        with(binding) {
            OnClickIng.visibility = View.INVISIBLE
            IngridientsTextView.setTextColor(Color.parseColor("#129575"))


            OnClickInst.visibility = View.VISIBLE
            InstrTextView.setTextColor(Color.WHITE)

            // Set the instructions text
            val instructions = meal.strInstructions?.trimIndent()?.replace("\r\n", "")?.split(".")

            val imgTextViews = listOf(
                Ing1,
                Ing2,
                Ing3,
                Ing4,
                Ing5,
                Ing6,
                Ing7,
                Ing8,
                Ing9,
                Ing10,
                Ing11,
                Ing12,
                Ing13,
                Ing14,
                Ing15,
                Ing16,
                Ing17,
                Ing18,
                Ing19,
                Ing20,
                Ing21,
                Ing22
            )




            for(i in imgTextViews.indices){
                imgTextViews[i].IngMateTextView.visibility = View.INVISIBLE
                imgTextViews[i].MesMateTextView.visibility = View.INVISIBLE
            }

            if (instructions != null) {
                for (i in imgTextViews.indices) {
                    if (i < instructions.size && instructions[i].isNotEmpty()) {
                        imgTextViews[i].InstrTextView.text = instructions[i]
                        imgTextViews[i].InstrTextView.visibility = View.VISIBLE
                        imgTextViews[i].Alpha.visibility = View.VISIBLE

                    }else {
                        imgTextViews[i].InstrTextView.visibility = View.GONE
                        imgTextViews[i].Alpha.visibility = View.GONE

                    }

                }
            }
        }
    }
}
