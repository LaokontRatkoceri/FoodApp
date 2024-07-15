package com.example.foodapp.ui.Intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.IntroFragmentBinding
import com.example.foodapp.ui.Login.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class IntroFragment: AppCompatActivity() {

    private lateinit var binding: IntroFragmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        
    }
}