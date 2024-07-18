package com.example.foodapp.ui.Ing

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodapp.Data.mealsR
import com.example.foodapp.Utils.CircularTransformation
import com.example.foodapp.databinding.FoodFragmentBinding
import com.example.foodapp.ui.Home.HomeModel
import com.squareup.picasso.Picasso

class IngFragment : Fragment() {

    private lateinit var binding: FoodFragmentBinding // Deklarimi i 'binding' për layout-in e fragmentit
    private val args: IngFragmentArgs by navArgs() // Marrja e argumenteve të fragmentit për të marrë 'mealId'
    private val viewModel: HomeModel by viewModels() // Inicimi i ViewModel-it për fragmentin

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FoodFragmentBinding.inflate(inflater, container, false) // Inflating layout-in e fragmentit
        return binding.root // Kthimi i root view-it të layout-it të inflatuar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.meal.observe(viewLifecycleOwner) { meal -> // Observimi i LiveData-s 'meal' për ndryshime
            meal?.let { updateUI(it) } // Nëse 'meal' nuk është null, përditëso UI-n
        }

        viewModel.getFoodById(args.mealId) // Kërko ushqimin sipas 'mealId' të marrë nga argumentet

        binding.BackArrow.setOnClickListener {
            findNavController().navigate(IngFragmentDirections.actionFoodFragmentToHomeFragment()) // Navigo në 'HomeFragment' kur klikohet butoni i kthimit
        }
    }

    private fun updateUI(meal: mealsR) {
        with(binding) {
            Picasso.get()
                .load(meal.strMealThumb)
                .transform(CircularTransformation())
                .into(MealImage) // Shfaq imazhin e ushqimit duke përdorur Picasso dhe një transformim rrethor

            val imgTextViews = listOf(
                Ing1, Ing2, Ing3, Ing4, Ing5, Ing6, Ing7, Ing8, Ing9, Ing10,
                Ing11, Ing12, Ing13, Ing14, Ing15, Ing16, Ing17, Ing18, Ing19, Ing20, Ing21, Ing22, Ing23
            ) // Lista e TextView-ve për përbërësit

            NameMealTextView.text = meal.strMeal // Vendos emrin e ushqimit

            imgTextViews.forEach { it.Alpha.visibility = View.GONE } // Fshi të gjitha TextView-t e përbërësve fillimisht

            IngridientsTextView.setOnClickListener { showIngredients(meal) } // Vendos onClickListener për shfaqjen e përbërësve
            InstrTextView.setOnClickListener { showInstructions(meal) } // Vendos onClickListener për shfaqjen e instruksioneve
        }
    }

    private fun showIngredients(meal: mealsR) {
        with(binding) {
            OnClickInst.visibility = View.INVISIBLE // Fshi view-in e instruksioneve
            InstrTextView.setTextColor(Color.parseColor("#129575")) // Ndrysho ngjyrën e tekstit për instruksione

            IngridientsTextView.setTextColor(Color.WHITE) // Ndrysho ngjyrën e tekstit për përbërësit
            OnClickIng.visibility = View.VISIBLE // Shfaq view-in e përbërësve

            val imgTextViews = listOf(
                Ing1, Ing2, Ing3, Ing4, Ing5, Ing6, Ing7, Ing8, Ing9, Ing10,
                Ing11, Ing12, Ing13, Ing14, Ing15, Ing16, Ing17, Ing18, Ing19, Ing20
            ) // Lista e TextView-ve për përbërësit

            val measures = listOf(
                meal.strMeasure1, meal.strMeasure2, meal.strMeasure3, meal.strMeasure4, meal.strMeasure5,
                meal.strMeasure6, meal.strMeasure7, meal.strMeasure8, meal.strMeasure9, meal.strMeasure10,
                meal.strMeasure11, meal.strMeasure12, meal.strMeasure13, meal.strMeasure14, meal.strMeasure15,
                meal.strMeasure16, meal.strMeasure17, meal.strMeasure18, meal.strMeasure19, meal.strMeasure20
            ) // Lista e masave të përbërësve

            val ingredients = listOf(
                meal.strIngredient1, meal.strIngredient2, meal.strIngredient3, meal.strIngredient4, meal.strIngredient5,
                meal.strIngredient6, meal.strIngredient7, meal.strIngredient8, meal.strIngredient9, meal.strIngredient10,
                meal.strIngredient11, meal.strIngredient12, meal.strIngredient13, meal.strIngredient14, meal.strIngredient15,
                meal.strIngredient16, meal.strIngredient17, meal.strIngredient18, meal.strIngredient19, meal.strIngredient20
            ) // Lista e përbërësve

            imgTextViews.forEachIndexed { index, textView ->
                textView.IngMateTextView.visibility = View.VISIBLE
                textView.MesMateTextView.visibility = View.VISIBLE
                textView.Alpha.visibility = if (ingredients[index].isNullOrEmpty() && measures[index].isNullOrEmpty()) View.GONE else View.VISIBLE
            } // Shfaq/FSHI përbërësit dhe masat bazuar në përmbajtjen e tyre

            imgTextViews.forEachIndexed { index, textView ->
                textView.IngMateTextView.apply {
                    text = ingredients.getOrNull(index)
                    visibility = if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                }
                textView.MesMateTextView.apply {
                    text = measures.getOrNull(index)
                    visibility = if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                }
                textView.InstrTextView.visibility = View.GONE
            } // Vendos tekstin dhe dukshmërinë e përbërësve dhe masave
        }
    }

    private fun showInstructions(meal: mealsR) {
        with(binding) {
            OnClickIng.visibility = View.INVISIBLE // Fshi view-in e përbërësve
            IngridientsTextView.setTextColor(Color.parseColor("#129575")) // Ndrysho ngjyrën e tekstit për përbërësit
            OnClickInst.visibility = View.VISIBLE // Shfaq view-in e instruksioneve
            InstrTextView.setTextColor(Color.WHITE) // Ndrysho ngjyrën e tekstit për instruksionet

            val instructions = meal.strInstructions?.trimIndent()?.replace("\r\n", "")?.split(".") // Merr instruksionet dhe ndaji në lista të veçanta
            val imgTextViews = listOf(
                Ing1, Ing2, Ing3, Ing4, Ing5, Ing6, Ing7, Ing8, Ing9, Ing10,
                Ing11, Ing12, Ing13, Ing14, Ing15, Ing16, Ing17, Ing18, Ing19, Ing20, Ing21, Ing22
            ) // Lista e TextView-ve për instruksionet

            imgTextViews.forEach {
                it.IngMateTextView.visibility = View.INVISIBLE
                it.MesMateTextView.visibility = View.INVISIBLE
            } // Fshi TextView-t e përbërësve dhe masave

            instructions?.forEachIndexed { index, instruction ->
                if (index < imgTextViews.size) {
                    imgTextViews[index].InstrTextView.text = instruction
                    imgTextViews[index].InstrTextView.visibility = View.VISIBLE
                    imgTextViews[index].Alpha.visibility = View.VISIBLE
                }
            } // Vendos tekstin dhe dukshmërinë e instruksioneve

            imgTextViews.drop(instructions?.size ?: 0).forEach {
                it.InstrTextView.visibility = View.GONE
                it.Alpha.visibility = View.GONE
            } // Fshi TextView-t e tepërta
        }
    }
}
