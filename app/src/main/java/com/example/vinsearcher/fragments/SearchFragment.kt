package com.example.vinsearcher.fragments

import android.content.Context
import android.hardware.input.InputManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.airbnb.lottie.LottieAnimationView
import com.example.vinsearcher.MainActivity
import com.example.vinsearcher.R
import com.example.vinsearcher.util.repeatWhileActive
import kotlinx.coroutines.*
import androidx.core.content.ContextCompat.getSystemService




class SearchFragment(val callback: SearchCallback) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface SearchCallback {
        fun searchQuery(query: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val lottieView = view.findViewById<LottieAnimationView>(R.id.lottie_search).apply {
            postDelayed({
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in).apply {
                    duration =
                        resources.getInteger(R.integer.material_motion_duration_medium_2).toLong()
                    post {
                        speed = 0.2f
                        playAnimation()
                    }
                }
                visibility = View.VISIBLE
            }, resources.getInteger(R.integer.material_motion_duration_long_2).toLong())
        }
        CoroutineScope(Dispatchers.Main).launch {
            repeatWhileActive {
                lottieView.playAnimation()
                delay(15000)

                lottieView.reverseAnimationSpeed()
                lottieView.playAnimation()

                delay(15000)
                lottieView.reverseAnimationSpeed()

            }
        }

        val editText = view.findViewById<EditText>(R.id.search_fragment_edittext)
        val button = view.findViewById<Button>(R.id.search_fragment_button)

        button.setOnClickListener {
            processClick(it, editText)
        }

        editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                processClick(v, editText)
                handled = true

                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            return@OnEditorActionListener handled
        })

        return view
    }

    private fun processClick(view: View, editText: EditText) {
        val fragmentManager = parentFragmentManager

        if (editText.text.toString().isNotEmpty()) {
            callback.searchQuery(editText.text.toString().filterNot { it.isWhitespace() })
            fragmentManager.popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showNavigation()
        view?.findViewById<LottieAnimationView>(R.id.lottie_search)?.cancelAnimation()
    }
}