package com.example.vinsearcher

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vinsearcher.di.DaggerAppComponent
import com.example.vinsearcher.network.CarInfoModule
import com.example.vinsearcher.viewmodels.MainActivityViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    val viewModel = MainActivityViewModel()

    @Inject
    @Named("Car info")
    lateinit var carInfoClient: CarInfoModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class MyApplication : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}