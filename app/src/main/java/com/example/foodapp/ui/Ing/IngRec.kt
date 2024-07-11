package com.example.foodapp.ui.Ing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Data.Ing
import com.example.foodapp.databinding.InRecyBinding

class IngRec:RecyclerView.Adapter<IngRec.ViewHolder>(){

    var meals: List<Ing> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(val binding: InRecyBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = InRecyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        with(holder.binding){
            IngMateTextView.text = meal.strIngredient
        }
    }

    override fun getItemCount(): Int = meals.size

}