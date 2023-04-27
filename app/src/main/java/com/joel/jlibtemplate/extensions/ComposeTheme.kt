package com.joel.jlibtemplate.extensions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.joel.jlibtemplate.ui.theme.CommunicationComposeTheme

fun Context.composeTheme(block: @Composable () -> Unit) = ComposeView(this).apply {
    setContent {
        CommunicationComposeTheme(block)
    }
}