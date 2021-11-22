package com.example.vinsearcher.di

import android.content.Context
import com.example.vinsearcher.MainActivity
import com.example.vinsearcher.network.CarInfoModule
import com.google.gson.GsonBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [WebClient::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
}

@Module
class WebClient {

    @Provides
    @Named("Car info")
    fun provideWebClient(): CarInfoModule {

        return Retrofit.Builder()
            .baseUrl("https://vpic.nhtsa.dot.gov/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CarInfoModule::class.java)
    }
}