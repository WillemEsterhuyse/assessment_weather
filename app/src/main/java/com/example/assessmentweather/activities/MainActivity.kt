package com.example.assessmentweather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.assessmentweather.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        bottomNavigationView.setupWithNavController(findNavController(R.id.fragment))
    }
}
