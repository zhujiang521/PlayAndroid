/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.play.ui.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.VectorizedAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt


@Composable
fun LandNavigation(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = LandNavigationDefaults.Elevation,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .width(landNavigationWidth)
                .selectableGroup(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

/**
 * <a href="https://material.io/components/bottom-navigation" class="external" target="_blank">Material Design bottom navigation</a> item.
 *
 * The recommended configuration for a LandNavigationItem depends on how many items there are
 * inside a [LandNavigation]:
 *
 * - Three destinations: Display icons and text labels for all destinations.
 * - Four destinations: Active destinations display an icon and text label. Inactive destinations
 * display icons, and text labels are recommended.
 * - Five destinations: Active destinations display an icon and text label. Inactive destinations
 * use icons, and use text labels if space permits.
 *
 * A LandNavigationItem always shows text labels (if it exists) when selected. Showing text
 * labels if not selected is controlled by [alwaysShowLabel].
 *
 * @param selected whether this item is selected
 * @param onClick the callback to be invoked when this item is selected
 * @param icon icon for this item, typically this will be an Icon
 * @param modifier optional [Modifier] for this item
 * @param enabled controls the enabled state of this item. When `false`, this item will not
 * be clickable and will appear disabled to accessibility services.
 * @param label optional text label for this item
 * @param alwaysShowLabel whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this LandNavigationItem. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this LandNavigationItem in different [Interaction]s.
 * @param selectedContentColor the color of the text label and icon when this item is selected,
 * and the color of the ripple.
 * @param unselectedContentColor the color of the text label and icon when this item is not selected
 */
@Composable
fun ColumnScope.LandNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {
    val styledLabel: @Composable (() -> Unit)? = label?.let {
        @Composable {
            val style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(style, content = label)
        }
    }
    // The color of the Ripple should always the selected color, as we want to show the color
    // before the item is considered selected, and hence before the new contentColor is
    // provided by LandNavigationTransition.
    val ripple = rememberRipple(bounded = false, color = selectedContentColor)

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = ripple
            )
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        LandNavigationTransition(
            selectedContentColor,
            unselectedContentColor,
            selected
        ) { progress ->
            val animationProgress = if (alwaysShowLabel) 1f else progress

            LandNavigationItemBaselineLayout(
                icon = icon,
                label = styledLabel,
                iconPositionAnimationProgress = animationProgress
            )
        }
    }
}

/**
 * Contains default values used for [LandNavigation].
 */
object LandNavigationDefaults {
    /**
     * Default elevation used for [LandNavigation].
     */
    val Elevation = 8.dp
}

/**
 * Transition that animates [LocalContentColor] between [inactiveColor] and [activeColor], depending
 * on [selected]. This component also provides the animation fraction as a parameter to [content],
 * to allow animating the position of the icon and the scale of the label alongside this color
 * animation.
 *
 * @param activeColor [LocalContentColor] when this item is [selected]
 * @param inactiveColor [LocalContentColor] when this item is not [selected]
 * @param selected whether this item is selected
 * @param content the content of the [LandNavigationItem] to animate [LocalContentColor] for,
 * where the animationProgress is the current progress of the animation from 0f to 1f.
 */
@Composable
private fun LandNavigationTransition(
    activeColor: Color,
    inactiveColor: Color,
    selected: Boolean,
    content: @Composable (animationProgress: Float) -> Unit
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = landNavigationAnimationSpec, label = ""
    )

    val color = lerp(inactiveColor, activeColor, animationProgress)

    CompositionLocalProvider(
        LocalContentColor provides color.copy(alpha = 1f),
        LocalContentAlpha provides color.alpha,
    ) {
        content(animationProgress)
    }
}

/**
 * Base layout for a [LandNavigationItem]
 *
 * @param icon icon for this item
 * @param label text label for this item
 * @param iconPositionAnimationProgress progress of the animation that controls icon position,
 * where 0 represents its unselected position and 1 represents its selected position. If both the
 * [icon] and [label] should be shown at all times, this will always be 1, as the icon position
 * should remain constant.
 */
