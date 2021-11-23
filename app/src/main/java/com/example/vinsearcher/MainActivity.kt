package com.example.vinsearcher

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.animation.addListener
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.example.vinsearcher.di.DaggerAppComponent
import com.example.vinsearcher.fragments.SearchFragment
import com.example.vinsearcher.network.CarInfoModule
import com.example.vinsearcher.viewmodels.MainActivityViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject
import javax.inject.Named

import android.util.TypedValue
import androidx.annotation.ColorInt


class MainActivity : AppCompatActivity() {

    val viewModel = MainActivityViewModel()

    @Inject
    @Named("Car info")
    lateinit var carInfoClient: CarInfoModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val duplicateFab = findViewById<FloatingActionButton>(R.id.main_button_search_duplicate)
        val fab = findViewById<FloatingActionButton>(R.id.main_button_search)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.app_bottom_bar)
        val fragmentContainer = findViewById<FragmentContainerView>(R.id.main_fragment_container)


        fab.setOnClickListener {
            hideNavigation()
        }
    }

    fun hideNavigation() {

        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorSecondary, typedValue, true)
        @ColorInt val statusBarColor = typedValue.data

        val fragmentContainer = findViewById<FragmentContainerView>(R.id.main_fragment_container)
        val fab = findViewById<FloatingActionButton>(R.id.main_button_search)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.app_bottom_bar)
        val duplicateFab = findViewById<FloatingActionButton>(R.id.main_button_search_duplicate)

        val alphaContainer =
            ObjectAnimator.ofFloat(fragmentContainer, View.ALPHA, 0f, 1f) // from 0 to 1

        val diff = duplicateFab.rootView.height / duplicateFab.height
        val scaleAnimation = ObjectAnimator.ofFloat(duplicateFab, "scaleY", 0f, diff.toFloat())
        val scaleAnimationWidth =
            ObjectAnimator.ofFloat(duplicateFab, "scaleX", 0f, diff.toFloat())


        val animation = AnimatorSet().apply {
            duration =
                resources.getInteger(R.integer.material_motion_duration_medium_1).toLong()
            duplicateFab.show()
            playTogether(scaleAnimation, scaleAnimationWidth, alphaContainer)
            addListener(onEnd = {
                duplicateFab.visibility = View.INVISIBLE
            }, onStart = {
                bottomAppBar.animation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_down)
                bottomAppBar.performHide()
                fab.visibility = View.GONE
                supportFragmentManager.commit {
                    replace(R.id.main_fragment_container, SearchFragment())
                    addToBackStack("welcome")
                }
                window.statusBarColor = statusBarColor
            })
        }
        animation.start()

    }

    fun simpleHideNavigation() {
        findViewById<BottomAppBar>(R.id.app_bottom_bar).performHide()
        findViewById<FloatingActionButton>(R.id.main_button_search).hide()
    }

    fun showNavigation() {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        @ColorInt val statusBarColor = typedValue.data

        val duplicateFab = findViewById<FloatingActionButton>(R.id.main_button_search_duplicate)
        val fragmentContainer = findViewById<FragmentContainerView>(R.id.main_fragment_container)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.app_bottom_bar)

        val diff = duplicateFab.rootView.height / duplicateFab.height * 1.3
        val scaleAnimation = ObjectAnimator.ofFloat(duplicateFab, "scaleY", 0f, diff.toFloat())
        val scaleAnimationWidth =
            ObjectAnimator.ofFloat(duplicateFab, "scaleX", 0f, diff.toFloat())

        val alphaContainer =
            ObjectAnimator.ofFloat(fragmentContainer, View.ALPHA, 0f, 1f) // from 0 to 1

        alphaContainer.reverse()
        scaleAnimation.reverse()
        scaleAnimationWidth.reverse()

        val animation = AnimatorSet().apply {
            duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            play(alphaContainer) // play rest.
            addListener(onEnd = {
                findViewById<FloatingActionButton>(R.id.main_button_search).show()
                bottomAppBar.performShow()
                duplicateFab.visibility = View.INVISIBLE
            }, onStart = {
                duplicateFab.visibility = View.VISIBLE
                bottomAppBar.animation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_up)
                window.statusBarColor = statusBarColor
            })
        }

        animation.start()
    }

    fun simpleShowNavigation(){
        findViewById<FloatingActionButton>(R.id.main_button_search).show()
        findViewById<BottomAppBar>(R.id.app_bottom_bar).performShow()
    }
}

class MyApplication : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}