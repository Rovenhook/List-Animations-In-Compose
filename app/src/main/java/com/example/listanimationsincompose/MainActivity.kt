package com.example.listanimationsincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.listanimationsincompose.model.ShoesArticle
import com.example.listanimationsincompose.ui.ShoesCard
import com.example.listanimationsincompose.ui.theme.Blue
import com.example.listanimationsincompose.ui.theme.ListAnimationsInComposeTheme
import com.example.listanimationsincompose.ui.theme.Purple
import com.example.listanimationsincompose.ui.theme.Red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a: Job = CoroutineScope(IO).launch {

        }
        setContent {
            ListAnimationsInComposeTheme(true) {
                Home()
            }
        }
    }
}

val allShoesArticles = arrayOf(
    ShoesArticle(
        title = "Nike Air Max 270",
        price = 199.8f,
        width = "2X Wide",
        drawable = R.drawable.ic_shoes_1,
        color = Red
    ),
    ShoesArticle(
        title = "Nike Joyride Run V",
        price = 249.1f,
        width = "3X Wide",
        drawable = R.drawable.ic_shoes_2,
        color = Blue
    ),
    ShoesArticle(
        title = "Nike Space Hippie 04",
        price = 179.7f,
        width = "Extra Wide",
        drawable = R.drawable.ic_shoes_3,
        color = Purple
    )
)

//val shoesArticles = arrayListOf<ShoesArticle>()

@ExperimentalAnimationApi
@Composable
fun Home() {
    val colorsArray = arrayOf(Purple, Blue, Red)
    var particleColor by remember { mutableStateOf(Color.White) }
    val shoesArticles = remember { mutableStateListOf<ShoesArticle>() }
    var addedArticle by remember { mutableStateOf(ShoesArticle()) }
    var id by remember { mutableStateOf(0) }



    LaunchedEffect(true) {
        while (true) {
            delay(2000)
            particleColor = colorsArray.random()
            addedArticle =
                allShoesArticles.first { it.color == particleColor }.copy(id = id)
                    .also {
                        id++
                    }
            shoesArticles.add(addedArticle)
        }
    }

    Scaffold(
        topBar = {
            Box {
                TopAppBar(
                    title = {
                        Text(text = "List Animations In Compose")
                    },
                    actions = {
                        IconButton(onClick = {
                            particleColor = colorsArray.random()
                            addedArticle =
                                allShoesArticles.first { it.color == particleColor }.copy(id = id)
                                    .also {
                                        id++
                                    }
                            shoesArticles.add(addedArticle)
                        }) {
                            Icon(Icons.Filled.AddCircle, contentDescription = null)
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val offsetX = remember { mutableStateOf(0f) }
            val offsetY = remember { mutableStateOf(0f) }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ShoesList(
                    modifier = Modifier.padding(innerPadding),
                    shoesArticles = shoesArticles
                )
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            offsetX.value += dragAmount.x
                            offsetY.value += dragAmount.y
                        }
                    }
            ) {

            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ShoesList(
    modifier: Modifier,
    shoesArticles: MutableList<ShoesArticle>
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(shoesArticles.size) {
        with(lazyListState) {
            if (firstVisibleItemIndex + 10 < layoutInfo.totalItemsCount) {
                animateScrollToItem(layoutInfo.totalItemsCount - 10)
            }
            while (!isScrolledToTheEnd()) {
                animateScrollBy(2000f, tween(2000, easing = LinearEasing))
            }
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .animateContentSize(animationSpec = tween(500))
            .padding(top = dimensionResource(id = R.dimen.list_top_padding)),
        verticalArrangement = Arrangement.Bottom
    ) {
        lazyListState

        items(shoesArticles.size) { index ->
            val shoesArticle = shoesArticles.getOrNull(index)
            if (shoesArticle != null) {
//                key(shoesArticle) {
                ShoesCard(
                    shoesArticle = shoesArticle,
                )
//                }
            }
        }
    }
}

private fun LazyListState.isScrolledToTheEnd(): Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}
