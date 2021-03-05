/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.play.compose.common.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.zj.play.R
import com.zj.play.compose.common.X5WebView

/**
 * Remembers a MapView and gives it the lifecycle of the current LifecycleOwner
 */
@Composable
fun rememberX5WebViewWithLifecycle(): X5WebView {
    val context = LocalContext.current
    val x5WebView = remember {
        X5WebView(context).apply {
            id = R.id.web_view
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberX5WebViewLifecycleObserver(x5WebView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return x5WebView
}

@Composable
private fun rememberX5WebViewLifecycleObserver(x5WebView: X5WebView): LifecycleEventObserver =
    remember(x5WebView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> x5WebView.onCreate()
                Lifecycle.Event.ON_START -> x5WebView.onStart()
                Lifecycle.Event.ON_RESUME -> x5WebView.onResume()
                Lifecycle.Event.ON_PAUSE -> x5WebView.onPause()
                Lifecycle.Event.ON_STOP -> x5WebView.onStop()
                Lifecycle.Event.ON_DESTROY -> x5WebView.destroy()
                else -> throw IllegalStateException()
            }
        }
    }
