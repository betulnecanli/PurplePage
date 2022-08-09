package com.betulnecanli.purplepage.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding
            .bottomNavigationView
            .setupWithNavController(
                navController
            )

        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.subjectsFragment,
                    R.id.projectsFragment,
                    R.id.goalsFragment,
                ))

        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}

const val ADD_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1