@Composable
private fun LandNavigationItemBaselineLayout(
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)?,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    iconPositionAnimationProgress: Float
) {
    Layout(
        {
            Box(Modifier.layoutId("icon")) { icon() }
            if (label != null) {
                Box(
                    Modifier
                        .layoutId("label")
                        .alpha(iconPositionAnimationProgress)
                        .padding(horizontal = landNavigationItemHorizontalPadding)
                ) { label() }
            }
        }
    ) { measurable, constraints ->
        val iconPlaceable = measurable.first { it.layoutId == "icon" }.measure(constraints)

        val labelPlaceable = label?.let {
            measurable.first { it.layoutId == "label" }.measure(
                // Measure with loose constraints for height as we don't want the label to take up more
                // space than it needs
                constraints.copy(minHeight = 0)
            )
        }

        // If there is no label, just place the icon.
        if (label == null) {
            placeIcon(iconPlaceable, constraints)
        } else {
            placeLabelAndIcon(
                labelPlaceable!!,
                iconPlaceable,
                constraints,
                iconPositionAnimationProgress
            )
        }
    }
}

/**
 * Places the provided [iconPlaceable] in the vertical center of the provided [constraints]
 */
private fun MeasureScope.placeIcon(
    iconPlaceable: Placeable,
    constraints: Constraints
): MeasureResult {
    val height = constraints.maxHeight
    val iconY = (height - iconPlaceable.height) / 2
    return layout(iconPlaceable.width, height) {
        iconPlaceable.placeRelative(0, iconY)
    }
}

/**
 * Places the provided [labelPlaceable] and [iconPlaceable] in the correct position, depending on
 * [iconPositionAnimationProgress].
 *
 * When [iconPositionAnimationProgress] is 0, [iconPlaceable] will be placed in the center, as with
 * [placeIcon], and [labelPlaceable] will not be shown.
 *
 * When [iconPositionAnimationProgress] is 1, [iconPlaceable] will be placed near the top of item,
 * and [labelPlaceable] will be placed at the bottom of the item, according to the spec.
 *
 * When [iconPositionAnimationProgress] is animating between these values, [iconPlaceable] will be
 * placed at an interpolated position between its centered position and final resting position.
 *
 * @param labelPlaceable text label placeable inside this item
 * @param iconPlaceable icon placeable inside this item
 * @param constraints constraints of the item
 * @param iconPositionAnimationProgress the progress of the icon position animation, where 0
 * represents centered icon and no label, and 1 represents top aligned icon with label.
 * Values between 0 and 1 interpolate the icon position so we can smoothly move the icon.
 */
private fun MeasureScope.placeLabelAndIcon(
    labelPlaceable: Placeable,
    iconPlaceable: Placeable,
    constraints: Constraints,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    iconPositionAnimationProgress: Float
): MeasureResult {
    val height = constraints.maxHeight

    val firstBaseline = labelPlaceable[FirstBaseline]
    val baselineOffset = combinedItemTextBaseline.roundToPx()
    val netBaselineAdjustment = baselineOffset - firstBaseline

    val contentHeight = iconPlaceable.height + labelPlaceable.height + netBaselineAdjustment
    val contentVerticalPadding = ((height - contentHeight) / 2).coerceAtLeast(0)

    val unselectedIconY = (height - iconPlaceable.height) / 2
    // Icon should be [contentVerticalPadding] from the top

    // Label's first baseline should be [baselineOffset] below the icon
    val labelY = contentVerticalPadding + iconPlaceable.height + netBaselineAdjustment

    val containerWidth = max(labelPlaceable.width, iconPlaceable.width)

    val labelX = (containerWidth - labelPlaceable.width) / 2
    val iconX = (containerWidth - iconPlaceable.width) / 2

    // How far the icon needs to move between unselected and selected states
    val iconDistance = unselectedIconY - contentVerticalPadding

    // When selected the icon is above the unselected position, so we will animate moving
    // downwards from the selected state, so when progress is 1, the total distance is 0, and we
    // are at the selected state.
    val offset = (iconDistance * (1 - iconPositionAnimationProgress)).roundToInt()

    return layout(containerWidth, height) {
        if (iconPositionAnimationProgress != 0f) {
            labelPlaceable.placeRelative(labelX, labelY + offset)
        }
        iconPlaceable.placeRelative(iconX, contentVerticalPadding + offset)
    }
}

/**
 * [VectorizedAnimationSpec] controlling the transition between unselected and selected
 * [LandNavigationItem]s.
 */
private val landNavigationAnimationSpec = TweenSpec<Float>(
    durationMillis = 300,
    easing = FastOutSlowInEasing
)

/**
 * Height of a [LandNavigation] component
 */
private val landNavigationWidth = 66.dp

/**
 * Padding at the start and end of a [LandNavigationItem]
 */
private val landNavigationItemHorizontalPadding = 12.dp

/**
 * The space between the text baseline and the bottom of the [LandNavigationItem], and between
 * the text baseline and the bottom of the icon placed above it.
 */
private val combinedItemTextBaseline = 12.dp
