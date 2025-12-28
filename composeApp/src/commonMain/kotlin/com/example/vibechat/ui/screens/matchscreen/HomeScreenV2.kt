package com.example.vibechat.ui.screens.matchscreen


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibechat.koin.ToastManager
import com.example.vibechat.koin.getDeviceInfo
import com.example.vibechat.koin.showToast
import com.example.vibechat.ui.screens.ConversationsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenV2(
    modifier: Modifier = Modifier,
    onMatchFound : () -> Unit
) {
    val viewModel = koinViewModel<RandomMatchViewModel>()
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit){
        viewModel.effect.collectLatest{ effect ->
            when(effect){
                RandomMatchSideEffect.NavigateToChatScreen -> {
                    onMatchFound()
                }
            }
        }
    }

    // Full-screen gradient background
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF181A20), // deep dark
                        Color(0xFF23242B)  // slightly lighter dark
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top area: Online label and count
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(
                    text = "User Online",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = getDeviceInfo().getDeviceId(),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }

            FrostedStartButton(
                size = 240.dp,
                isAnimating = uiState.isLoading,
                onClick = {
                    showToast("Starting Match...")
                    viewModel.handleEvent(RandomMatchEvent.OnStartMatchClick)
                }
            )
        }
    }
}

@Composable
private fun AnimatedRings(size: Dp) {
    val infinite = rememberInfiniteTransition()

    // All rings animate in sync
    val anim by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Restart
        )
    )
    val alpha1 by infinite.animateFloat(
        initialValue = 0.35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {

        Ring(
            baseSize = size,
            scale = anim,
            alpha = alpha1,
            strokeWidthDp = 4.dp
        )

        Ring(
            baseSize = size,
            scale = anim * 0.90f, // slightly smaller for spacing
            alpha = alpha1,
            strokeWidthDp = 4.dp
        )
        Ring(
            baseSize = size,
            scale = anim * 0.8f, // even smaller for more spacing
            alpha = alpha1,
            strokeWidthDp = 4.dp
        )
    }
}

@Composable
private fun Ring(
    baseSize: Dp,
    scale: Float,
    alpha: Float,
    strokeWidthDp: Dp
) {
    val sizePx = with(LocalDensity.current) { baseSize.toPx() }
    val density = LocalDensity.current.density
    Canvas(
        modifier = Modifier
            .size(baseSize * scale)
    ) {
        val strokeWidth = with(density) { strokeWidthDp.toPx() }
        val radius = min(sizePx / 2f, sizePx / 2f)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00C9FF).copy(alpha = 0.86f * alpha), // Vibrant blue
                    Color(0xFF92FE9D).copy(alpha = 0.8f * alpha)  // Soft green
                ),
                center = Offset(size.width / 2f, size.height / 2f),
                radius = radius
            ),
            radius = size.minDimension / 2f - strokeWidth / 2f,
            style = Stroke(width = strokeWidth),
            alpha = 1f
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FrostedStartButton(
    size: Dp,
    isAnimating: Boolean,
    onClick: () -> Unit
) {
    val texts = listOf("Finding Match", "Getting Vibes", "Searching Souls")
    var textIndex by remember { mutableIntStateOf(0) }


    if (isAnimating) {
        LaunchedEffect(key1 = true, key2 = textIndex) {
            delay(1800)
            textIndex = (textIndex + 1) % texts.size
        }
    } else {
        textIndex = 0
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color(0xFFB388FF).copy(alpha = 0.10f)
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Circular wave animation around text
        if (isAnimating) {
            AnimatedRings(size = size)
        }
        AnimatedContent(
            targetState = texts[textIndex],
            transitionSpec = { fadeIn().togetherWith(fadeOut()) },
            label = "buttonText"
        ) { animatedText ->
            Text(
                text = if (isAnimating) animatedText else "Start Matching",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
