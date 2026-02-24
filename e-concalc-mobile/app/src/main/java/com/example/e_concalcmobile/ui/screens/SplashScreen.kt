package com.example.e_concalcmobile.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // Animation states
    val logoScale = remember { Animatable(0.5f) }
    val logoAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val barProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Logo pop in
        launch {
            logoScale.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
        }
        logoAlpha.animateTo(1f, animationSpec = tween(500))

        delay(200)
        titleAlpha.animateTo(1f, animationSpec = tween(400))

        delay(150)
        taglineAlpha.animateTo(1f, animationSpec = tween(400))

        delay(100)
        barProgress.animateTo(1f, animationSpec = tween(1000, easing = FastOutSlowInEasing))

        delay(300)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo icon
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🖩", fontSize = 42.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                "E-Concalc",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00D4FF),
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tagline
            Text(
                "Electronic Conversion Calculator",
                fontSize = 13.sp,
                color = Color(0xFF475569),
                modifier = Modifier.alpha(taglineAlpha.value)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Loading bar
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0x14FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = barProgress.value)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
                            )
                        )
                )
            }
        }
    }
}
