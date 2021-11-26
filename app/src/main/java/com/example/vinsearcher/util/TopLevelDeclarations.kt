package com.example.vinsearcher.util

import android.view.View
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun repeatWhileActive(block: () -> Unit): Nothing {
    while (true) {
        coroutineContext.ensureActive()
        block()
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}