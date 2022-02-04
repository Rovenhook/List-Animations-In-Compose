package com.example.listanimationsincompose.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listanimationsincompose.R
import com.example.listanimationsincompose.model.ShoesArticle

@ExperimentalAnimationApi
@Composable
fun ShoesCard(shoesArticle: ShoesArticle) {

    val visibilityAlpha = remember { Animatable(0f) }

    Box(
        Modifier.padding(horizontal = 16.dp).alpha(visibilityAlpha.value)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = shoesArticle.color)
                .padding(dimensionResource(id = R.dimen.slot_padding))
                .align(Alignment.CenterStart)
                .fillMaxWidth(),
        ) {
            Text(
                shoesArticle.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "$ ${shoesArticle.price}", fontSize = 14.sp, color = Color.White)
                Spacer(Modifier.width(8.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Spacer(Modifier.width(8.dp))
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(shoesArticle.width, fontSize = 14.sp, color = Color.White)
                }
            }
        }
        Image(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(dimensionResource(id = R.dimen.image_size)),
            painter = painterResource(id = shoesArticle.drawable),
            contentDescription = ""
        )
    }

    LaunchedEffect(true) {
        visibilityAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )
    }
}
