package com.example.vinsearcher.util

import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun repeatWhileActive(block: () -> Unit): Nothing {
    while (true) {
        coroutineContext.ensureActive()
        block()
    }
}