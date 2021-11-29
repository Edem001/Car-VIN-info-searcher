package com.example.vinsearcher.di

import android.content.Context
import androidx.room.Room
import com.example.vinsearcher.MainActivity
import com.example.vinsearcher.fragments.CarInfoFragment
import com.example.vinsearcher.fragments.WelcomeFragment
import com.example.vinsearcher.network.CarImage
import com.example.vinsearcher.network.CarInfoModule
import com.example.vinsearcher.room.CarDatabase
import com.example.vinsearcher.room.TypeConverters
import com.example.vinsearcher.viewmodels.MainActivityViewModel
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
@Component(modules = [WebClient::class, DatabaseClient::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: WelcomeFragment)
    fun inject(fragment: CarInfoFragment)
}

@Module
class WebClient {

    @Singleton
    @Provides
    @Named("Car info")
    fun provideWebClient(): CarInfoModule {

        return Retrofit.Builder()
            .baseUrl("https://vpic.nhtsa.dot.gov/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CarInfoModule::class.java)
    }

    @Singleton
    @Named("Image search")
    @Provides
    fun getImageURL(): CarImage {
        return Retrofit.Builder()
            .baseUrl("https://imsea.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CarImage::class.java)
    }

    @Singleton
    @Provides
    fun provideMainViewModel(
        @Named("Car info") client: CarInfoModule,
        @Named("Image search") image: CarImage
    ): MainActivityViewModel = MainActivityViewModel(client, image)
}

@Module
class DatabaseClient {
    @Singleton
    @Provides
    fun provideRoom(context: Context): CarDatabase {
        return Room.databaseBuilder(
            context,
            CarDatabase::class.java,
            "car-database"
        ).build()
    }
}