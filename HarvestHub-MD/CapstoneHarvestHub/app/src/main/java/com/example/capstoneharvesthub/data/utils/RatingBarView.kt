package com.example.capstoneharvesthub.data.utils

import android.R
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBarView(
    modifier: Modifier = Modifier,
    rating: MutableState<Int> = mutableStateOf(0),
    isRatingEditable: Boolean = false,
    starSize: Dp = 25.dp,
    starsPadding: Dp = 4.dp,
    isViewAnimated: Boolean = true,
    numberOfStars: Int = 5,
    starIcon: Painter = painterResource(id = R.drawable.star_off),
    contentDescription: String = "rating",
    ratedStarsColor: Color = Color.Yellow,
    unRatedStarsColor: Color = Color.DarkGray,
) {

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) (starSize + 2.dp) else starSize,
        spring(Spring.DampingRatioLowBouncy),
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        for (star in 1..numberOfStars) {
            Icon(
                painter = starIcon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(if (isViewAnimated) size else starSize)
                    .padding(end = starsPadding)
                    .pointerInteropFilter {
                        when (isRatingEditable) {
                            true -> {
                                when (it.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        selected = true
                                        rating.value = star
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        selected = false
                                    }
                                    MotionEvent.ACTION_CANCEL -> {
                                        selected = false
                                    }
                                }
                            }
                            false -> {}
                        }
                        isRatingEditable
                    },

                tint = if (star <= rating.value) ratedStarsColor else unRatedStarsColor
            )
        }
    }
}