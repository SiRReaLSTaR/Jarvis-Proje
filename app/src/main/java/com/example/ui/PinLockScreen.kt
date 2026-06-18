package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinLockScreen(
    viewModel: ChatViewModel,
    onUnlockSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val lang by viewModel.appLanguage.collectAsState()
    val colors = LocalSciFiColors.current

    var pinBuffer by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.94f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .size(72.dp)
                    .border(2.dp, colors.primary, CircleShape),
                shape = CircleShape,
                color = colors.primary.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Lock icon",
                        tint = colors.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = LanguageHelper.getString("app_title", lang),
                color = colors.primary,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage ?: LanguageHelper.getString("pin_enter", lang),
                color = if (errorMessage != null) Color.Red else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 4 Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                repeat(4) { index ->
                    val isFilled = index < pinBuffer.length
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.5.dp,
                                color = if (errorMessage != null) Color.Red else colors.primary,
                                shape = CircleShape
                            )
                            .background(
                                color = if (isFilled) {
                                    if (errorMessage != null) Color.Red else colors.primary
                                } else Color.Transparent
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Numeric Keyboard Pad
            Box(modifier = Modifier.width(280.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(9) { index ->
                        val num = (index + 1).toString()
                        PinButton(
                            text = num,
                            onClick = {
                                if (pinBuffer.length < 4) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    errorMessage = null
                                    pinBuffer += num
                                    if (pinBuffer.length == 4) {
                                        val ok = viewModel.unlockApp(pinBuffer)
                                        if (ok) {
                                            onUnlockSuccess()
                                        } else {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            errorMessage = LanguageHelper.getString("pin_wrong", lang)
                                            pinBuffer = ""
                                        }
                                    }
                                }
                            },
                            colors = colors
                        )
                    }

                    // CLEAR on bottom-left
                    item {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    pinBuffer = ""
                                    errorMessage = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "CLEAR",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Number 0
                    item {
                        PinButton(
                            text = "0",
                            onClick = {
                                if (pinBuffer.length < 4) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    errorMessage = null
                                    pinBuffer += "0"
                                    if (pinBuffer.length == 4) {
                                        val ok = viewModel.unlockApp(pinBuffer)
                                        if (ok) {
                                            onUnlockSuccess()
                                        } else {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            errorMessage = LanguageHelper.getString("pin_wrong", lang)
                                            pinBuffer = ""
                                        }
                                    }
                                }
                            },
                            colors = colors
                        )
                    }

                    // Backspace
                    item {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (pinBuffer.isNotEmpty()) {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        pinBuffer = pinBuffer.dropLast(1)
                                        errorMessage = null
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Backspace,
                                contentDescription = "Backspace",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PinButton(
    text: String,
    onClick: () -> Unit,
    colors: SciFiColors
) {
    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                1.dp,
                colors.primary.copy(alpha = 0.25f),
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        color = colors.surfaceVariant.copy(alpha = 0.1f),
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
