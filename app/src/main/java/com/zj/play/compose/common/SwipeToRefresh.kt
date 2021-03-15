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

package com.zj.play.compose.common

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private const val TAG = "SwipeToRefreshLayout"

private val RefreshDistance = 80.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRefreshLayout(
    refreshingState: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    val refreshDistance = with(LocalDensity.current) { RefreshDistance.toPx() }
    val state = rememberSwipeableState(refreshingState) { newValue ->
        // compare both copies of the swipe state before calling onRefresh(). This is a workaround.
        if (newValue && !refreshingState) onRefresh()
        true
    }
    //Log.e(TAG, "SwipeToRefreshLayout: refreshDistance:$refreshDistance")

    Box(
        modifier = Modifier
            .nestedScroll(state.PreUpPostDownNestedScrollConnection)
            .swipeable(
                state = state,
                anchors = mapOf(
                    -refreshDistance to false,
                    refreshDistance to true,
                ),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Vertical
            )
    ) {
        content()
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, state.offset.value.roundToInt()) }
        ) {
            if (state.offset.value != -refreshDistance) {
                Surface(elevation = 10.dp, shape = CircleShape) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            }
        }


        // TODO (https://issuetracker.google.com/issues/164113834): This state->event trampoline is a
        //  workaround for a bug in the SwipableState API. Currently, state.value is a duplicated
        //  source of truth of refreshingState.
        LaunchedEffect(refreshingState) { state.animateTo(refreshingState) }
    }
}

/**
 * Temporary workaround for nested scrolling behavior. There is no default implementation for
 * pull to refresh yet, this nested scroll connection mimics the behavior.
 */
@ExperimentalMaterialApi
private val <T> SwipeableState<T>.PreUpPostDownNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            Log.d(TAG, "onPreScroll: available:$available   source:$source")
            val delta = available.toFloat()
            return if (delta < 0 && source == NestedScrollSource.Drag) {
                Log.e(TAG, "onPreScroll111: delta:$delta")
                performDrag(delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            Log.d(TAG, "onPostScroll: consumed:$consumed   available:$available   source:$source ")
            return if (source == NestedScrollSource.Drag) {
                Log.e(TAG, "onPostScroll111: available:$available" )
                performDrag(available.toFloat()).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = Offset(available.x, available.y).toFloat()
            Log.d(TAG, "onPreFling: available$available")
            return if (toFling < 0) {
                Log.e(TAG, "onPreFling111: $toFling")
                performFling(velocity = toFling)
                // since we go to the anchor with tween settling, consume all for the best UX
                // available
                Velocity.Zero
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(
            consumed: Velocity,
            available: Velocity
        ): Velocity {
            Log.e(TAG, "onPostFling: consumed:$consumed   available:$available")
            performFling(velocity = Offset(available.x, available.y).toFloat())
            return Velocity.Zero
        }

        private fun Float.toOffset(): Offset = Offset(0f, this)

        private fun Offset.toFloat(): Float = this.y
    }