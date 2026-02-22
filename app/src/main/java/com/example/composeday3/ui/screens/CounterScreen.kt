package com.example.composeday3.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.PaddingValues

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CounterScreen(
    count: Int,
    step: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onStepUp: () -> Unit,
    onStepDown: () -> Unit,
    onResetStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Counter", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Step controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onStepDown()
                },
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-", fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary)
            }

            Text(
                text = "Mult: $step",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Button(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onStepUp()
                },
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onResetStep()
            }
        ) {
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically(animationSpec = tween(180)) { height -> height } + fadeIn(tween(180)))
                            .togetherWith(
                                slideOutVertically(animationSpec = tween(180)) { height -> -height } + fadeOut(tween(180))
                            )
                    } else {
                        (slideInVertically(animationSpec = tween(180)) { height -> -height } + fadeIn(tween(180)))
                            .togetherWith(
                                slideOutVertically(animationSpec = tween(180)) { height -> height } + fadeOut(tween(180))
                            )
                    }
                },
                label = "countSlide"
            ) { animatedCount ->
                Text(
                    text = "$animatedCount",
                    fontSize = 64.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Button(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onDecrement()
                    },
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape
                ) {
                    Text("-", fontSize = 28.sp, color = MaterialTheme.colorScheme.onPrimary)
                }

                Button(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onIncrement()
                    },
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape
                ) {
                    Text("+", fontSize = 28.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onReset()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Tap + or – ", fontSize = 14.sp)
    }
}
