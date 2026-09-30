package com.example.similimumai.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.example.similimumai.ui.theme.EmeraldPrimary
import kotlin.math.sin

@Composable
fun AudioWaveformVisualizer(
    rmsLevel: Float, // Normalized 0.0 to 1.0
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
    ) {
        val barCount = 36
        val barWidth = size.width / (barCount * 1.6f)
        val spacing = (size.width - (barCount * barWidth)) / (barCount - 1)
        val midY = size.height / 2f

        for (i in 0 until barCount) {
            val progress = i.toFloat() / barCount
            val wave = sin((progress * 4 * Math.PI + phase).toDouble()).toFloat()

            val amplitude = if (isActive) {
                val base = 0.2f + (rmsLevel * 0.8f)
                (base * (0.4f + 0.6f * ((wave + 1f) / 2f)))
            } else {
                0.08f
            }

            val barHeight = (size.height * amplitude).coerceIn(4f, size.height)
            val left = i * (barWidth + spacing)
            val top = midY - (barHeight / 2f)

            drawRoundRect(
                color = if (isActive) EmeraldPrimary else EmeraldPrimary.copy(alpha = 0.25f),
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
            )
        }
    }
}
