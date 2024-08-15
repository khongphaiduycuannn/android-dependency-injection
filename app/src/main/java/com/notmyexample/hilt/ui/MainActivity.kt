package com.notmyexample.hilt.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.notmyexample.hilt.R
import com.notmyexample.hilt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, ButtonsFragment())
            .addToBackStack(packageName)
            .commit()
    }
}