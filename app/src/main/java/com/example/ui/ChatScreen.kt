package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.haptic.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Dialog
import com.example.data.ChatMessage
import com.example.data.ChatSession
import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class SciFiColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val accent: Color,
    val userBubble: Color,
    val botBubble: Color,
    val isDark: Boolean
)

val DarkSciFiColors = SciFiColors(
    background = Color(0xFF0D111E),
    surface = Color(0xFF161C2C),
    surfaceVariant = Color(0xFF22293C),
    primary = Color(0xFF00FFCC),
    primaryVariant = Color(0xFF00B3FF),
    secondary = Color(0xFF6C63FF),
    accent = Color(0xFFFF5252),
    userBubble = Color(0xFF2A3655),
    botBubble = Color(0xFF161B2E),
    isDark = true
)

val LightSciFiColors = SciFiColors(
    background = Color(0xFFF0F4FF),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE2E9F8),
    primary = Color(0xFF008D75),
    primaryVariant = Color(0xFF006699),
    secondary = Color(0xFF4D3DFF),
    accent = Color(0xFFD32F2F),
    userBubble = Color(0xFFD2E0FB),
    botBubble = Color(0xFFE5EDFC),
    isDark = false
)

val IronManColors = SciFiColors(
    background = Color(0xFF1F0808),
    surface = Color(0xFF2E0C0C),
    surfaceVariant = Color(0xFF451111),
    primary = Color(0xFFFFD700),
    primaryVariant = Color(0xFFFF9E00),
    secondary = Color(0xFFE50914),
    accent = Color(0xFF00E5FF),
    userBubble = Color(0xFF531515),
    botBubble = Color(0xFF1D0606),
    isDark = true
)

val MatrixColors = SciFiColors(
    background = Color(0xFF010101),
    surface = Color(0xFF070B07),
    surfaceVariant = Color(0xFF0D160D),
    primary = Color(0xFF00FF41),
    primaryVariant = Color(0xFF003B00),
    secondary = Color(0xFF00B32C),
    accent = Color(0xFFFF3333),
    userBubble = Color(0xFF0A1C0B),
    botBubble = Color(0xFF010501),
    isDark = true
)

val CyberpunkColors = SciFiColors(
    background = Color(0xFF130122),
    surface = Color(0xFF1B0330),
    surfaceVariant = Color(0xFF280746),
    primary = Color(0xFFFF007F),
    primaryVariant = Color(0xFF00F0FF),
    secondary = Color(0xFF9D4EDD),
    accent = Color(0xFFFADE19),
    userBubble = Color(0xFF38085C),
    botBubble = Color(0xFF11001F),
    isDark = true
)

val SpaceCockpitColors = SciFiColors(
    background = Color(0xFF0A0F1D),
    surface = Color(0xFF121829),
    surfaceVariant = Color(0xFF1C253E),
    primary = Color(0xFF00D8F6),
    primaryVariant = Color(0xFF3B82F6),
    secondary = Color(0xFFF97316),
    accent = Color(0xFFEF4444),
    userBubble = Color(0xFF1E293B),
    botBubble = Color(0xFF0B101E),
    isDark = true
)

val LocalSciFiColors = staticCompositionLocalOf { DarkSciFiColors }

val CosmicBackground: Color
    @Composable
    get() = LocalSciFiColors.current.background

val CosmicSurface: Color
    @Composable
    get() = LocalSciFiColors.current.surface

val CosmicSurfaceVariant: Color
    @Composable
    get() = LocalSciFiColors.current.surfaceVariant

val CosmicPrimary: Color
    @Composable
    get() = LocalSciFiColors.current.primary

val CosmicPrimaryVariant: Color
    @Composable
    get() = LocalSciFiColors.current.primaryVariant

val CosmicSecondary: Color
    @Composable
    get() = LocalSciFiColors.current.secondary

val CosmicAccent: Color
    @Composable
    get() = LocalSciFiColors.current.accent

val UserBubbleColor: Color
    @Composable
    get() = LocalSciFiColors.current.userBubble

val BotBubbleColor: Color
    @Composable
    get() = LocalSciFiColors.current.botBubble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val sessions by viewModel.sessions.collectAsState()
    val currentSessionId by viewModel.currentSessionId.collectAsState()
    val currentSession by viewModel.currentSession.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val isAppLocked by viewModel.isAppLocked.collectAsState()
    val lang by viewModel.appLanguage.collectAsState()
    val isOfflineMode by viewModel.isOfflineMode.collectAsState()
    val customShortcuts by viewModel.customShortcuts.collectAsState()
    val isLockEnabled by viewModel.isLockEnabled.collectAsState()
    val appPin by viewModel.appPin.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Native Text-to-Speech (TTS) Initialization & State
    var ttsInstance by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var speakingMessageId by remember { mutableStateOf<Int?>(null) }
    var isAutoTtsEnabled by remember { mutableStateOf(false) }
    var voiceSpeechRate by remember { mutableStateOf(1.0f) }

    // Dynamic Battery Status Monitoring
    val batteryLevel = remember { mutableStateOf<Int?>(null) }
    val batteryIsCharging = remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent != null) {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    if (level >= 0 && scale > 0) {
                        batteryLevel.value = (level * 100 / scale.toFloat()).toInt()
                    }
                    val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    batteryIsCharging.value = status == BatteryManager.BATTERY_STATUS_CHARGING
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    val ttsLanguageCode by viewModel.ttsLanguageCode.collectAsState()
    val isContextCachingEnabled by viewModel.isContextCachingEnabled.collectAsState()

    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
            }
        }
        ttsInstance = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    LaunchedEffect(ttsInstance, isTtsReady, ttsLanguageCode) {
        if (isTtsReady && ttsInstance != null) {
            val finalLocale = when (ttsLanguageCode) {
                "tr" -> Locale("tr", "TR")
                "en" -> Locale.US
                "en_uk" -> Locale.UK
                "de" -> Locale.GERMANY
                "fr" -> Locale.FRANCE
                else -> Locale("tr", "TR")
            }
            ttsInstance?.language = finalLocale
        }
    }

    LaunchedEffect(ttsInstance, isTtsReady) {
        if (isTtsReady && ttsInstance != null) {
            ttsInstance?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    val msgId = utteranceId?.toIntOrNull()
                    if (msgId != null) {
                        speakingMessageId = msgId
                    }
                }

                override fun onDone(utteranceId: String?) {
                    if (speakingMessageId?.toString() == utteranceId) {
                        speakingMessageId = null
                    }
                    if (utteranceId == "WAKE_WORD_REPLY") {
                        viewModel.triggerVoiceInput()
                    }
                }

                @Deprecated("Deprecated")
                override fun onError(utteranceId: String?) {
                    if (speakingMessageId?.toString() == utteranceId) {
                        speakingMessageId = null
                    }
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    if (speakingMessageId?.toString() == utteranceId) {
                        speakingMessageId = null
                    }
                }
            })
        }
    }

    val speakMessage = remember(ttsInstance, isTtsReady, speakingMessageId, voiceSpeechRate) {
        { message: ChatMessage ->
            if (ttsInstance != null && isTtsReady) {
                if (speakingMessageId == message.id) {
                    ttsInstance?.stop()
                    speakingMessageId = null
                } else {
                    ttsInstance?.stop()
                    speakingMessageId = message.id
                    ttsInstance?.setSpeechRate(voiceSpeechRate)
                    val params = Bundle().apply {
                        putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, message.id.toString())
                    }
                    ttsInstance?.speak(message.text, TextToSpeech.QUEUE_FLUSH, params, message.id.toString())
                }
            } else {
                Toast.makeText(context, "Ses motoru yükleniyor, lütfen bekleyin...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Auto-TTS execution on incoming new model assistant messages
    var lastProcessedAutoTtsId by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(messages, isAutoTtsEnabled, isTtsReady) {
        if (isAutoTtsEnabled && isTtsReady && messages.isNotEmpty()) {
            val latestMessage = messages.last()
            if (latestMessage.role == "model" && latestMessage.id != lastProcessedAutoTtsId) {
                lastProcessedAutoTtsId = latestMessage.id
                speakMessage(latestMessage)
            }
        }
    }

    // Dialog state for settings, renaming, info
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf<ChatSession?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf<ChatSession?>(null) }
    var showResetMessagesConfirm by remember { mutableStateOf(false) }
    var showStatsOverlay by remember { mutableStateOf(false) }
    var showCommandPaletteDialog by remember { mutableStateOf(false) }

    // Pre-made instructions templates
    val systemInstructs = listOf(
        Pair("Standart Asistan", "Sen kullanıcıya her konuda yardımcı olan, cana yakın ve çok zeki bir yapay zeka asistanısın."),
        Pair("Yazılım/Kodlama Uzmanı", "Sen uzman bir yazılım mühendisisin. Temiz, optimize ve modern kod standartlarına uygun çözümler ve örnek kod taslakları sun. Teknik detayları net açıkla."),
        Pair("İngilizce Pratiği", "Sen cana yakın bir İngilizce öğretmenisin. Kullanıcıyla İngilizce konuş, hatalarını düzelt ve Türkçe açıklamalarla destekleyerek kelime dağarcığını zenginleştir."),
        Pair("Seyahat Rehberi", "Sen dünyayı gezmiş deneyimli bir seyahat rehberi ve gurmesin. Rotalar, gezilecek yerler, yerel lezzetler ve kültürel ipuçları ver.")
    )

    // Cosmic theme dynamic selection
    val currentIsDarkMode by viewModel.isDarkMode.collectAsState()
    val currentThemePreset by viewModel.themePreset.collectAsState()

    val sciFiColors = when (currentThemePreset) {
        "Iron Man (Kırmızı/Altın)" -> IronManColors
        "Matrix (Siyah/Yeşil)" -> MatrixColors
        "Cyberpunk (Neon Pembe/Mavi)" -> CyberpunkColors
        "Uzay Gemisi Kokpiti" -> SpaceCockpitColors
        "Cosmic Light" -> LightSciFiColors
        else -> DarkSciFiColors
    }

    val cosmicColorScheme = if (sciFiColors.isDark) {
        darkColorScheme(
            primary = CosmicPrimary,
            onPrimary = Color.Black,
            secondary = CosmicSecondary,
            background = CosmicBackground,
            surface = CosmicSurface,
            onBackground = Color.White,
            onSurface = Color.White,
            surfaceVariant = CosmicSurfaceVariant,
            onSurfaceVariant = Color.LightGray
        )
    } else {
        lightColorScheme(
            primary = CosmicPrimary,
            onPrimary = Color.White,
            secondary = CosmicSecondary,
            background = CosmicBackground,
            surface = CosmicSurface,
            onBackground = Color(0xFF0F172A),
            onSurface = Color(0xFF0F172A),
            surfaceVariant = CosmicSurfaceVariant,
            onSurfaceVariant = Color(0xFF334155)
        )
    }

    CompositionLocalProvider(LocalSciFiColors provides sciFiColors) {
        MaterialTheme(colorScheme = cosmicColorScheme) {
            BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val isWideScreen = maxWidth > 650.dp

            if (isWideScreen) {
                // Landscape split view (Sidebar + Active Chat content side by side)
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                            .background(CosmicSurface)
                    ) {
                        SidebarContent(
                            viewModel = viewModel,
                            onSessionSelected = { id -> viewModel.selectSession(id) },
                            onNewChatClicked = { viewModel.createNewSession() },
                            onRenameClicked = { s -> showRenameDialog = s },
                            onDeleteClicked = { s -> showDeleteConfirmDialog = s },
                            isApiKeyConfigured = viewModel.isApiKeyConfigured
                        )
                    }

                    // Vertical divider with faint glow
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(CosmicPrimary, CosmicSecondary)
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        ChatArea(
                            viewModel = viewModel,
                            ttsInstance = ttsInstance,
                            isTtsReady = isTtsReady,
                            currentSession = currentSession,
                            messages = messages,
                            isLoading = isLoading,
                            errorMessage = errorMessage,
                            onSendMessage = { txt -> viewModel.sendMessage(txt) },
                            onSettingsClicked = { showSettingsDialog = true },
                            onMenuClicked = { /* No-op in wide screen */ },
                            onClearClicked = { showResetMessagesConfirm = true },
                            showMenuIcon = false,
                            isApiKeyConfigured = viewModel.isApiKeyConfigured,
                            speakingMessageId = speakingMessageId,
                            onSpeakClicked = speakMessage,
                            onStatsClicked = { showStatsOverlay = true }
                        )
                    }
                }
            } else {
                // Mobile view using standard Drawer sliding component
                val gesturesEnabled = drawerState.isOpen || drawerState.isClosed
                ModalNavigationDrawer(
                    gesturesEnabled = gesturesEnabled,
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = CosmicSurface,
                            modifier = Modifier.width(300.dp)
                        ) {
                            SidebarContent(
                                viewModel = viewModel,
                                onSessionSelected = { id ->
                                    viewModel.selectSession(id)
                                    scope.launch { drawerState.close() }
                                },
                                onNewChatClicked = {
                                    viewModel.createNewSession()
                                    scope.launch { drawerState.close() }
                                },
                                onRenameClicked = { s -> showRenameDialog = s },
                                onDeleteClicked = { s -> showDeleteConfirmDialog = s },
                                isApiKeyConfigured = viewModel.isApiKeyConfigured
                            )
                        }
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
                        ChatArea(
                            viewModel = viewModel,
                            ttsInstance = ttsInstance,
                            isTtsReady = isTtsReady,
                            currentSession = currentSession,
                            messages = messages,
                            isLoading = isLoading,
                            errorMessage = errorMessage,
                            onSendMessage = { txt -> viewModel.sendMessage(txt) },
                            onSettingsClicked = { showSettingsDialog = true },
                            onMenuClicked = { scope.launch { drawerState.open() } },
                            onClearClicked = { showResetMessagesConfirm = true },
                            showMenuIcon = true,
                            isApiKeyConfigured = viewModel.isApiKeyConfigured,
                            speakingMessageId = speakingMessageId,
                            onSpeakClicked = speakMessage,
                            onStatsClicked = { showStatsOverlay = true }
                        )
                    }
                }
            }

            // --- DIALOGS SECTION ---

            // 1. Settings Dialog
            if (showSettingsDialog && currentSession != null) {
                var selectedModel by remember { mutableStateOf(currentSession!!.model) }
                var tempVal by remember { mutableStateOf(currentSession!!.temperature) }
                var customInstruction by remember { mutableStateOf(currentSession!!.systemInstruction) }

                val personalities = listOf(
                    Triple("Standart Asistan", "Sen kullanıcıya her konuda yardımcı olan, cana yakın ve çok zeki bir yapay zeka asistanısın.", Icons.Filled.Face),
                    Triple("Formal (Resmi)", "Sen her zaman resmi, son derece profesyonel, ciddi ve kibar bir üslup kullanan bir yapay zeka asistanısın. Yanıtlarında kurumsal dile sadık kal, deyim ve mecazlardan kaçın, teknik terimleri doğru kullan ve net, mantıklı açıklamalar yap.", Icons.Filled.Business),
                    Triple("Concise (Kısa ve Net)", "Sen her zaman son derece kısa, net, pratik ve doğrudan sonuca giden yanıtlar veren bir yapay zeka asistanısın. Sadece sorulan sorunun tam cevabını ver. Gereksiz hiçbir ayrıntı, açıklama veya yan bilgi ekleme.", Icons.Filled.Bolt),
                    Triple("Creative (Yaratıcı)", "Sen her zaman yaratıcı, fantastik, sanatsal ve ilham verici düşünen, hayal gücü geniş bir yapay zeka asistanısın. Yanıtlarında bolca benzetme, hikayeleştirme ve orijinal bakış açıları barındır. Şiirsel veya edebi dokunuşlar ekle.", Icons.Filled.Lightbulb),
                    Triple("Futuristic Android (Siber)", "Sen fütüristik bir uzay gemisi yapay zekası ya da sibernetik bir androidsin. Kod adın NEURAL LINK. Yanıtlarında yüksek teknoloji terimleri, siber-sistem durumu raporları ve fütüristik semboller kullan.", Icons.Filled.Build)
                )

                var dropdownExpanded by remember { mutableStateOf(false) }
                val selectedPersonality = personalities.find { it.second == customInstruction }
                val selectedName = selectedPersonality?.first ?: "Özel Karakter / Serbest"

                Dialog(onDismissRequest = { showSettingsDialog = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(1.dp, CosmicPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Asistan Tweak Paneli",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicPrimary
                                    )
                                )
                                IconButton(onClick = { showSettingsDialog = false }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Kapat", tint = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Model selection section
                            Text(
                                text = "Yapay Zeka Modeli",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimaryVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CosmicSurface)
                            ) {
                                val models = listOf(
                                    Pair("gemini-3.5-flash", "Hızlı (Flash)"),
                                    Pair("gemini-3.1-pro-preview", "Gelişmiş (Pro)")
                                )
                                models.forEach { (modelId, label) ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedModel = modelId }
                                            .background(if (selectedModel == modelId) CosmicSecondary else Color.Transparent)
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            color = if (selectedModel == modelId) Color.White else Color.Gray,
                                            fontWeight = if (selectedModel == modelId) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Temperature tuning
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Yaratıcılık Derecesi (Temp)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CosmicPrimaryVariant
                                )
                                Text(
                                    text = String.format(Locale.US, "%.1f", tempVal),
                                    color = CosmicPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Slider(
                                value = tempVal,
                                onValueChange = { tempVal = it },
                                valueRange = 0.0f..2.0f,
                                steps = 20,
                                modifier = Modifier.testTag("temp_slider"),
                                colors = SliderDefaults.colors(
                                    thumbColor = CosmicPrimary,
                                    activeTrackColor = CosmicPrimary,
                                    inactiveTrackColor = Color.Gray
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Doğrucu/Teknik", fontSize = 11.sp, color = Color.Gray)
                                Text("Dengeli", fontSize = 11.sp, color = Color.Gray)
                                Text("Yaratıcı/Uydurma", fontSize = 11.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // --- FUTURISTIC PERSONALITY DROPDOWN MENU ---
                            Text(
                                text = "Sohbet Kişiliği / Karakteri (Personality)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(CosmicSurface)
                                        .border(1.dp, CosmicPrimary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                        .clickable { dropdownExpanded = true }
                                        .padding(horizontal = 14.dp, vertical = 12.dp)
                                        .testTag("personality_dropdown_trigger"),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = selectedPersonality?.third ?: Icons.Filled.Face,
                                            contentDescription = "Personality Icon",
                                            tint = CosmicPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = selectedName,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Icon(
                                        imageVector = if (dropdownExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = "Seçenekleri Göster",
                                        tint = CosmicPrimaryVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.85f)
                                        .background(CosmicSurfaceVariant)
                                        .border(1.dp, CosmicPrimary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                ) {
                                    personalities.forEach { (name, prompt, icon) ->
                                        val isThisSelected = customInstruction == prompt
                                        DropdownMenuItem(
                                            text = {
                                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                                    Text(
                                                        text = name,
                                                        color = if (isThisSelected) CosmicPrimary else Color.White,
                                                        fontWeight = if (isThisSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                        fontSize = 13.sp
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = if (prompt.length > 90) prompt.substring(0, 87) + "..." else prompt,
                                                        color = Color.LightGray.copy(alpha = 0.8f),
                                                        fontSize = 10.sp,
                                                        lineHeight = 14.sp
                                                    )
                                                }
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = name,
                                                    tint = if (isThisSelected) CosmicPrimary else CosmicPrimaryVariant.copy(alpha = 0.8f),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            },
                                            onClick = {
                                                customInstruction = prompt
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Default Prompts (System Instructions templates)
                            Text(
                                text = "Karakter/Talimat Hazır Şablonları",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimaryVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(bottom = 8.dp)
                            ) {
                                items(systemInstructs) { (label, prompt) ->
                                    SuggestionChip(
                                        onClick = { customInstruction = prompt },
                                        label = { Text(label, fontSize = 12.sp) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = if (customInstruction == prompt) CosmicSecondary else CosmicSurface,
                                            labelColor = if (customInstruction == prompt) Color.White else Color.LightGray
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // System instructions input
                            OutlinedTextField(
                                value = customInstruction,
                                onValueChange = { customInstruction = it },
                                label = { Text("Sistem Rolü / Talimatlar (System Instruction)") },
                                placeholder = { Text("Modelin nasıl davranması gerektiğini girin (ör: İngilizce konuş, kısa cevap ver)...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .testTag("system_instruction_input"),
                                textStyle = TextStyle(fontSize = 13.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CosmicPrimary,
                                    unfocusedBorderColor = Color.Gray,
                                    focusedLabelColor = CosmicPrimary,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // --- AUTO READ RESPONSE TTS OPTION ---
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(CosmicSurface)
                                    .border(1.dp, CosmicPrimary.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                    .clickable { isAutoTtsEnabled = !isAutoTtsEnabled }
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                                    .testTag("auto_tts_row"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.VolumeUp,
                                        contentDescription = "Voice Assistant",
                                        tint = CosmicPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Sesli Yanıt Kilidi (Otomatik Oku)",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "Asistandan gelen yeni mesajları sesli oku",
                                            color = Color.LightGray.copy(alpha = 0.6f),
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                                Switch(
                                    checked = isAutoTtsEnabled,
                                    onCheckedChange = { isAutoTtsEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = CosmicPrimary,
                                        checkedTrackColor = CosmicPrimary.copy(alpha = 0.4f),
                                        uncheckedThumbColor = Color.DarkGray,
                                        uncheckedTrackColor = CosmicSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag("auto_tts_switch")
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // --- VOICE SPEED CONTROL ---
                            Text(
                                text = "Ses Okuma Hızı (Voice Speed)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CosmicSurface)
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val speeds = listOf(
                                    Pair(0.75f, "0.75x"),
                                    Pair(1.0f, "Normal"),
                                    Pair(1.25f, "1.25x"),
                                    Pair(1.5f, "1.5x")
                                )
                                speeds.forEach { (speedValue, label) ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { voiceSpeechRate = speedValue }
                                                .background(if (voiceSpeechRate == speedValue) CosmicSecondary else Color.Transparent, RoundedCornerShape(6.dp))
                                                .padding(vertical = 8.dp, horizontal = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                color = if (voiceSpeechRate == speedValue) Color.White else Color.Gray,
                                                fontWeight = if (voiceSpeechRate == speedValue) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 11.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Sistem ses üretim hızı: ${voiceSpeechRate}x",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CosmicPrimaryVariant.copy(alpha = 0.7f),
                                modifier = Modifier.align(Alignment.End)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // --- TTS VOICE SELECTION ---
                            Text(
                                text = "TTS Ses ve Dil Seçimi (Voice Selection)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CosmicSurface)
                                    .padding(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val voices = listOf(
                                    Pair("tr", "Türkçe 🇹🇷"),
                                    Pair("en", "English US 🇺🇸"),
                                    Pair("en_uk", "English UK 🇬🇧"),
                                    Pair("de", "Deutsch 🇩🇪"),
                                    Pair("fr", "Français 🇫🇷")
                                )
                                voices.chunked(3).forEach { rowList ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        rowList.forEach { (langCode, langLabel) ->
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .clickable { viewModel.setTtsLanguageCode(langCode) }
                                                    .background(if (ttsLanguageCode == langCode) CosmicSecondary else Color.Transparent)
                                                    .padding(vertical = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = langLabel,
                                                    color = if (ttsLanguageCode == langCode) Color.White else Color.Gray,
                                                    fontWeight = if (ttsLanguageCode == langCode) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                        if (rowList.size < 3) {
                                            for (i in 1..(3 - rowList.size)) {
                                                Box(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // --- SMART CONTEXT CACHE ACCELERATION ---
                            Text(
                                text = "Akıllı Algoritmik Önbellekleme (Token Caching)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CosmicSurface)
                                    .clickable { viewModel.setContextCachingEnabled(!isContextCachingEnabled) }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Smart Context Cache (Akıllı Önbellek)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "10+ üzeri mesajlarda eski geçmişi otomatik optimize eder (Token Tasarrufu sağlar).",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                                Switch(
                                    checked = isContextCachingEnabled,
                                    onCheckedChange = { viewModel.setContextCachingEnabled(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = CosmicPrimary,
                                        checkedTrackColor = CosmicPrimary.copy(alpha = 0.4f),
                                        uncheckedThumbColor = Color.Gray,
                                        uncheckedTrackColor = CosmicSurfaceVariant
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // --- GESTURES & HAPTICS INFOGRAPH ---
                            Text(
                                text = "Hızlı Gestures & Dokunsal Geri Bildirimi",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CosmicSurface)
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.TouchApp,
                                        contentDescription = null,
                                        tint = CosmicPrimaryVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Çift Dokunma (Double Tap): Mesajı kopyalar + Haptic titreşim.",
                                        fontSize = 11.sp,
                                        color = Color.LightGray
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.VolumeUp,
                                        contentDescription = null,
                                        tint = CosmicPrimaryVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Uzun Dokunma (Long Press): Mesajı seslendirir / sessize alır.",
                                        fontSize = 11.sp,
                                        color = Color.LightGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            HorizontalDivider(color = CosmicPrimary.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(16.dp))

                            // 1. MULTI-LANGUAGE SECTION
                            Text(
                                text = LanguageHelper.getString("theme_selection", lang).replace("Tema", "Dili").replace("Theme", "Language"),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimaryVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.setAppLanguage("tr") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (lang == "tr") CosmicPrimary else CosmicSurfaceVariant.copy(alpha = 0.3f)
                                    ),
                                    border = BorderStroke(1.dp, if (lang == "tr") CosmicPrimary else Color.Gray),
                                    modifier = Modifier.weight(1f).testTag("lang_tr_button")
                                ) {
                                    Text("TÜRKÇE (TR)", color = if (lang == "tr") Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { viewModel.setAppLanguage("en") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (lang == "en") CosmicPrimary else CosmicSurfaceVariant.copy(alpha = 0.3f)
                                    ),
                                    border = BorderStroke(1.dp, if (lang == "en") CosmicPrimary else Color.Gray),
                                    modifier = Modifier.weight(1f).testTag("lang_en_button")
                                ) {
                                    Text("ENGLISH (EN)", color = if (lang == "en") Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 2. OFFLINE MODE SECTION
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = LanguageHelper.getString("offline_mode", lang),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CosmicPrimaryVariant
                                    )
                                    Text(
                                        text = LanguageHelper.getString("offline_desc", lang),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.LightGray
                                    )
                                }
                                Switch(
                                    checked = isOfflineMode,
                                    onCheckedChange = { viewModel.setOfflineMode(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = CosmicPrimary,
                                        checkedTrackColor = CosmicPrimary.copy(alpha = 0.4f)
                                    ),
                                    modifier = Modifier.testTag("offline_mode_switch")
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 3. AUTO LOCK SECTION
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = LanguageHelper.getString("autolock_title", lang),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CosmicPrimaryVariant
                                    )
                                    Text(
                                        text = LanguageHelper.getString("autolock_desc", lang),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.LightGray
                                    )
                                }
                                Switch(
                                    checked = isLockEnabled,
                                    onCheckedChange = { viewModel.setLockEnabled(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = CosmicPrimary,
                                        checkedTrackColor = CosmicPrimary.copy(alpha = 0.4f)
                                    ),
                                    modifier = Modifier.testTag("autolock_switch")
                                )
                            }

                            if (isLockEnabled) {
                                Spacer(modifier = Modifier.height(10.dp))
                                var pinTextState by remember { mutableStateOf(appPin) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, CosmicPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = pinTextState,
                                        onValueChange = {
                                            if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                                                pinTextState = it
                                            }
                                        },
                                        label = { Text(LanguageHelper.getString("pin_set", lang), color = Color.Gray, fontSize = 11.sp) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = CosmicPrimary,
                                            unfocusedBorderColor = Color.Gray,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                        ),
                                        modifier = Modifier.weight(1f).height(56.dp).testTag("pin_input_field")
                                    )
                                    Button(
                                        onClick = {
                                            if (pinTextState.length == 4) {
                                                viewModel.setAppPin(pinTextState)
                                                Toast.makeText(context, LanguageHelper.getString("pin_enabled", lang), Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "PIN 4 hane olmalıdır!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CosmicPrimaryVariant),
                                        modifier = Modifier.testTag("save_pin_button")
                                    ) {
                                        Text(LanguageHelper.getString("save", lang), color = Color.Black)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 4. CUSTOM SHORTCUTS EDITOR
                            Text(
                                text = LanguageHelper.getString("shortcuts_title", lang),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = CosmicPrimaryVariant
                            )
                            Text(
                                text = LanguageHelper.getString("shortcuts_desc", lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Display current shortcuts with delete option
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, CosmicPrimary.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                    .background(Color.Black.copy(alpha = 0.2f))
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                customShortcuts.forEach { shortcut ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "• " + if (shortcut.length > 32) shortcut.take(30) + "..." else shortcut,
                                            color = Color.LightGray,
                                            fontSize = 12.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(
                                            onClick = { viewModel.removeCustomShortcut(shortcut) },
                                            modifier = Modifier.size(24.dp).testTag("delete_shortcut_${shortcut.take(5)}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = "Sil",
                                                tint = Color.Red,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                                if (customShortcuts.isEmpty()) {
                                    Text("List is empty / Liste boş.", color = Color.DarkGray, fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Add shortcut form
                            var newShortcutText by remember { mutableStateOf("") }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = newShortcutText,
                                    onValueChange = { newShortcutText = it },
                                    placeholder = { Text("Yeni kısayol yazın...", color = Color.Gray, fontSize = 12.sp) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CosmicPrimary,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f).height(48.dp).testTag("new_shortcut_input")
                                )
                                Button(
                                    onClick = {
                                        if (newShortcutText.trim().isNotEmpty()) {
                                            viewModel.addCustomShortcut(newShortcutText.trim())
                                            newShortcutText = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CosmicPrimary),
                                    modifier = Modifier.height(40.dp).testTag("add_shortcut_button")
                                ) {
                                    Text(LanguageHelper.getString("add", lang), color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            TextButton(
                                onClick = { viewModel.resetShortcuts() },
                                modifier = Modifier.align(Alignment.End).testTag("reset_shortcuts_button")
                            ) {
                                Text("Fabrika Ayarlarına Sıfırla / Reset Default", color = CosmicPrimary, fontSize = 11.sp)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { showSettingsDialog = false }) {
                                    Text("İptal", color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        viewModel.updateSessionConfigs(selectedModel, tempVal, customInstruction)
                                        showSettingsDialog = false
                                        Toast.makeText(context, "Asistan ayarları güncellendi!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CosmicPrimary),
                                    modifier = Modifier.testTag("save_settings_button")
                                ) {
                                    Text("Kaydet", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Rename Chat Dialog
            if (showRenameDialog != null) {
                var newTitle by remember { mutableStateOf(showRenameDialog!!.title) }
                AlertDialog(
                    onDismissRequest = { showRenameDialog = null },
                    containerColor = CosmicSurfaceVariant,
                    title = { Text("Sohbeti Yeniden Adlandır", color = CosmicPrimary) },
                    text = {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Sohbet Başlığı") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("rename_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CosmicPrimary,
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newTitle.trim().isNotEmpty()) {
                                    viewModel.renameSession(showRenameDialog!!.id, newTitle.trim())
                                }
                                showRenameDialog = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CosmicPrimary)
                        ) {
                            Text("Giriş", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showRenameDialog = null }) {
                            Text("Vazgeç", color = Color.LightGray)
                        }
                    }
                )
            }

            // 3. Delete Session Confirm Dialog
            if (showDeleteConfirmDialog != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmDialog = null },
                    modifier = Modifier.testTag("delete_session_dialog"),
                    containerColor = CosmicSurfaceVariant,
                    icon = { Icon(Icons.Filled.Delete, contentDescription = null, tint = CosmicAccent) },
                    title = { Text("Sohbeti Sil?", color = Color.White) },
                    text = { Text("Bu sohbet oturumu ve içindeki tüm yazışmalar kalıcı olarak silinecektir. Emin misiniz?", color = Color.LightGray) },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDeleteConfirmDialog?.let { viewModel.deleteSession(it) }
                                showDeleteConfirmDialog = null
                            },
                            modifier = Modifier.testTag("dialog_delete_confirm_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CosmicAccent)
                        ) {
                            Text("Sil", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteConfirmDialog = null },
                            modifier = Modifier.testTag("dialog_delete_dismiss_button")
                        ) {
                            Text("İptal", color = Color.LightGray)
                        }
                    }
                )
            }

            // 4. Reset messages confirmation (Clear History)
            if (showResetMessagesConfirm) {
                AlertDialog(
                    onDismissRequest = { showResetMessagesConfirm = false },
                    modifier = Modifier.testTag("clear_history_dialog"),
                    containerColor = CosmicSurfaceVariant,
                    icon = { Icon(Icons.Filled.ClearAll, contentDescription = null, tint = CosmicAccent) },
                    title = { Text("Geçmişi Temizle (Clear History)?", color = Color.White) },
                    text = { Text("Bu sohbete ait tüm mesaj geçmişi kalıcı olarak silinecektir. Emin misiniz? Bu işlem geri alınamaz.", color = Color.LightGray) },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.clearActiveMessages()
                                showResetMessagesConfirm = false
                            },
                            modifier = Modifier.testTag("dialog_clear_confirm_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CosmicAccent)
                        ) {
                            Text("Temizle", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showResetMessagesConfirm = false },
                            modifier = Modifier.testTag("dialog_clear_dismiss_button")
                        ) {
                            Text("İptal", color = Color.LightGray)
                        }
                    }
                )
            }

            // 5. Statistics Dashboard (Jarvis Overlay)
            if (showStatsOverlay) {
                JarvisStatsOverlay(
                    sessionsCount = sessions.size,
                    totalMessages = viewModel.totalMessageCount.collectAsState().value,
                    userMessages = viewModel.userMessageCount.collectAsState().value,
                    modelMessages = viewModel.modelMessageCount.collectAsState().value,
                    sentiment = viewModel.activeSessionSentiment.collectAsState().value,
                    isWakeWordEnabled = viewModel.isWakeWordEnabled.collectAsState().value,
                    wakeWord = viewModel.wakeWord.collectAsState().value,
                    apiCallsRpm = viewModel.apiCallsRpm.collectAsState().value,
                    onWakeWordToggle = { enabled -> viewModel.setWakeWordEnabled(enabled) },
                    onWakeWordChange = { word -> viewModel.setWakeWord(word) },
                    onDismiss = { showStatsOverlay = false }
                )
            }

            // 6. Interactive Command Palette Modal Dialog
            if (showCommandPaletteDialog) {
                var searchFilter by remember { mutableStateOf("") }
                val haptic = LocalHapticFeedback.current
                Dialog(onDismissRequest = { showCommandPaletteDialog = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(1.dp, CosmicPrimary, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Terminal,
                                        contentDescription = null,
                                        tint = CosmicPrimary
                                    )
                                    Text(
                                        text = "J.A.R.V.I.S Komut Merkezi",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                IconButton(onClick = { showCommandPaletteDialog = false }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Kapat", tint = Color.Gray)
                                }
                            }

                            // Fuzzy Search Input Bar
                            OutlinedTextField(
                                value = searchFilter,
                                onValueChange = { searchFilter = it },
                                placeholder = { Text("Komut ara (ör. theme, clear, stats)...", color = Color.Gray, fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                trailingIcon = {
                                    if (searchFilter.isNotEmpty()) {
                                        IconButton(onClick = { searchFilter = "" }) {
                                            Icon(Icons.Filled.Clear, contentDescription = "Temizle", tint = Color.Gray)
                                        }
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CosmicPrimary,
                                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                                    focusedContainerColor = CosmicBackground,
                                    unfocusedContainerColor = CosmicBackground
                                )
                            )

                            // Command list with rich visual panels
                            val dialogCommands = listOf(
                                Triple("new", "Yeni Sohbet Başlat", "Yeni boş bir oturum oluşturur"),
                                Triple("theme", "Temayı Değiştir", "Karanlık/Aydınlık mod geçişi yapar"),
                                Triple("clear", "Aktif Mesajları Temizle", "Geçmiş sohbet verilerini siler"),
                                Triple("stats", "Neural Link Overlay", "Performans ve RPM panelini açar"),
                                Triple("voice", "Wake Word Uyandırma", "Sesli dinleme motorunu aç/kapat yapar"),
                                Triple("setting", "Detaylı Ayarlar", "Asistan model ve konfigurasyonunu açar"),
                                Triple("export", "Geçmişi Dışa Aktar", "Sohbet içeriklerini kopyalar ve paylaşır")
                            ).filter {
                                it.first.contains(searchFilter.lowercase()) ||
                                it.second.lowercase().contains(searchFilter.lowercase())
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 280.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (dialogCommands.isEmpty()) {
                                    item {
                                        Text(
                                            text = "Aramanızla eşleşen hiçbir sistem komutu bulunamadı.",
                                            color = Color.Gray,
                                            fontSize = 11.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp)
                                        )
                                    }
                                } else {
                                    items(dialogCommands) { (name, title, desc) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    showCommandPaletteDialog = false
                                                    when (name) {
                                                        "new" -> viewModel.createNewSession()
                                                        "theme" -> viewModel.toggleTheme()
                                                        "clear" -> viewModel.clearActiveMessages()
                                                        "stats" -> showStatsOverlay = true
                                                        "voice" -> viewModel.setWakeWordEnabled(!viewModel.isWakeWordEnabled.value)
                                                        "setting" -> showSettingsDialog = true
                                                        "export" -> {
                                                            val currentSession = viewModel.currentSession.value
                                                            val exportStr = buildString {
                                                                appendLine("# J.A.R.V.I.S SOHBET GEÇMİŞİ")
                                                                appendLine("Sohbet Başlığı: ${currentSession?.title ?: ""}")
                                                                appendLine("Model: ${currentSession?.model ?: ""}")
                                                                appendLine("---")
                                                                messages.forEach { msg ->
                                                                    val sender = if (msg.role == "user") "KULLANICI" else "J.A.R.V.I.S"
                                                                    appendLine("### [$sender] - ${msg.timestamp}")
                                                                    appendLine(msg.text)
                                                                    appendLine()
                                                                }
                                                            }
                                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                            val clip = android.content.ClipData.newPlainText("JARVIS Chat Export", exportStr)
                                                            clipboard.setPrimaryClip(clip)
                                                            Toast.makeText(context, "Sohbet dökümü kopyalandı!", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }
                                                .background(CosmicSurface)
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Terminal,
                                                contentDescription = null,
                                                tint = CosmicPrimaryVariant,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "/$name — $title",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                                Text(
                                                    text = desc,
                                                    fontSize = 10.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Filled.ChevronRight,
                                                contentDescription = null,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isAppLocked) {
                PinLockScreen(
                    viewModel = viewModel,
                    onUnlockSuccess = {}
                )
            }
        }
    }
}

// Left Sidebar / Navigation contents
@Composable
fun SidebarContent(
    viewModel: ChatViewModel,
    onSessionSelected: (Int) -> Unit,
    onNewChatClicked: () -> Unit,
    onRenameClicked: (ChatSession) -> Unit,
    onDeleteClicked: (ChatSession) -> Unit,
    isApiKeyConfigured: Boolean,
    modifier: Modifier = Modifier
) {
    val sessions by viewModel.sessions.collectAsState()
    val currentSessionId by viewModel.currentSessionId.collectAsState()
    val globalSearchQuery by viewModel.globalSearchQuery.collectAsState()
    val globalSearchResults by viewModel.globalSearchResults.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp, start = 12.dp, end = 12.dp)
    ) {
        // App header containing decorative neon lines
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(CosmicPrimary, CosmicSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = "Cosmic Asistan Logo",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Akıllı Asistan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Gemini AI Engine",
                    fontSize = 11.sp,
                    color = CosmicPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // New Chat Button
        OutlinedButton(
            onClick = onNewChatClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("new_chat_button"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CosmicPrimary),
            border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(CosmicPrimary, CosmicSecondary))),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = CosmicPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Yeni Sohbet", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Futuristic Search Chat History Bar (Global Search)
        OutlinedTextField(
            value = globalSearchQuery,
            onValueChange = { viewModel.setGlobalSearchQuery(it) },
            placeholder = { Text("Maziye göz at...", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Ara",
                    tint = CosmicPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            },
            trailingIcon = {
                if (globalSearchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.setGlobalSearchQuery("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Temizle",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("chat_history_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = CosmicSurfaceVariant.copy(alpha = 0.6f),
                unfocusedContainerColor = CosmicSurfaceVariant.copy(alpha = 0.3f),
                focusedBorderColor = CosmicPrimary.copy(alpha = 0.6f),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.2f)
            ),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(fontSize = 12.sp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Session Listing and matching filters
        val filteredSessions = remember(sessions, globalSearchQuery) {
            if (globalSearchQuery.trim().isEmpty()) {
                sessions
            } else {
                sessions.filter { it.title.contains(globalSearchQuery, ignoreCase = true) }
            }
        }

        if (globalSearchQuery.trim().isEmpty()) {
            Text(
                text = "YAZIŞMA GEÇMİŞİ",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
        }

        if (filteredSessions.isEmpty() && globalSearchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (sessions.isEmpty()) {
                        "Oturum bulunamadı.\nYeni sohbet başlatın."
                    } else {
                        "Aramayla eşleşen\nsonuç bulunamadı."
                    },
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (globalSearchQuery.trim().isNotEmpty() && filteredSessions.isNotEmpty()) {
                    item {
                        Text(
                            text = "EŞLEŞEN OTURUMLAR",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicSecondary,
                            letterSpacing = 1.0.sp,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                        )
                    }
                }

                items(filteredSessions, key = { it.id }) { session ->
                    val isSelected = currentSessionId == session.id
                    val itemBg = if (isSelected) CosmicSurfaceVariant else Color.Transparent
                    val borderLineColor = if (isSelected) CosmicPrimary.copy(alpha = 0.5f) else Color.Transparent

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(itemBg)
                            .border(1.dp, borderLineColor, RoundedCornerShape(10.dp))
                            .clickable { onSessionSelected(session.id) }
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Filled.ChatBubble else Icons.Filled.ChatBubbleOutline,
                            contentDescription = null,
                            tint = if (isSelected) CosmicPrimary else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = session.title,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (session.model.contains("pro")) "Gemini Pro" else "Gemini Flash",
                                    fontSize = 10.sp,
                                    color = if (isSelected) CosmicPrimary.copy(alpha = 0.8f) else Color.Gray
                                )
                                val dateStr = remember(session.createdAt) {
                                    try {
                                        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale("tr"))
                                        sdf.format(Date(session.createdAt))
                                    } catch (e: Exception) {
                                        ""
                                    }
                                }
                                Text(
                                    text = dateStr,
                                    fontSize = 9.sp,
                                    color = Color.Gray.copy(alpha = 0.6f)
                                )
                            }
                        }

                        IconButton(
                            onClick = { onRenameClicked(session) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Sohbeti Düzenle",
                                tint = if (isSelected) Color.LightGray else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = { onDeleteClicked(session) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Sohbeti Sil",
                                tint = if (isSelected) CosmicAccent.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Global search message results
                if (globalSearchQuery.trim().isNotEmpty() && globalSearchResults.isNotEmpty()) {
                    item {
                        Text(
                            text = "BULUNAN MESAJLAR (${globalSearchResults.size})",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicPrimary,
                            letterSpacing = 1.0.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 12.dp, bottom = 4.dp)
                        )
                    }

                    items(globalSearchResults) { msg ->
                        val matchingSession = sessions.find { it.id == msg.sessionId }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onSessionSelected(msg.sessionId) },
                            colors = CardDefaults.cardColors(
                                containerColor = CosmicSurfaceVariant.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = matchingSession?.title ?: "Oturum #${msg.sessionId}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicPrimaryVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = if (msg.role == "user") "Siz" else "AI",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (msg.role == "user") CosmicSecondary else CosmicPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg.text,
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // API Key status warning container
        if (!isApiKeyConfigured) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CosmicAccent.copy(alpha = 0.15f))
                    .border(1.dp, CosmicAccent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Warning, contentDescription = null, tint = CosmicAccent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "API Anahtarı bulunamadı.\nSecrets paneline gidin.",
                        fontSize = 10.sp,
                        color = Color.LightGray,
                        lineHeight = 12.sp
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Text(
                        text = "SYS_UPLINK: ACTIVE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }
                Text(
                    text = "V1.8.0",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
@Composable
fun ChatArea(
    viewModel: ChatViewModel,
    ttsInstance: TextToSpeech?,
    isTtsReady: Boolean,
    currentSession: ChatSession?,
    messages: List<ChatMessage>,
    isLoading: Boolean,
    errorMessage: String?,
    onSendMessage: (String) -> Unit,
    onSettingsClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    onClearClicked: () -> Unit,
    showMenuIcon: Boolean,
    isApiKeyConfigured: Boolean,
    speakingMessageId: Int?,
    onSpeakClicked: (ChatMessage) -> Unit,
    onStatsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val currentSessionId = currentSession?.id

    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var isSessionIdle by remember { mutableStateOf(false) }

    LaunchedEffect(inputText) {
        lastInteractionTime = System.currentTimeMillis()
        isSessionIdle = false
    }

    LaunchedEffect(messages.size) {
        lastInteractionTime = System.currentTimeMillis()
        isSessionIdle = false
    }

    LaunchedEffect(lastInteractionTime) {
        while (true) {
            delay(5000)
            val idleDuration = System.currentTimeMillis() - lastInteractionTime
            if (idleDuration > 120_000) { // 2 minutes
                isSessionIdle = true
            } else {
                isSessionIdle = false
            }
        }
    }

    // Restore draft precisely when session changes or loads
    LaunchedEffect(currentSessionId) {
        if (currentSessionId != null) {
            inputText = viewModel.getSessionDraft(currentSessionId)
        }
    }

    // Auto-save user inputs as draft whenever writing
    LaunchedEffect(inputText) {
        if (currentSessionId != null) {
            viewModel.saveSessionDraft(currentSessionId, inputText)
        }
    }
    val lazyListState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // SpeechRecognizer state for real-time RMS (decibel/volume) sensing
    var isListening by remember { mutableStateOf(false) }
    var micVolumeLevel by remember { mutableStateOf(0f) }
    var speechRecognizerInstance by remember { mutableStateOf<android.speech.SpeechRecognizer?>(null) }

    val isWakeWordEnabled by viewModel.isWakeWordEnabled.collectAsState()
    val wakeWord by viewModel.wakeWord.collectAsState()
    var isWaitingForWakeWord by remember { mutableStateOf(false) }
    val activeSentiment by viewModel.activeSessionSentiment.collectAsState()

    // Collect wake word voice input trigger events
    LaunchedEffect(Unit) {
        viewModel.voiceInputTriggerEvent.collect {
            isWaitingForWakeWord = false
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val permissionCheck = androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.RECORD_AUDIO
                )
                if (permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    startInlineListening()
                }
            }, 300)
        }
    }

    // Auto trigger background wake word listener
    LaunchedEffect(isWakeWordEnabled, isLoading) {
        if (isWakeWordEnabled && !isLoading) {
            isWaitingForWakeWord = true
            val permissionCheck = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.RECORD_AUDIO
            )
            if (permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                if (!isListening) {
                    startInlineListening()
                }
            }
        } else {
            if (!isWakeWordEnabled && isWaitingForWakeWord) {
                isWaitingForWakeWord = false
                stopInlineListening()
            }
        }
    }

    DisposableEffect(context) {
        onDispose {
            speechRecognizerInstance?.destroy()
        }
    }

    val speechRecognizerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.firstOrNull() ?: ""
            if (spokenText.isNotEmpty()) {
                inputText = spokenText
            }
        }
    }

    val startInlineListening: () -> Unit = {
        if (android.speech.SpeechRecognizer.isRecognitionAvailable(context)) {
            val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault().language)
                putExtra(android.speech.RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            }
            
            val recognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : android.speech.RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        isListening = true
                    }
                    override fun onBeginningOfSpeech() {
                        isListening = true
                    }
                    override fun onRmsChanged(rmsdB: Float) {
                        micVolumeLevel = rmsdB.coerceIn(0f, 12f)
                    }
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}
                    override fun onError(error: Int) {
                        isListening = false
                        micVolumeLevel = 0f
                        if (isWaitingForWakeWord && isWakeWordEnabled && !isLoading) {
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                if (isWakeWordEnabled && isWaitingForWakeWord && !isListening && !isLoading) {
                                    startInlineListening()
                                }
                            }, 1000)
                        }
                    }
                    override fun onResults(results: Bundle?) {
                        isListening = false
                        micVolumeLevel = 0f
                        val matches = results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                        val spokenText = matches?.firstOrNull() ?: ""
                        if (spokenText.isNotEmpty()) {
                            if (isWaitingForWakeWord && isWakeWordEnabled) {
                                val spokenLower = spokenText.lowercase(java.util.Locale.getDefault())
                                val wakeWordLower = wakeWord.lowercase(java.util.Locale.getDefault())
                                val hasWakeWord = spokenLower.contains("okey jarvis") || 
                                                 spokenLower.contains("okay jarvis") || 
                                                 spokenLower.contains("ok jarvis") || 
                                                 spokenLower.contains("jarvis") || 
                                                 (wakeWordLower.isNotEmpty() && spokenLower.contains(wakeWordLower))
                                
                                if (hasWakeWord) {
                                    isWaitingForWakeWord = false
                                    var extractedText = ""
                                    val matchPatterns = listOf("okey jarvis", "okay jarvis", "ok jarvis", "jarvis", wakeWordLower)
                                    for (pattern in matchPatterns) {
                                        val idx = spokenLower.indexOf(pattern)
                                        if (idx != -1) {
                                            extractedText = spokenText.substring(idx + pattern.length).trim().trimStart(',', '.', ' ', '?')
                                            break
                                        }
                                    }
                                    
                                    if (ttsInstance != null && isTtsReady) {
                                        ttsInstance.stop()
                                        ttsInstance.setSpeechRate(voiceSpeechRate)
                                        val msgText = if (extractedText.isNotEmpty()) "Anlaşıldı efendim." else "Evet efendim, sizi dinliyorum."
                                        val params = Bundle().apply {
                                            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "WAKE_WORD_REPLY")
                                        }
                                        ttsInstance.speak(msgText, TextToSpeech.QUEUE_FLUSH, params, "WAKE_WORD_REPLY")
                                    } else {
                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                            isWaitingForWakeWord = false
                                            startInlineListening()
                                        }, 500)
                                    }
                                    
                                    if (extractedText.isNotEmpty()) {
                                        inputText = extractedText
                                    }
                                } else {
                                    if (isWaitingForWakeWord && isWakeWordEnabled && !isLoading) {
                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                            if (isWakeWordEnabled && isWaitingForWakeWord && !isListening && !isLoading) {
                                                startInlineListening()
                                            }
                                        }, 800)
                                    }
                                }
                            } else {
                                inputText = spokenText
                            }
                        } else {
                            if (isWaitingForWakeWord && isWakeWordEnabled && !isLoading) {
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    if (isWakeWordEnabled && isWaitingForWakeWord && !isListening && !isLoading) {
                                        startInlineListening()
                                    }
                                }, 800)
                            }
                        }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                        val spokenText = matches?.firstOrNull() ?: ""
                        if (spokenText.isNotEmpty()) {
                            if (!isWaitingForWakeWord) {
                                inputText = spokenText
                            } else {
                                val spokenLower = spokenText.lowercase(java.util.Locale.getDefault())
                                val wakeWordLower = wakeWord.lowercase(java.util.Locale.getDefault())
                                val hasWakeWord = spokenLower.contains("okey jarvis") || 
                                                 spokenLower.contains("okay jarvis") || 
                                                 spokenLower.contains("ok jarvis") || 
                                                 spokenLower.contains("jarvis") || 
                                                 (wakeWordLower.isNotEmpty() && spokenLower.contains(wakeWordLower))
                                if (hasWakeWord) {
                                    isWaitingForWakeWord = false
                                    speechRecognizerInstance?.stopListening()
                                    
                                    if (ttsInstance != null && isTtsReady) {
                                        ttsInstance.stop()
                                        val params = Bundle().apply {
                                            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "WAKE_WORD_REPLY")
                                        }
                                        ttsInstance.setSpeechRate(voiceSpeechRate)
                                        ttsInstance.speak("Evet efendim, sizi dinliyorum.", TextToSpeech.QUEUE_FLUSH, params, "WAKE_WORD_REPLY")
                                    } else {
                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                            isWaitingForWakeWord = false
                                            startInlineListening()
                                        }, 500)
                                    }
                                }
                            }
                        }
                    }
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
            
            speechRecognizerInstance = recognizer
            recognizer.startListening(intent)
        } else {
            try {
                val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault().language)
                    putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, "Asistanınız sizi dinliyor...")
                }
                speechRecognizerLauncher.launch(intent)
            } catch (e: Exception) {
                android.widget.Toast.makeText(context, "Ses tanıma başlatılamadı: ${e.localizedMessage}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    val stopInlineListening = {
        try {
            speechRecognizerInstance?.stopListening()
            speechRecognizerInstance?.destroy()
        } catch (e: Exception) {
            // noop
        }
        speechRecognizerInstance = null
        isListening = false
        micVolumeLevel = 0f
    }

    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startInlineListening()
        } else {
            android.widget.Toast.makeText(context, "Mikrofon izni reddedildi.", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            android.widget.Toast.makeText(context, "Sistem bildirimleri aktif edildi.", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            val permissionCheck = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                "android.permission.POST_NOTIFICATIONS"
            )
            if (permissionCheck != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }
        }
    }

    val toggleMicAction = {
        if (isListening) {
            stopInlineListening()
        } else {
            val permissionCheck = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.RECORD_AUDIO
            )
            if (permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                startInlineListening()
            } else {
                permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    val handleGlobalKeyEvent: (KeyEvent) -> Boolean = { keyEvent ->
        var consumed = false
        if (keyEvent.type == KeyEventType.KeyDown) {
            when {
                // Ctrl + Enter to send message
                keyEvent.key == Key.Enter && keyEvent.isCtrlPressed -> {
                    val isSendEnabled = inputText.trim().isNotEmpty() && !isLoading && isApiKeyConfigured
                    if (isSendEnabled) {
                        val messageStr = inputText
                        inputText = ""
                        onSendMessage(messageStr)
                        consumed = true
                    }
                }
                // Esc to clear chat (if input text is non-empty, clear text; otherwise trigger reset confirm)
                keyEvent.key == Key.Escape -> {
                    if (inputText.isNotEmpty()) {
                        inputText = ""
                    } else {
                        onClearClicked()
                    }
                    consumed = true
                }
                // Ctrl + M to toggle microphone
                keyEvent.key == Key.M && keyEvent.isCtrlPressed -> {
                    toggleMicAction()
                    consumed = true
                }
                // Ctrl + N to open/create a new session
                keyEvent.key == Key.N && keyEvent.isCtrlPressed -> {
                    viewModel.createNewSession()
                    consumed = true
                }
                // Ctrl + P to open settings dialog
                keyEvent.key == Key.P && keyEvent.isCtrlPressed -> {
                    showSettingsDialog = true
                    consumed = true
                }
                // Ctrl + S to open stats / diagnostics panel
                keyEvent.key == Key.S && keyEvent.isCtrlPressed -> {
                    showStatsOverlay = true
                    consumed = true
                }
                // Ctrl + T to toggle Dark/Light Mode
                keyEvent.key == Key.T && keyEvent.isCtrlPressed -> {
                    viewModel.toggleTheme()
                    consumed = true
                }
            }
        }
        consumed
    }

    // Suggestions shortcuts list (Smart context-aware suggestions)
    val activeVibePrompt = currentSession?.systemInstruction ?: ""
    val suggestionShortcuts = remember(activeVibePrompt, customShortcuts) {
        if (customShortcuts.isNotEmpty()) {
            customShortcuts
        } else {
            when {
                activeVibePrompt.contains("yazılım", ignoreCase = true) || activeVibePrompt.contains("mühendis", ignoreCase = true) -> listOf(
                "Kotlin Coroutines best practices nedir?",
                "Compose ile modern neon buton hazırlama örneği yap.",
                "Android Clean Architecture katmanlarını kısaca açıkla.",
                "Oturumlar arası arama yapan Room SQL sorgusu yaz."
            )
            activeVibePrompt.contains("resmi", ignoreCase = true) || activeVibePrompt.contains("kurumsal", ignoreCase = true) -> listOf(
                "Yapay zekanın iş gücüne etkisi resmi rapor taslağı.",
                "Hizmet iptali kurumsal müşteri e-postası şablonu yaz.",
                "Android güvenlik denetim listesi raporu hazırla.",
                "Siber güvenlik risk analizi nasıl yapılır?"
            )
            activeVibePrompt.contains("kısa", ignoreCase = true) || activeVibePrompt.contains("net", ignoreCase = true) -> listOf(
                "Hızlandırılmış Jetpack Compose kılavuzu.",
                "Kuantum fiziği nedir? (1 cümle ile tanımla)",
                "Solid yazılım prensipleri nelerdir? (Çok kısa)",
                "Kotlin MVVM modeli avantajları."
            )
            activeVibePrompt.contains("yaratıcı", ignoreCase = true) -> listOf(
                "Siberpunk uzay gemisinde geçen kurgusal bir hikaye yaz.",
                "Bilinç kazanan kod parçasının monoloğunu tasarla.",
                "Gelecekteki yapay zeka şehirleri için şiirsel betimleme.",
                "Karanlık ve aydınlık modunu anlatan ilginç metaforlar."
            )
            activeVibePrompt.contains("siber", ignoreCase = true) || activeVibePrompt.contains("jarvis", ignoreCase = true) || activeVibePrompt.contains("asistan", ignoreCase = true) -> listOf(
                "Merkezi işlemci çekirdek durum raporu simüle et.",
                "Bana retro-fütüristik terminal ASCII sanatı yap.",
                "Siber güvenlik tespiti ve log sızıntısı analizi ver.",
                "Yapay sınır ağları ve sinaps ağırlıkları çalıştır."
            )
            else -> listOf(
                "Bana Kotlin Coroutine anlatabilir misin?",
                "Compose ile dalgalı neon buton yapımı.",
                "Açıklamalı 3 günlük tatil planı.",
                "Yazılım mülakat sorusu sor."
            )
        }
    }
}

    // Scroll automatically when new messages arrive
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundParticles()
        Scaffold(
            containerColor = Color.Transparent,
        topBar = {
            val infiniteTransition = rememberInfiniteTransition(label = "jarvis_pulse")
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse_alpha"
            )
            val sentimentColor = when (activeSentiment.type) {
                SentimentType.POSITIVE -> Color(0xFF10B981)
                SentimentType.NEGATIVE -> Color(0xFFEF4444)
                SentimentType.NEUTRAL -> CosmicPrimary
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(
                    visible = isSessionIdle,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEF4444).copy(alpha = 0.95f))
                            .padding(vertical = 10.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "BOŞTA KALMA UYARISI: Oturum 2 dakikadır inaktif.",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "KAPAT",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    lastInteractionTime = System.currentTimeMillis()
                                    isSessionIdle = false
                                }
                                .background(Color.Black.copy(alpha = 0.3f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showMenuIcon) {
                        IconButton(onClick = onMenuClicked) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menüyü Aç", tint = MaterialTheme.colorScheme.onBackground)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentSession?.title ?: "Akıllı Asistan",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(sentimentColor.copy(alpha = pulseAlpha))
                                    .border(1.dp, sentimentColor, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            
                            val statusText = when {
                                isLoading -> "COMPUTING SYSTEM..."
                                isListening -> "LISTENING SOURCE // RMS: ${(micVolumeLevel * 10).toInt()}dB"
                                isWaitingForWakeWord -> "WAKE STANDBY [Okey Jarvis]"
                                else -> "SYS_ACTIVE // ${activeSentiment.description}"
                            }
                            
                            Text(
                                text = statusText,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (isLoading || isListening) sentimentColor else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = sentimentColor.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .border(1.dp, sentimentColor.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = activeSentiment.emoji + " " + activeSentiment.type.name,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = sentimentColor
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))
                            val pct = batteryLevel.value ?: 100
                            val batColor = when {
                                batteryIsCharging.value -> Color(0xFF38BDF8) // Bright Cyber Blue
                                pct >= 60 -> Color(0xFF10B981) // Cyber Green
                                pct >= 20 -> Color(0xFFFBBF24) // Warning Yellow
                                else -> Color(0xFFEF4444) // Critical Red
                            }
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = batColor.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .border(1.dp, batColor.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    // Custom visual Battery Icon bar
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(11.dp, 6.dp)
                                                .border(0.5.dp, batColor, RoundedCornerShape(1.dp))
                                                .padding(0.7.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .fillMaxWidth(pct / 100f)
                                                    .background(batColor)
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(1.dp, 2.5.dp)
                                                .background(batColor)
                                        )
                                    }
                                    if (batteryIsCharging.value) {
                                        Text(text = "⚡", fontSize = 7.sp, color = batColor)
                                    }
                                    Text(
                                        text = "$pct%",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = batColor
                                    )
                                }
                            }
                        }
                    }

                    if (currentSession != null) {
                        IconButton(
                            onClick = {
                                val exportStr = buildString {
                                    appendLine("# J.A.R.V.I.S SOHBET GEÇMİŞİ")
                                    appendLine("Sohbet Başlığı: ${currentSession.title}")
                                    appendLine("Model: ${currentSession.model}")
                                    appendLine("---")
                                    messages.forEach { msg ->
                                        val sender = if (msg.role == "user") "KULLANICI" else "J.A.R.V.I.S"
                                        appendLine("### [$sender] - ${msg.timestamp ?: ""}")
                                        appendLine("${msg.text}")
                                        appendLine()
                                    }
                                }
                                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                val clip = android.content.ClipData.newPlainText("JARVIS Chat Export", exportStr)
                                clipboard.setPrimaryClip(clip)
                                
                                val shareIntent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_TEXT, exportStr)
                                    type = "text/plain"
                                }
                                context.startActivity(android.content.Intent.createChooser(shareIntent, "Sohbeti Paylaş / Dışa Aktar"))
                            },
                            modifier = Modifier.testTag("export_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Sohbeti Dışa Aktar",
                                tint = CosmicPrimaryVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onStatsClicked,
                            modifier = Modifier.testTag("stats_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Assessment,
                                contentDescription = "Sistem İstatistikleri",
                                tint = CosmicSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))

                        val isGridLayoutEnabled by viewModel.isGridLayoutEnabled.collectAsState()
                        IconButton(
                            onClick = { viewModel.toggleGridLayout() },
                            modifier = Modifier.testTag("grid_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isGridLayoutEnabled) Icons.Filled.GridView else Icons.Filled.List,
                                contentDescription = "Mozaik / Grid Görünümü",
                                tint = if (isGridLayoutEnabled) CosmicPrimary else Color.LightGray
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))

                        val isDashboardExpanded by viewModel.isDashboardExpanded.collectAsState()
                        IconButton(
                            onClick = { viewModel.setDashboardExpanded(!isDashboardExpanded) },
                            modifier = Modifier.testTag("dashboard_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (isDashboardExpanded) Icons.Filled.Dashboard else Icons.Filled.Build,
                                contentDescription = "Sistem Kontrol Paneli",
                                tint = if (isDashboardExpanded) CosmicSecondary else Color.LightGray
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = { viewModel.toggleTheme() },
                            modifier = Modifier.testTag("theme_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (currentIsDarkMode) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                                contentDescription = if (currentIsDarkMode) "Aydınlık Modu Aç" else "Karanlık Modu Aç",
                                tint = CosmicPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onClearClicked,
                            modifier = Modifier.testTag("clear_history_button")
                        ) {
                            Icon(Icons.Filled.ClearAll, contentDescription = "Oturumu Temizle", tint = if (LocalSciFiColors.current.isDark) Color.LightGray else Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onSettingsClicked,
                            modifier = Modifier.testTag("settings_button")
                        ) {
                            Icon(Icons.Filled.Settings, contentDescription = "Asistan Ayarları", tint = CosmicPrimary)
                        }
                    }
                }
                // Faint top separator line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (currentSession == null) {
                // Outer clean layout when no session is active
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bir sohbet oturumu açın veya\nyeni bir oturum başlatın.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            } else {
                // Quick Vibe Selector Row
                val personalities = listOf(
                    Triple("Standart", "Sen kullanıcıya her konuda yardımcı olan, cana yakın ve çok zeki bir yapay zeka asistanısın.", Icons.Filled.Face),
                    Triple("Resmi", "Sen her zaman resmi, son derece profesyonel, ciddi ve kibar bir üslup kullanan bir yapay zeka asistanısın. Yanıtlarında kurumsal dile sadık kal, deyim ve mecazlardan kaçın, teknik terimleri doğru kullan ve net, mantıklı açıklamalar yap.", Icons.Filled.Business),
                    Triple("Kısa ve Net", "Sen her zaman son derece kısa, net, pratik ve doğrudan sonuca giden yanıtlar veren bir yapay zeka asistanısın. Sadece sorulan sorunun tam cevabını ver. Gereksiz hiçbir ayrıntı, açıklama veya yan bilgi ekleme.", Icons.Filled.Bolt),
                    Triple("Yaratıcı", "Sen her zaman yaratıcı, fantastik, sanatsal ve ilham verici düşünen, hayal gücü geniş bir yapay zeka asistanısın. Yanıtlarında bolca benzetme, hikayeleştirme ve orijinal bakış açıları barındır. Şiirsel veya edebi dokunuşlar ekle.", Icons.Filled.Lightbulb),
                    Triple("Siber", "Sen fütüristik bir uzay gemisi yapay zekası ya da sibernetik bir androidsin. Kod adın NEURAL LINK. Yanıtlarında yüksek teknoloji terimleri, siber-sistem durumu raporları ve fütüristik semboller kullan.", Icons.Filled.Build)
                )

                val activeInstruct = currentSession?.systemInstruction ?: ""
                val activeIndex = personalities.indexOfFirst { it.second == activeInstruct }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CosmicSurface)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VIBE SELECTION //",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CosmicPrimaryVariant.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    
                    personalities.forEachIndexed { index, (vibeName, prompt, icon) ->
                        val isSelected = index == activeIndex || (activeIndex == -1 && index == 0)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CosmicPrimary.copy(alpha = 0.15f) else Color.Transparent)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) CosmicPrimary else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    viewModel.updateSessionConfigs(
                                        currentSession!!.model,
                                        currentSession!!.temperature,
                                        prompt
                                    )
                                    Toast.makeText(context, "Karakter/Vibe: [$vibeName] aktif!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = vibeName,
                                    tint = if (isSelected) CosmicPrimary else Color.Gray,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = vibeName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color.Gray
                                )
                            }
                        }
                    }
                }
                
                // Line separator below vibe row
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                )

                // Messages Panel and Input Zone
                val isWideLayout = maxWidth > 650.dp
                val isDashboardExpanded by viewModel.isDashboardExpanded.collectAsState()
                val isGridLayoutEnabled by viewModel.isGridLayoutEnabled.collectAsState()

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Chat content container
                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .fillMaxHeight()
                    ) {
                        // Geometric grid pattern background
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val gridSpace = 60.dp.toPx()
                            val lineColor = Color.White.copy(alpha = 0.02f)

                            // Vertical lines
                            var x = 0f
                            while (x < size.width) {
                                drawLine(
                                    color = lineColor,
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height),
                                    strokeWidth = 1f
                                )
                                x += gridSpace
                            }

                            // Horizontal lines
                            var y = 0f
                            while (y < size.height) {
                                drawLine(
                                    color = lineColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1f
                                )
                                y += gridSpace
                            }
                        }

                        if (messages.isEmpty() && !isLoading) {
                            // Empty chat view / suggestion triggers
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp)
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                PersonalityPortrait(
                                    systemInstruction = currentSession?.systemInstruction ?: "",
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = LanguageHelper.getString("empty_title", lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = LanguageHelper.getString("empty_desc", lang),
                                    fontSize = 12.sp,
                                    color = Color.LightGray,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // Suggestions list
                                suggestionShortcuts.forEach { text ->
                                    Card(
                                        onClick = { inputText = text },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = CosmicSurface)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Filled.RocketLaunch,
                                                contentDescription = null,
                                                tint = CosmicPrimaryVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(text, fontSize = 12.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        } else {
                            // Messages lists
                            val columnsCount = when {
                                maxWidth > 850.dp -> 3
                                maxWidth > 600.dp -> 2
                                else -> 1
                            }
                            val gridChunks = remember(messages, columnsCount) {
                                messages.chunked(columnsCount)
                            }

                            LazyColumn(
                                state = lazyListState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                contentPadding = PaddingValues(top = 16.dp, bottom = 12.dp)
                            ) {
                                if (isGridLayoutEnabled) {
                                    items(gridChunks) { chunk ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            chunk.forEach { message ->
                                                val isUser = message.role == "user"
                                                val activeInstruct = currentSession?.systemInstruction ?: ""
                                                val (botPersonalityLabel, botIcon) = when {
                                                    activeInstruct.contains("cana yakın") -> Pair("Asistan [Cana Yakın]", Icons.Filled.Face)
                                                    activeInstruct.contains("resmi") -> Pair("Asistan [Resmi]", Icons.Filled.Business)
                                                    activeInstruct.contains("kısa") -> Pair("Asistan [Kısa ve Net]", Icons.Filled.Bolt)
                                                    activeInstruct.contains("yaratıcı") -> Pair("Asistan [Yaratıcı]", Icons.Filled.Lightbulb)
                                                    activeInstruct.contains("fütüristik") || activeInstruct.contains("NEURAL LINK") -> Pair("NEURAL LINK AI", Icons.Filled.Build)
                                                    else -> Pair("Asistan", Icons.Filled.AutoAwesome)
                                                }
                                                MessageCardInGrid(
                                                    message = message,
                                                    isUser = isUser,
                                                    botIcon = botIcon,
                                                    botPersonalityLabel = botPersonalityLabel,
                                                    onSpeakClicked = { onSpeakClicked(message) },
                                                    isSpeaking = speakingMessageId == message.id,
                                                    onCopyClicked = {
                                                        clipboardManager.setText(AnnotatedString(message.text))
                                                        Toast.makeText(context, "Mesaj kopyalandı!", Toast.LENGTH_SHORT).show()
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                            val remainder = columnsCount - chunk.size
                                            if (remainder > 0) {
                                                repeat(remainder) {
                                                    Spacer(modifier = Modifier.weight(1f))
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    items(messages, key = { message.id }) { message ->
                                        val isUser = message.role == "user"
                                        val isLatestModelMsg = !isUser && (messages.lastOrNull()?.id == message.id)
                                        val activeInstruct = currentSession?.systemInstruction ?: ""
                                        val (botPersonalityLabel, botIcon) = when {
                                            activeInstruct.contains("cana yakın") -> Pair("Asistan [Cana Yakın]", Icons.Filled.Face)
                                            activeInstruct.contains("resmi") -> Pair("Asistan [Resmi]", Icons.Filled.Business)
                                            activeInstruct.contains("kısa") -> Pair("Asistan [Kısa ve Net]", Icons.Filled.Bolt)
                                            activeInstruct.contains("yaratıcı") -> Pair("Asistan [Yaratıcı]", Icons.Filled.Lightbulb)
                                            activeInstruct.contains("fütüristik") || activeInstruct.contains("NEURAL LINK") -> Pair("NEURAL LINK AI", Icons.Filled.Build)
                                            else -> Pair("Asistan", Icons.Filled.AutoAwesome)
                                        }
                                        MessageRow(
                                            message = message,
                                            isUser = isUser,
                                            animateTypewriter = isLatestModelMsg,
                                            botIcon = botIcon,
                                            botPersonalityLabel = botPersonalityLabel,
                                            isSpeaking = speakingMessageId == message.id,
                                            onSpeakClicked = { onSpeakClicked(message) },
                                            onCopyClicked = {
                                                clipboardManager.setText(AnnotatedString(message.text))
                                                Toast.makeText(context, "Mesaj kopyalandı!", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                val lastMessage = messages.lastOrNull()
                                if (!isLoading && lastMessage != null && lastMessage.role == "model") {
                                    item(key = "quick_replies") {
                                        val quickReplies = remember(lastMessage.text) {
                                            generateQuickReplies(lastMessage.text)
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp, horizontal = 8.dp)
                                        ) {
                                            Text(
                                                text = "Devam Etmek İçin Öneriler:",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = CosmicPrimary.copy(alpha = 0.8f),
                                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                                            )
                                            
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .horizontalScroll(rememberScrollState()),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                quickReplies.forEachIndexed { index, reply ->
                                                    Surface(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .clickable {
                                                                onSendMessage(reply)
                                                            }
                                                            .testTag("quick_reply_button_$index")
                                                            .border(
                                                                1.dp,
                                                                CosmicPrimary.copy(alpha = 0.4f),
                                                                RoundedCornerShape(12.dp)
                                                            ),
                                                        color = CosmicSurface.copy(alpha = 0.6f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Filled.Send,
                                                                contentDescription = null,
                                                                tint = CosmicPrimary,
                                                                modifier = Modifier.size(10.dp)
                                                            )
                                                            Text(
                                                                text = reply,
                                                                color = Color.White,
                                                                fontSize = 11.sp,
                                                                fontWeight = FontWeight.Medium
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (isLoading) {
                                    item(key = "typing_indicator") {
                                        BotTypingRow()
                                    }
                                }
                            }
                        }
                    }

                    // Side-by-side Panel (Wide Screen format only)
                    if (isDashboardExpanded && isWideLayout) {
                        Box(
                            modifier = Modifier
                                .width(310.dp)
                                .fillMaxHeight()
                                .background(CosmicSurface)
                                .border(start = 1.dp, color = CosmicPrimary.copy(alpha = 0.2f))
                        ) {
                            SciFiDashboardPanel(viewModel = viewModel)
                        }
                    }
                }

                // Collapsible Bottom Panel (Compact/Mobile format only)
                if (isDashboardExpanded && !isWideLayout) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .background(CosmicSurface)
                            .border(top = 1.dp, color = CosmicSecondary.copy(alpha = 0.2f))
                    ) {
                        SciFiDashboardPanel(viewModel = viewModel)
                    }
                }

                // Error Banner Overlay
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    errorMessage?.let { errorText ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CosmicAccent.copy(alpha = 0.9f))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Error, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorText,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Chat Input box container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CosmicSurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Small inline api key alert warning if not configured
                    if (!isApiKeyConfigured) {
                        Text(
                            text = "Uyarı: Gemini API Anahtarı eksik! AI Studio Secrets'ten yapılandırana kadar cevap üretemeyeceğim.",
                            color = CosmicAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    // Ambient/Active Sci-Fi Audio Waveform visualizer
                    SciFiAudioWaveform(
                        isSpeaking = speakingMessageId != null,
                        isListening = isListening,
                        inputLevel = micVolumeLevel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .padding(bottom = 4.dp)
                    )

                    // --- INLINE PORTABLE COMMAND SUGGESTIONS PANEL ---
                    if (inputText.startsWith("/")) {
                        val filterText = inputText.removePrefix("/").lowercase()
                        val haptic = LocalHapticFeedback.current
                        val rawCommands = listOf(
                            Triple("new", "Yeni Sohbet Başlat", "Hızlıca yeni temiz bir oturum açar"),
                            Triple("theme", "Temayı Değiştir", "Aydınlık ve karanlık mod arasında geçiş yapar"),
                            Triple("clear", "Mesajları Temizle", "Aktif oturumdaki tüm mesaj geçmişini sıfırlar"),
                            Triple("stats", "Neural Link Overlay", "Gelişmiş istatistik ve metrikleri görüntüler"),
                            Triple("voice", "Wake Word Değiştir", "Mikrofon sesli uyandırma durumunu değiştirir"),
                            Triple("setting", "Ayarlar Paneli", "Asistan model ve sistem direktif ayarlarını açar"),
                            Triple("export", "Geçmişi Dışa Aktar", "Sohbet dökümünü cihaza kopyalar ve paylaşır")
                        )
                        val matchedCommands = rawCommands.filter { it.first.startsWith(filterText) }
                        if (matchedCommands.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .border(1.dp, CosmicPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant.copy(alpha = 0.95f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "KOMUT ÖNERİLERİ",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                    matchedCommands.forEach { (cmdName, cmdTitle, cmdDesc) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    inputText = ""
                                                    when (cmdName) {
                                                        "new" -> viewModel.createNewSession()
                                                        "theme" -> viewModel.toggleTheme()
                                                        "clear" -> viewModel.clearActiveMessages()
                                                        "stats" -> showStatsOverlay = true
                                                        "voice" -> viewModel.setWakeWordEnabled(!viewModel.isWakeWordEnabled.value)
                                                        "setting" -> showSettingsDialog = true
                                                        "export" -> {
                                                            val currentSession = viewModel.currentSession.value
                                                            val exportStr = buildString {
                                                                appendLine("# J.A.R.V.I.S SOHBET GEÇMİŞİ")
                                                                appendLine("Sohbet Başlığı: ${currentSession?.title ?: ""}")
                                                                appendLine("Model: ${currentSession?.model ?: ""}")
                                                                appendLine("---")
                                                                messages.forEach { msg ->
                                                                    val sender = if (msg.role == "user") "KULLANICI" else "J.A.R.V.I.S"
                                                                    appendLine("### [$sender] - ${msg.timestamp}")
                                                                    appendLine(msg.text)
                                                                    appendLine()
                                                                }
                                                            }
                                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                            val clip = android.content.ClipData.newPlainText("JARVIS Chat Export", exportStr)
                                                            clipboard.setPrimaryClip(clip)
                                                            Toast.makeText(context, "Sohbet dökümü panoya kopyalandı!", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }
                                                .background(CosmicSurface)
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Terminal,
                                                contentDescription = null,
                                                tint = CosmicPrimaryVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "/$cmdName — $cmdTitle",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                                Text(
                                                    text = cmdDesc,
                                                    fontSize = 10.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Filled.ChevronRight,
                                                contentDescription = null,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val haptic = LocalHapticFeedback.current
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Asistanımla sohbet edin...", color = Color.Gray) },
                            leadingIcon = {
                                IconButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        showCommandPaletteDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Terminal,
                                        contentDescription = "Komut Paleti",
                                        tint = CosmicPrimary
                                    )
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("message_input")
                                .clip(RoundedCornerShape(24.dp))
                                .onKeyEvent { event -> handleGlobalKeyEvent(event) },
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                focusedContainerColor = CosmicBackground,
                                unfocusedContainerColor = CosmicBackground,
                                focusedBorderColor = CosmicPrimary,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Elegant Glowing Voice Assistant Input Button
                        val micBgColor = if (isListening) CosmicAccent.copy(alpha = 0.2f) else CosmicSurfaceVariant
                        val micBorderColor = if (isListening) CosmicAccent else CosmicSecondary.copy(alpha = 0.5f)
                        val micIconColor = if (isListening) CosmicAccent else CosmicSecondary
                        IconButton(
                            onClick = { toggleMicAction() },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(micBgColor)
                                .border(1.dp, micBorderColor, CircleShape)
                                .testTag("mic_button")
                        ) {
                            Icon(
                                imageVector = if (isListening) Icons.Filled.MicOff else Icons.Filled.Mic,
                                contentDescription = if (isListening) "Kaydı Durdur" else "Sesle Yaz",
                                tint = micIconColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        val isSendEnabled = inputText.trim().isNotEmpty() && !isLoading && isApiKeyConfigured
                        IconButton(
                            onClick = {
                                val messageStr = inputText
                                inputText = ""
                                onSendMessage(messageStr)
                            },
                            enabled = isSendEnabled,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isSendEnabled) CosmicPrimary else CosmicSurfaceVariant)
                                .testTag("send_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Mesaj Gönder",
                                tint = if (isSendEnabled) Color.Black else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
}
}

// Single bubble item design
@Composable
fun MessageRow(
    message: ChatMessage,
    isUser: Boolean,
    animateTypewriter: Boolean = false,
    botIcon: ImageVector = Icons.Filled.AutoAwesome,
    botPersonalityLabel: String = "Asistan",
    isSpeaking: Boolean = false,
    onSpeakClicked: () -> Unit,
    onCopyClicked: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val bubbleShape = if (isUser) {
        RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
    }

    val bubbleColor = if (isUser) UserBubbleColor else BotBubbleColor
    val alignment = if (isUser) Alignment.End else Alignment.Start
    
    // Custom micro-level sentiment check
    val rowSentiment = remember(message.text) { SentimentAnalyzer.analyze(message.text) }
    
    val bubbleBorderColor = if (isUser) {
        when (rowSentiment.type) {
            SentimentType.POSITIVE -> Color(0xFF10B981).copy(alpha = 0.4f)
            SentimentType.NEGATIVE -> Color(0xFFEF4444).copy(alpha = 0.4f)
            SentimentType.NEUTRAL -> Color.Transparent
        }
    } else {
        CosmicPrimary.copy(alpha = 0.15f)
    }

    // Typewriter effect state
    val displayedText = if (animateTypewriter) {
        val typedText = remember(message.id) { mutableStateOf("") }
        val isTyping = remember(message.id) { mutableStateOf(true) }
        LaunchedEffect(message.text) {
            val fullText = message.text
            isTyping.value = true
            for (i in 1..fullText.length) {
                typedText.value = fullText.substring(0, i)
                delay(10) // Fast 10ms typing delay for futuristic high-tech feel
            }
            isTyping.value = false
        }
        if (isTyping.value) "${typedText.value}▋" else typedText.value
    } else {
        message.text
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = alignment
    ) {
        // Label header for visual elegance
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp, start = if (isUser) 0.dp else 4.dp, end = if (isUser) 4.dp else 0.dp)
        ) {
            Icon(
                imageVector = if (isUser) Icons.Filled.Person else botIcon,
                contentDescription = null,
                tint = if (isUser) CosmicPrimaryVariant else CosmicPrimary,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isUser) "Siz" else botPersonalityLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUser) CosmicPrimaryVariant else CosmicPrimary
            )
            
            if (isUser) {
                Spacer(modifier = Modifier.width(6.dp))
                val badgeColor = when (rowSentiment.type) {
                    SentimentType.POSITIVE -> Color(0xFF34D399)
                    SentimentType.NEGATIVE -> Color(0xFFF87171)
                    SentimentType.NEUTRAL -> Color.Gray
                }
                Text(
                    text = "[${rowSentiment.emoji} ${rowSentiment.type.name}]",
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            val timeString = remember(message.timestamp) {
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                sdf.format(Date(message.timestamp))
            }
            Text(timeString, fontSize = 9.sp, color = Color.Gray)
        }

        if (isUser) {
            Box(
                modifier = Modifier
                    .widthIn(max = 290.dp)
                    .clip(bubbleShape)
                    .background(bubbleColor)
                    .border(if (rowSentiment.type != SentimentType.NEUTRAL) 1.5.dp else 1.dp, bubbleBorderColor, bubbleShape)
                    .pointerInput(message.id) {
                        detectTapGestures(
                            onDoubleTap = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onCopyClicked()
                            },
                            onLongPress = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSpeakClicked()
                            }
                        )
                    }
                    .padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (LocalSciFiColors.current.isDark) Color.White else Color(0xFF0F172A),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .widthIn(max = 330.dp)
                        .clip(bubbleShape)
                        .background(bubbleColor)
                        .border(1.dp, bubbleBorderColor, bubbleShape)
                        .pointerInput(message.id) {
                            detectTapGestures(
                                onDoubleTap = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onCopyClicked()
                                },
                                onLongPress = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onSpeakClicked()
                                }
                            )
                        }
                        .padding(12.dp)
                ) {
                    val blocks = remember(displayedText) { parseResponseToBlocks(displayedText) }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        blocks.forEach { block ->
                            RenderResponseBlock(block) { codeToCopy ->
                                clipboardManager.setText(AnnotatedString(codeToCopy))
                                Toast.makeText(context, "Kod panoya kopyalandı!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Beautifully designed stacked TTS Speak button and Copy to Clipboard button
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Speak Aloud / Mute TTS Button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClickLabel = if (isSpeaking) "Sesi Durdur" else "Seslendir (Oku)",
                                onClick = onSpeakClicked
                            )
                            .testTag("speak_message_button_${message.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSpeaking) CosmicPrimary.copy(alpha = 0.2f) else CosmicSurfaceVariant)
                                .border(
                                    1.dp,
                                    if (isSpeaking) CosmicPrimary else CosmicPrimary.copy(alpha = 0.4f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSpeaking) {
                                val infiniteTransition = rememberInfiniteTransition(label = "SpeakWaves")
                                val waveScale1 by infiniteTransition.animateFloat(
                                    initialValue = 0.8f,
                                    targetValue = 1.2f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(450, easing = LinearEasing),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "WaveScale1"
                                )
                                Icon(
                                    imageVector = Icons.Filled.VolumeUp,
                                    contentDescription = "Sesi Durdur",
                                    tint = CosmicPrimary,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .graphicsLayer {
                                            scaleX = waveScale1
                                            scaleY = waveScale1
                                        }
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.VolumeMute,
                                    contentDescription = "Seslendir",
                                    tint = CosmicPrimaryVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // Copy to Clipboard button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClickLabel = "Cevabı Kopyala",
                                onClick = onCopyClicked
                            )
                            .testTag("copy_to_clipboard_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CosmicSurfaceVariant)
                                .border(1.dp, CosmicPrimary.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Cevabı Kopyala",
                                tint = CosmicPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Typing indicators
@Composable
fun BotTypingRow() {
    val infiniteTransition = rememberInfiniteTransition(label = "QuantumHUD")
    
    // Rotating HUD Ring angle
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "HUDRotation"
    )

    // Glowing/Pulsing core scale
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CorePulse"
    )

    // Hologram cyber-line breathing alpha
    val lineAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LinePulse"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Futuristic Status Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = CosmicPrimary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "NEURAL LINK STABLE // PROCESSING QUERY",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = CosmicPrimary,
                letterSpacing = 1.sp
            )
        }

        // Processing Bubble Panel
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp, 18.dp, 18.dp, 2.dp))
                .background(BotBubbleColor)
                .border(1.dp, CosmicPrimary.copy(alpha = 0.15f), RoundedCornerShape(18.dp, 18.dp, 18.dp, 2.dp))
                .padding(14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interactive HUD Core
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(36.dp)
                ) {
                    // Outer rotating cybernetic fragments
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(rotationAngle)
                    ) {
                        // Cyan/Mint fragment
                        drawArc(
                            color = CosmicPrimary.copy(alpha = 0.8f),
                            startAngle = 0f,
                            sweepAngle = 100f,
                            useCenter = false,
                            style = Stroke(width = 2.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )
                        // Cyan/Blue fragment
                        drawArc(
                            color = CosmicSecondary.copy(alpha = 0.6f),
                            startAngle = 140f,
                            sweepAngle = 80f,
                            useCenter = false,
                            style = Stroke(width = 2.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )
                        // Dim helper arc
                        drawArc(
                            color = CosmicPrimary.copy(alpha = 0.15f),
                            startAngle = 240f,
                            sweepAngle = 90f,
                            useCenter = false,
                            style = Stroke(width = 1.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )
                    }

                    // Pulsing inner quantum core
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .graphicsLayer {
                                scaleX = pulseScale
                                scaleY = pulseScale
                            }
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(CosmicPrimary, CosmicSecondary)
                                )
                            )
                    )
                }

                // Cyber terminal scanning strings
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "SYNAPSE DECRYPT...",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = lineAlpha),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "GEN_DAT://M3_BOT_INT",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CosmicSecondary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

fun generateQuickReplies(text: String): List<String> {
    val lower = text.lowercase(java.util.Locale("tr", "TR"))
    return when {
        lower.contains("kod") || lower.contains("program") || lower.contains("android") || 
        lower.contains("kotlin") || lower.contains("xml") || lower.contains("hata") || 
        lower.contains("fun ") || lower.contains("val ") || lower.contains("class ") ||
        lower.contains("java") || lower.contains("python") || lower.contains("api") -> {
            listOf(
                "Bu kodu detaylıca açıklar mısın?",
                "Daha performanslı bir alternatifi var mı?",
                "Bunu farklı bir örnekle gösterebilir misin?"
            )
        }
        lower.contains("tasarım") || lower.contains("ui") || lower.contains("renk") || 
        lower.contains("butona") || lower.contains("görsel") || lower.contains("tema") ||
        lower.contains("animas") || lower.contains("efekt") -> {
            listOf(
                "Farklı bir renk şeması veya tema önerir misin?",
                "Bu tasarımın mobil uyumluluğu nasıl olmalı?",
                "Kullanıcı deneyimini (UX) nasıl iyileştirebilirim?"
            )
        }
        lower.contains("rota") || lower.contains("seyahat") || lower.contains("gezilecek") || 
        lower.contains("yemek") || lower.contains("şehir") || lower.contains("ülke") ||
        lower.contains("otel") || lower.contains("müze") || lower.contains("bilet") ||
        lower.contains("restoran") -> {
            listOf(
                "Bu rotada bütçe dostu ne gibi seçenekler var?",
                "Mutlaka denemem gereken yerel lezzet nedir?",
                "Burayı ziyaret etmek için en iyi mevsim hangisidir?"
            )
        }
        lower.contains("şiir") || lower.contains("hikaye") || lower.contains("yaratıcı") || 
        lower.contains("masal") || lower.contains("kurgu") || lower.contains("karakter") -> {
            listOf(
                "Harika! Hikayeyi bir sonraki adımla devam ettir.",
                "Bu kurguya beklenmedik esrarengiz bir son yazar mısın?",
                "Bunu daha mizahi veya enerjik bir dille yaz."
            )
        }
        lower.contains("nedir") || lower.contains("neden") || lower.contains("tarih") || 
        lower.contains("bilim") || lower.contains("nasıl") || lower.contains("fizik") ||
        lower.contains("kimya") || lower.contains("matematik") || lower.contains("uzay") -> {
            listOf(
                "Bunu 10 yaşında bir çocuğun anlayacağı şekilde açıklar mısın?",
                "Peki bunun tarihteki ilk ortaya çıkışı nasıldı?",
                "Bu konuyla ilgili az bilinen ilginç bir bilgi paylaşır mısın?"
            )
        }
        else -> {
            listOf(
                "Bu konuda bana somut bir örnek verebilir misin?",
                "Bunun avantajları ve olası dezavantajları nelerdir?",
                "Harika, peki bir sonraki adımda ne yapmalıyım?"
            )
        }
    }
}

@Composable
fun SciFiAudioWaveform(
    isSpeaking: Boolean,
    isListening: Boolean,
    inputLevel: Float,
    modifier: Modifier = Modifier
) {
    val colors = LocalSciFiColors.current
    val infiniteTransition = rememberInfiniteTransition(label = "SciFiWave")
    
    // Constant continuous phase shift
    val phaseShift1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase1"
    )
    val phaseShift2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase2"
    )
    val phaseShift3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase3"
    )

    // Base target amplitudes depending on states
    val targetAmplitude = when {
        isListening -> 8f + inputLevel * 3.5f
        isSpeaking -> 14f
        else -> 3f
    }
    
    // Smooth transition of amplitude to make it look responsive and organic
    val animatedAmplitude by animateFloatAsState(
        targetValue = targetAmplitude,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "Amplitude"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .testTag("scifi_audio_waveform")
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f

        if (width <= 0 || height <= 0) return@Canvas

        // Draw multiple waves
        val drawSineWave = { phase: Float, color: Color, strokeWidth: Float, waveCount: Float, dampFactor: Float ->
            val path = Path()
            path.moveTo(0f, centerY)
            
            for (x in 0..width.toInt() step 4) {
                val xFloat = x.toFloat()
                // Apply a gaussian-like envelope so waves shrink smoothly to 0 at the start and end (looks like Siri/Google overlay!)
                val envelope = Math.sin((xFloat / width) * Math.PI).toFloat() * dampFactor
                
                val sineValue = Math.sin((xFloat / width) * Math.PI * waveCount + phase).toFloat()
                val y = centerY + sineValue * animatedAmplitude * density * envelope
                path.lineTo(xFloat, y)
            }
            
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth * density)
            )
        }

        // Wave 1: Primary color (ambient/active)
        drawSineWave(phaseShift1, colors.primary.copy(alpha = 0.65f), 1.8f, 2.2f, 1.0f)
        
        // Wave 2: Secondary Indigo/Purple variant
        drawSineWave(phaseShift2, colors.secondary.copy(alpha = 0.45f), 1.2f, 3.2f, 0.8f)
        
        // Wave 3: PrimaryVariant Cyan accent
        drawSineWave(phaseShift3, colors.primaryVariant.copy(alpha = 0.35f), 0.8f, 1.6f, 1.1f)
    }
}

// Visual Response Cards Supporting Types
sealed class ResponseBlock {
    data class Code(val code: String, val language: String) : ResponseBlock()
    data class Alert(val text: String, val type: AlertType, val originalHeader: String) : ResponseBlock()
    data class KeyValueGroup(val pairs: List<Pair<String, String>>) : ResponseBlock()
    data class BulletList(val items: List<String>) : ResponseBlock()
    data class NumberedList(val items: List<String>) : ResponseBlock()
    data class Paragraph(val text: String) : ResponseBlock()
}

enum class AlertType {
    WARNING, INFO, SUCCESS, TIP
}

// Markdown-like parser optimized for Sci-Fi Visual Cards
fun parseResponseToBlocks(text: String): List<ResponseBlock> {
    val blocks = mutableListOf<ResponseBlock>()
    val lines = text.split("\n")
    var index = 0
    val totalLines = lines.size

    while (index < totalLines) {
        val line = lines[index]
        val trimmedLine = line.trim()

        // 1. Code Block parsing
        if (trimmedLine.startsWith("```")) {
            val language = trimmedLine.removePrefix("```").trim()
            val codeBuilder = StringBuilder()
            index++
            while (index < totalLines && !lines[index].trim().startsWith("```")) {
                codeBuilder.append(lines[index]).append("\n")
                index++
            }
            if (index < totalLines && lines[index].trim().startsWith("```")) {
                index++
            }
            blocks.add(ResponseBlock.Code(codeBuilder.toString().trimEnd(), language))
            continue
        }

        // 2. Alert Block parsing (Warning, error, info, tip, success)
        val isWarning = trimmedLine.startsWith("**UYARI**", ignoreCase = true) || 
                        trimmedLine.startsWith("**HATA**", ignoreCase = true) || 
                        trimmedLine.startsWith("**DİKKAT**", ignoreCase = true)
        val isInfo = trimmedLine.startsWith("[BİLGİ]", ignoreCase = true) || 
                     trimmedLine.startsWith("[INFO]", ignoreCase = true)
        val isTip = trimmedLine.startsWith("[İPUCU]", ignoreCase = true) || 
                    trimmedLine.startsWith("[TIP]", ignoreCase = true)
        val isSuccess = trimmedLine.startsWith("[BAŞARI]", ignoreCase = true) ||
                        trimmedLine.startsWith("**BAŞARI**", ignoreCase = true)

        if (isWarning || isInfo || isTip || isSuccess) {
            val type = when {
                isWarning -> AlertType.WARNING
                isSuccess -> AlertType.SUCCESS
                isTip -> AlertType.TIP
                else -> AlertType.INFO
            }
            val content = trimmedLine
                .removePrefix("**UYARI:**")
                .removePrefix("**uyari:**")
                .removePrefix("**UYARI**")
                .removePrefix("**HATA:**")
                .removePrefix("**hata:**")
                .removePrefix("**HATA**")
                .removePrefix("**DİKKAT:**")
                .removePrefix("**dikkat:**")
                .removePrefix("**DİKKAT**")
                .removePrefix("[BİLGİ]")
                .removePrefix("[bilgi]")
                .removePrefix("[INFO]")
                .removePrefix("[info]")
                .removePrefix("[İPUCU]")
                .removePrefix("[ipucu]")
                .removePrefix("[TIP]")
                .removePrefix("[tip]")
                .removePrefix("[BAŞARI]")
                .removePrefix("[başarı]")
                .trim()
            blocks.add(ResponseBlock.Alert(content, type, trimmedLine.take(15)))
            index++
            continue
        }

        // 3. Key-Value Rows group parsing
        if (trimmedLine.contains(":") && 
            !trimmedLine.startsWith("http") && 
            trimmedLine.indexOf(":") > 1 && 
            trimmedLine.indexOf(":") < 25 &&
            !trimmedLine.startsWith("-") &&
            !trimmedLine.startsWith("*") &&
            !trimmedLine.matches("^\\d+\\..*".toRegex())
        ) {
            val pairs = mutableListOf<Pair<String, String>>()
            var subIndex = index
            while (subIndex < totalLines) {
                val subLine = lines[subIndex].trim()
                if (subLine.contains(":") && 
                    !subLine.startsWith("http") && 
                    subLine.indexOf(":") > 1 && 
                    subLine.indexOf(":") < 25 &&
                    !subLine.startsWith("-") &&
                    !subLine.startsWith("*") &&
                    !subLine.matches("^\\d+\\..*".toRegex())
                ) {
                    val parts = subLine.split(":", limit = 2)
                    pairs.add(Pair(parts[0].trim(), parts[1].trim()))
                    subIndex++
                } else {
                    break
                }
            }
            if (pairs.isNotEmpty()) {
                blocks.add(ResponseBlock.KeyValueGroup(pairs))
                index = subIndex
                continue
            }
        }

        // 4. Bullet List parsing
        if (trimmedLine.startsWith("-") || trimmedLine.startsWith("*")) {
            val listItems = mutableListOf<String>()
            var subIndex = index
            while (subIndex < totalLines) {
                val subLine = lines[subIndex].trim()
                if (subLine.startsWith("-") || subLine.startsWith("*")) {
                    listItems.add(subLine.substring(1).trim())
                    subIndex++
                } else {
                    break
                }
            }
            if (listItems.isNotEmpty()) {
                blocks.add(ResponseBlock.BulletList(listItems))
                index = subIndex
                continue
            }
        }

        // 5. Numbered List parsing
        if (trimmedLine.matches("^\\d+\\..*".toRegex())) {
            val listItems = mutableListOf<String>()
            var subIndex = index
            while (subIndex < totalLines) {
                val subLine = lines[subIndex].trim()
                if (subLine.matches("^\\d+\\..*".toRegex())) {
                    val itemContent = subLine.replaceFirst("^\\d+\\.\\s*".toRegex(), "").trim()
                    listItems.add(itemContent)
                    subIndex++
                } else {
                    break
                }
            }
            if (listItems.isNotEmpty()) {
                blocks.add(ResponseBlock.NumberedList(listItems))
                index = subIndex
                continue
            }
        }

        // 6. Plain Paragraph (or ignore trailing empty lines)
        if (line.isNotEmpty()) {
            blocks.add(ResponseBlock.Paragraph(line))
        }
        index++
    }
    return blocks
}

// Core Response Block Router Composable
@Composable
fun RenderResponseBlock(block: ResponseBlock, onCopyClicked: (String) -> Unit) {
    when (block) {
        is ResponseBlock.Code -> {
            SciFiCodeCard(block.code, block.language, onCopyClicked)
        }
        is ResponseBlock.Alert -> {
            SciFiAlertCard(block.text, block.type, block.originalHeader)
        }
        is ResponseBlock.KeyValueGroup -> {
            SciFiKeyValueCard(block.pairs)
        }
        is ResponseBlock.BulletList -> {
            SciFiBulletListCard(block.items)
        }
        is ResponseBlock.NumberedList -> {
            SciFiNumberedListCard(block.items)
        }
        is ResponseBlock.Paragraph -> {
            Text(
                text = block.text,
                color = if (LocalSciFiColors.current.isDark) Color.White else Color(0xFF1E293B),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

// Gorgeous glowing code snippet visualization card
@Composable
fun SciFiCodeCard(code: String, language: String, onCopyClicked: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, CosmicPrimary.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .testTag("code_card"),
        colors = CardDefaults.cardColors(
            containerColor = CosmicBackground.copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CosmicPrimary.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Code,
                        contentDescription = null,
                        tint = CosmicPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (language.isNotEmpty()) language.uppercase() else "KOD",
                        color = CosmicPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            onCopyClicked(code)
                        }
                        .background(CosmicSurfaceVariant.copy(alpha = 0.5f))
                        .border(1.dp, CosmicPrimary.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Kodu Kopyala",
                        tint = CosmicPrimaryVariant,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "KOPYALA",
                        color = CosmicPrimaryVariant,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    text = highlightCodeString(code, language),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// Fütüristik alert, uyan, bilgi ve ipucu bildirim kartı
@Composable
fun SciFiAlertCard(text: String, type: AlertType, originalHeader: String) {
    val accentColor = when (type) {
        AlertType.WARNING -> CosmicAccent
        AlertType.SUCCESS -> Color(0xFF10B981)
        AlertType.TIP -> Color(0xFFFBBF24)
        AlertType.INFO -> CosmicPrimary
    }

    val icon = when (type) {
        AlertType.WARNING -> Icons.Filled.Warning
        AlertType.SUCCESS -> Icons.Filled.CheckCircle
        AlertType.TIP -> Icons.Filled.Lightbulb
        AlertType.INFO -> Icons.Filled.Info
    }

    val labelText = when (type) {
        AlertType.WARNING -> "UYARI / DİKKAT"
        AlertType.SUCCESS -> "İŞLEM BAŞARILI"
        AlertType.TIP -> "İPUCU / TAVSİYE"
        AlertType.INFO -> "ÖNEMLİ BİLGİ"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(accentColor.copy(alpha = 0.08f))
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                drawLine(
                    color = accentColor,
                    start = Offset(strokeWidth / 2, 0f),
                    end = Offset(strokeWidth / 2, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .border(1.dp, accentColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag("alert_card"),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = labelText,
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = text,
                color = if (LocalSciFiColors.current.isDark) Color.White else Color(0xFF1E293B),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

// Anahtar - Değer veri ızgarası okuma paneli
@Composable
fun SciFiKeyValueCard(pairs: List<Pair<String, String>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, CosmicPrimary.copy(alpha = 0.18f), RoundedCornerShape(10.dp))
            .testTag("key_value_card"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = CosmicSurface.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pairs.forEach { (key, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = key,
                        color = CosmicPrimaryVariant.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.45f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = value,
                        color = if (LocalSciFiColors.current.isDark) Color.White else Color(0xFF0F172A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(0.55f)
                    )
                }
                if (key != pairs.last().first) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(1.dp)
                            .background(CosmicPrimary.copy(alpha = 0.08f))
                    )
                }
            }
        }
    }
}

// Diamond (Baklava) simgeli liste kartları
@Composable
fun SciFiBulletListCard(items: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("bullet_list_card"),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(6.dp)
                        .rotate(45f)
                        .background(CosmicPrimary)
                        .border(1.dp, CosmicSecondary, RoundedCornerShape(1f))
                )
                Text(
                    text = item,
                    color = if (LocalSciFiColors.current.isDark) Color.White else Color(0xFF1E293B),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// Sayı sayımlı işlem süreci adım kartları
@Composable
fun SciFiNumberedListCard(items: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("numbered_list_card"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEachIndexed { idx, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CosmicPrimary, CosmicSecondary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (idx + 1).toString(),
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = item,
                    color = if (LocalSciFiColors.current.isDark) Color.White else Color(0xFF1E293B),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// Stats / Diagnostics dashboard overlay indicating total messages and interactions for "Jarvis" vibe
@Composable
fun JarvisStatsOverlay(
    sessionsCount: Int,
    totalMessages: Int,
    userMessages: Int,
    modelMessages: Int,
    sentiment: SentimentAnalysis,
    isWakeWordEnabled: Boolean,
    wakeWord: String,
    apiCallsRpm: Int,
    onWakeWordToggle: (Boolean) -> Unit,
    onWakeWordChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalSciFiColors.current
    
    val cpuHistory = remember { androidx.compose.runtime.mutableStateListOf<Float>(22f, 25f, 24f, 30f, 32f, 28f, 24f, 25f, 35f, 30f, 28f, 27f, 35f, 40f, 33f) }
    val memHistory = remember { androidx.compose.runtime.mutableStateListOf<Float>(45f, 46f, 45f, 45f, 46f, 47f, 47f, 46f, 46f, 47f, 48f, 47f, 47f, 47f, 48f) }
    
    var currentCpu by remember { mutableStateOf(28) }
    var currentMemory by remember { mutableStateOf(47) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1200)
            val cpuChange = (15..55).random()
            currentCpu = cpuChange
            if (cpuHistory.size > 18) cpuHistory.removeAt(0)
            cpuHistory.add(cpuChange.toFloat())

            val memChange = (40..51).random()
            currentMemory = memChange
            if (memHistory.size > 18) memHistory.removeAt(0)
            memHistory.add(memChange.toFloat())
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "jarvis_glow_pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(colors.primary, colors.secondary)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("jarvis_stats_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface.copy(alpha = 0.96f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .maxHeight(580.dp) // Maintain beautiful layout balance on all screen sizes
                    .padding(16.dp)
            ) {
                // Header of diagnostics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(colors.primary.copy(alpha = alphaAnim))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SYSTEM METRICS // JARVIS HUD",
                            color = colors.primary,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(24.dp)
                            .testTag("jarvis_stats_close_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close HUD",
                            tint = colors.accent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Cyan circuit-style grid line divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.primary.copy(alpha = 0.25f))
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable metrics content to prevent overflow
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // SEC 1: CORE DATA STREAMS
                    Column {
                        Text(
                            text = "> CORE DATA STREAMS INGESTED",
                            color = if (colors.isDark) Color.LightGray else Color(0xFF475569),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Stats Dashboard Grid
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StatTile(
                                    title = "TOTAL INTERACTIONS",
                                    value = totalMessages.toString(),
                                    subtitle = "Cumulative packet cycles",
                                    icon = Icons.Filled.ChatBubble,
                                    color = colors.primary,
                                    modifier = Modifier.weight(1f)
                                )
                                StatTile(
                                    title = "PROT. SESSIONS",
                                    value = sessionsCount.toString(),
                                    subtitle = "Active neural nodes",
                                    icon = Icons.Filled.Language,
                                    color = colors.primaryVariant,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StatTile(
                                    title = "USER INPUTS",
                                    value = userMessages.toString(),
                                    subtitle = "Direct vocal/text injects",
                                    icon = Icons.Filled.Person,
                                    color = colors.secondary,
                                    modifier = Modifier.weight(1f)
                                )
                                StatTile(
                                    title = "AI COMPUTES",
                                    value = modelMessages.toString(),
                                    subtitle = "Generated neural feedback",
                                    icon = Icons.Filled.AutoAwesome,
                                    color = colors.primary,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StatTile(
                                    title = "API RESOURCE RPM",
                                    value = "$apiCallsRpm RPM",
                                    subtitle = "Calls per minute (limit 15)",
                                    icon = Icons.Filled.Speed,
                                    color = if (apiCallsRpm >= 12) Color.Red else if (apiCallsRpm >= 6) Color(0xFFEAB308) else colors.primary,
                                    modifier = Modifier.weight(1f)
                                )
                                StatTile(
                                    title = "BANDWIDTH DUPLEX",
                                    value = "UPLINK SAFE",
                                    subtitle = "No transmission throttles",
                                    icon = Icons.Filled.CheckCircle,
                                    color = colors.primaryVariant,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // SEC 2: REAL-TIME EMOTIONAL TELEMETRY (SENTIMENT ANALYSIS DASHBOARD)
                    Column {
                        Text(
                            text = "> EMOTIONAL RADAR & TELEMETRY",
                            color = if (colors.isDark) Color.LightGray else Color(0xFF475569),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colors.surfaceVariant.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                                .border(1.dp, colors.secondary.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = sentiment.emoji,
                                            fontSize = 20.sp,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Column {
                                            Text(
                                                text = sentiment.description,
                                                color = colors.secondary,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            val valencePercentage = ((sentiment.score + 1.0f) * 50).toInt()
                                            Text(
                                                text = "VALENCE: $valencePercentage%",
                                                color = if (colors.isDark) Color.LightGray else Color.DarkGray,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = when (sentiment.type) {
                                                    SentimentType.POSITIVE -> Color(0xFF10B981).copy(alpha = 0.15f)
                                                    SentimentType.NEGATIVE -> Color(0xFFEF4444).copy(alpha = 0.15f)
                                                    SentimentType.NEUTRAL -> colors.primary.copy(alpha = 0.15f)
                                                },
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = sentiment.type.name,
                                            color = when (sentiment.type) {
                                                SentimentType.POSITIVE -> Color(0xFF34D399)
                                                SentimentType.NEGATIVE -> Color(0xFFF87171)
                                                SentimentType.NEUTRAL -> colors.primary
                                            },
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Sentiment bar
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .background(colors.surface.copy(alpha = 0.5f), shape = RoundedCornerShape(4.dp))
                                ) {
                                    val progressRatio = ((sentiment.score + 1.0f) / 2f).coerceIn(0f, 1f)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progressRatio)
                                            .fillMaxHeight()
                                            .background(
                                                brush = Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFFEF4444),
                                                        colors.primary,
                                                        Color(0xFF10B981)
                                                    )
                                                ),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Analyzed valence ratio based on active token tone. System operating in ${sentiment.description.lowercase()} mode with ${sentiment.confidence}% calculation confidence.",
                                    color = if (colors.isDark) Color.Gray else Color(0xFF475569),
                                    fontSize = 10.sp,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }

                    // SEC 4: MACHINE RESOURCE CORES // REALTIME
                    Column {
                        Text(
                            text = "> MACHINE RESOURCE CORES // REALTIME",
                            color = if (colors.isDark) Color.LightGray else Color(0xFF475569),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                SciFiResourceGraph(
                                    label = "CPU LOAD CORE",
                                    currentValue = currentCpu,
                                    accentColor = colors.primary,
                                    history = cpuHistory
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                SciFiResourceGraph(
                                    label = "MEMORY COMPUTE",
                                    currentValue = currentMemory,
                                    accentColor = colors.secondary,
                                    history = memHistory
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // SEC 3: VOICE CONFIGURATION (CONTINUOUS WAKE WORD SWITCH)
                    Column {
                        Text(
                            text = "> VOICE UPLINK & WAKE CONFIG",
                            color = if (colors.isDark) Color.LightGray else Color(0xFF475569),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colors.surfaceVariant.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                                .border(1.dp, colors.primary.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Continuous Wake Word",
                                            color = if (colors.isDark) Color.White else Color(0xFF0F172A),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = "Keep background listener active",
                                            color = if (colors.isDark) Color.Gray else Color(0xFF64748B),
                                            fontSize = 10.sp
                                        )
                                    }
                                    Switch(
                                        checked = isWakeWordEnabled,
                                        onCheckedChange = onWakeWordToggle,
                                        modifier = Modifier.graphicsLayer(scaleX = 0.85f, scaleY = 0.85f).testTag("wake_word_toggle"),
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = colors.primary,
                                            checkedTrackColor = colors.primary.copy(alpha = 0.3f)
                                        )
                                    )
                                }

                                if (isWakeWordEnabled) {
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Custom Wake Trigger String:",
                                        color = colors.primary,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )

                                    androidx.compose.foundation.text.BasicTextField(
                                        value = wakeWord,
                                        onValueChange = onWakeWordChange,
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            color = if (colors.isDark) Color.White else Color.Black,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(colors.surface.copy(alpha = 0.7f), shape = RoundedCornerShape(6.dp))
                                            .border(1.dp, colors.primary.copy(alpha = 0.3f), shape = RoundedCornerShape(6.dp))
                                            .padding(8.dp)
                                            .testTag("wake_word_input"),
                                        cursorBrush = androidx.compose.ui.graphics.SolidColor(colors.primary)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF10B981).copy(alpha = alphaAnim))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "STATUS: ACTIVE // LISTENING FOR '$wakeWord'",
                                            color = Color(0xFF34D399),
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(Color.Gray)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "STATUS: STANDBY // WAKE TRIGGER OFF",
                                            color = Color.Gray,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer diagnostics string
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SYS_UPTIME: LIVE // TELEMETRY: REALTIME",
                        color = if (colors.isDark) Color.Gray.copy(alpha = 0.7f) else Color(0xFF64748B),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp
                    )
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("stats_dismiss_btn")
                    ) {
                        Text(
                            text = "DISMISS HUD",
                            color = colors.primary,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatTile(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalSciFiColors.current
    Box(
        modifier = modifier
            .background(colors.surfaceVariant.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
            .border(1.dp, colors.primary.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = if (colors.isDark) Color.LightGray.copy(alpha = 0.8f) else Color(0xFF475569),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(13.dp)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = value,
                color = if (colors.isDark) Color.White else Color(0xFF0F172A),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = if (colors.isDark) Color.Gray else Color(0xFF64748B),
                fontSize = 8.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Particle details model for ambient backgrounds
data class CyberParticle(
    var x: Float,
    var y: Float,
    var radius: Float,
    var speed: Float,
    var alpha: Float
)

// Gorgeous cyber background particle animation
@Composable
fun BackgroundParticles() {
    val colors = LocalSciFiColors.current
    val particleColor = colors.primary
    val particles = remember {
        List(25) {
            CyberParticle(
                x = (0..1000).random() / 1000f,
                y = (0..1000).random() / 1000f,
                radius = (1..3).random().toFloat(),
                speed = (5..15).random() / 100000f,
                alpha = (10..60).random() / 100f
            )
        }
    }

    var frameTime by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            androidx.compose.runtime.withFrameNanos { time ->
                frameTime = time
            }
        }
    }

    // Reference time trigger to invalidate on frame
    val pulseTrigger = frameTime

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        if (width > 0 && height > 0) {
            particles.forEach { p ->
                // Update particle y movement upwards
                p.y -= p.speed
                if (p.y < 0f) {
                    p.y = 1.0f
                    p.x = (0..1000).random() / 1000f
                }
                
                val drawX = p.x * width
                val drawY = p.y * height
                
                drawCircle(
                    color = particleColor,
                    radius = p.radius,
                    center = Offset(drawX, drawY),
                    alpha = p.alpha * 0.25f
                )
            }
        }
    }
}

// Visual scrolling graph showing resource state history
@Composable
fun SciFiResourceGraph(
    label: String,
    currentValue: Int,
    accentColor: Color,
    history: List<Float>
) {
    val colors = LocalSciFiColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(colors.surfaceVariant.copy(alpha = 0.4f), shape = RoundedCornerShape(10.dp))
            .border(1.dp, accentColor.copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    color = if (colors.isDark) Color.LightGray else Color(0xFF475569),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$currentValue%",
                    color = accentColor,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Canvas(modifier = Modifier.fillMaxSize().weight(1f)) {
                val width = size.width
                val height = size.height
                if (width > 0 && height > 0 && history.isNotEmpty()) {
                    val stepX = width / (history.size - 1).coerceAtLeast(1)
                    val path = Path()
                    val fillPath = Path()
                    
                    history.forEachIndexed { idx, pct ->
                        val x = idx * stepX
                        val lineY = height - (pct / 100f * height)
                        if (idx == 0) {
                            path.moveTo(x, lineY)
                            fillPath.moveTo(x, height)
                            fillPath.lineTo(x, lineY)
                        } else {
                            path.lineTo(x, lineY)
                            fillPath.lineTo(x, lineY)
                        }
                        if (idx == history.size - 1) {
                            fillPath.lineTo(x, height)
                            fillPath.close()
                        }
                    }
                    
                    // Draw neon filled gradient background under the path
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
                    
                    // Draw neon stroke path line
                    drawPath(
                        path = path,
                        color = accentColor,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    
                    // Futuristic horizontal reference lines
                    val gridLines = 3
                    for (i in 1..gridLines) {
                        val gridY = height * i / (gridLines + 1)
                        drawLine(
                            color = colors.primary.copy(alpha = 0.05f),
                            start = Offset(0f, gridY),
                            end = Offset(width, gridY),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
        }
    }
}

// Custom code syntax highlighting parser compiling to rich AnnotatedStrings
fun highlightCodeString(code: String, language: String): AnnotatedString {
    val builder = AnnotatedString.Builder(code)
    
    val keywords = setOf(
        "fun", "val", "var", "class", "import", "package", "return", "if", "else", "while", "for", 
        "const", "let", "def", "interface", "struct", "fn", "impl", "pub", "use", "private", "public",
        "protected", "void", "true", "false", "null", "break", "continue", "try", "catch", "throw",
        "lambda", "async", "await", "function", "String", "Int", "Double", "Float", "Boolean"
    )

    val lines = code.split("\n")
    var currentOffset = 0
    lines.forEach { line ->
        val commentIdx = line.indexOf("//")
        if (commentIdx != -1) {
            builder.addStyle(
                style = androidx.compose.ui.text.SpanStyle(
                    color = Color(0xFF6B7280), 
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                start = currentOffset + commentIdx,
                end = currentOffset + line.length
            )
        }
        
        val words = line.split(Regex("[\\s\\Q(){}[]<>;,.+/*=-&|!\\E]"))
        var searchStart = 0
        words.forEach { word ->
            if (word.isNotEmpty()) {
                val wordIdx = line.indexOf(word, searchStart)
                if (wordIdx != -1 && (commentIdx == -1 || wordIdx < commentIdx)) {
                    val wordStart = currentOffset + wordIdx
                    val wordEnd = wordStart + word.length
                    
                    if (word.all { it.isDigit() }) {
                        builder.addStyle(
                            style = androidx.compose.ui.text.SpanStyle(
                                color = Color(0xFFFB923C), 
                                fontWeight = FontWeight.Bold
                            ),
                            start = wordStart,
                            end = wordEnd
                        )
                    } else if (keywords.contains(word)) {
                        builder.addStyle(
                            style = androidx.compose.ui.text.SpanStyle(
                                color = Color(0xFFEC4899), 
                                fontWeight = FontWeight.Bold
                            ),
                            start = wordStart,
                            end = wordEnd
                        )
                    }
                }
                searchStart = (wordIdx + word.length).coerceAtLeast(searchStart + 1)
            }
        }
        
        currentOffset += line.length + 1
    }

    val doubleQuoteRegex = Regex("\"[^\"]*\"")
    doubleQuoteRegex.findAll(code).forEach { result ->
        builder.addStyle(
            style = androidx.compose.ui.text.SpanStyle(color = Color(0xFF10B981)),
            start = result.range.first,
            end = result.range.last + 1
        )
    }

    return builder.toAnnotatedString()
}

@Composable
fun MessageCardInGrid(
    message: ChatMessage,
    isUser: Boolean,
    botIcon: androidx.compose.ui.graphics.vector.ImageVector,
    botPersonalityLabel: String,
    onSpeakClicked: () -> Unit,
    isSpeaking: Boolean,
    onCopyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalSciFiColors.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isUser) colors.secondary.copy(alpha = 0.3f) else colors.primary.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUser) colors.userBubble.copy(alpha = 0.85f) else colors.botBubble.copy(alpha = 0.85f)
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isUser) Icons.Filled.Person else botIcon,
                        contentDescription = null,
                        tint = if (isUser) colors.secondary else colors.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (isUser) "KULLANICI" else botPersonalityLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (isUser) colors.secondary else colors.primary
                    )
                }
                Row {
                    IconButton(
                        onClick = onSpeakClicked,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Filled.VolumeUp else Icons.Filled.VolumeMute,
                            contentDescription = "Seslendir",
                            tint = if (isSpeaking) colors.primary else Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onCopyClicked,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Filled.ContentCopy,
                            contentDescription = "Kopyala",
                            tint = Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message.text,
                fontSize = 11.sp,
                color = Color.White,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun SciFiDashboardPanel(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val colors = LocalSciFiColors.current
    val systemLogs by viewModel.systemLogs.collectAsState()
    val apiCallsRpm by viewModel.apiCallsRpm.collectAsState()
    
    val totalMessages by viewModel.totalMessageCount.collectAsState()
    val messages by viewModel.messages.collectAsState()

    val activeSentiment = remember(messages) {
        val totalText = messages.joinToString("\n") { it.text }
        if (totalText.trim().isEmpty()) {
            SentimentAnalyzer.analyze("merhaba")
        } else {
            SentimentAnalyzer.analyze(totalText)
        }
    }

    val hudWidgets by viewModel.hudWidgets.collectAsState()
    val isCustomizeMode by viewModel.isHudCustomizeMode.collectAsState()

    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    val density = LocalDensity.current.density

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.surface.copy(alpha = 0.96f))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isCustomizeMode) "HUD // YAPILANDIRMA MODU" else "TELEMETRE // HUDRADAR",
                    color = if (isCustomizeMode) colors.accent else colors.primary,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                if (isCustomizeMode) {
                    Text(
                        text = "Tut-sürükle veya okları kullan",
                        color = Color.LightGray.copy(alpha = 0.7f),
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isCustomizeMode) {
                    // Reset widget placement button
                    TextButton(
                        onClick = { viewModel.resetHudWidgetOrder() },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Sıfırla",
                            tint = colors.secondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "SIFIRLA",
                            color = colors.secondary,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Edit/Save toggle button
                IconButton(
                    onClick = { viewModel.setHudCustomizeMode(!isCustomizeMode) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isCustomizeMode) Icons.Filled.Check else Icons.Filled.Settings,
                        contentDescription = if (isCustomizeMode) "Tamam" else "HUD Düzenle",
                        tint = if (isCustomizeMode) Color(0xFF10B981) else colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    if (isCustomizeMode) colors.accent.copy(alpha = 0.5f) 
                    else colors.primary.copy(alpha = 0.2f)
                )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            hudWidgets.forEachIndexed { index, widget ->
                val isDragging = draggedIndex == index
                val itemOffset = if (isDragging) dragOffsetY else 0f
                val scale = if (isDragging) 1.04f else 1f
                val zIndexValue = if (isDragging) 10f else 1f
                val borderAlpha = if (isCustomizeMode) (if (isDragging) 0.8f else 0.4f) else 0f
                val rotationAngle = if (isDragging) -1f else 0f

                Box(
                    modifier = Modifier
                        .zIndex(zIndexValue)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            rotationZ = rotationAngle,
                            translationY = itemOffset * density
                        )
                        .fillMaxWidth()
                        .background(
                            if (isDragging) colors.surfaceVariant.copy(alpha = 0.6f)
                            else if (isCustomizeMode) colors.surfaceVariant.copy(alpha = 0.15f)
                            else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (isCustomizeMode) 1.dp else 0.dp,
                            color = if (isDragging) colors.accent else colors.primary.copy(alpha = borderAlpha),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(if (isCustomizeMode) 8.dp else 0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Customize panel header overlay
                        if (isCustomizeMode) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(colors.surface.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier
                                        .pointerInput(index) {
                                            detectDragGesturesAfterLongPress(
                                                onDragStart = {
                                                    draggedIndex = index
                                                    dragOffsetY = 0f
                                                    viewModel.addLog("HUD_DRAG", "${widget.title} Taşınıyor...", "WARN")
                                                },
                                                onDrag = { change, dragAmount ->
                                                    change.consume()
                                                    dragOffsetY += dragAmount.y / density
                                                    
                                                    // Continuous reorder calculations
                                                    val spacing = 110f // Approximately item spacing in dp
                                                    if (dragOffsetY > spacing && index < hudWidgets.size - 1) {
                                                        val newList = hudWidgets.toMutableList()
                                                        val temp = newList[index]
                                                        newList[index] = newList[index + 1]
                                                        newList[index + 1] = temp
                                                        viewModel.updateHudWidgetOrder(newList)
                                                        draggedIndex = index + 1
                                                        dragOffsetY = 0f
                                                    } else if (dragOffsetY < -spacing && index > 0) {
                                                        val newList = hudWidgets.toMutableList()
                                                        val temp = newList[index]
                                                        newList[index] = newList[index - 1]
                                                        newList[index - 1] = temp
                                                        viewModel.updateHudWidgetOrder(newList)
                                                        draggedIndex = index - 1
                                                        dragOffsetY = 0f
                                                    }
                                                },
                                                onDragEnd = {
                                                    draggedIndex = null
                                                    dragOffsetY = 0f
                                                    viewModel.addLog("HUD_DRAG", "${widget.title} Konumu Sabitlendi.", "SUCCESS")
                                                },
                                                onDragCancel = {
                                                    draggedIndex = null
                                                    dragOffsetY = 0f
                                                }
                                            )
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Taşıma Tutamacı",
                                        tint = if (isDragging) colors.accent else colors.primary.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = widget.title,
                                        fontSize = 8.5.sp,
                                        color = if (isDragging) colors.accent else colors.secondary,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (index > 0) {
                                                val newList = hudWidgets.toMutableList()
                                                val temp = newList[index]
                                                newList[index] = newList[index - 1]
                                                newList[index - 1] = temp
                                                viewModel.updateHudWidgetOrder(newList)
                                            }
                                        },
                                        modifier = Modifier.size(24.dp),
                                        enabled = index > 0
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.KeyboardArrowUp,
                                            contentDescription = "Yukarı Taşı",
                                            tint = if (index > 0) colors.primary else Color.Gray.copy(alpha = 0.5f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            if (index < hudWidgets.size - 1) {
                                                val newList = hudWidgets.toMutableList()
                                                val temp = newList[index]
                                                newList[index] = newList[index + 1]
                                                newList[index + 1] = temp
                                                viewModel.updateHudWidgetOrder(newList)
                                            }
                                        },
                                        modifier = Modifier.size(24.dp),
                                        enabled = index < hudWidgets.size - 1
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.KeyboardArrowDown,
                                            contentDescription = "Aşağı Taşı",
                                            tint = if (index < hudWidgets.size - 1) colors.primary else Color.Gray.copy(alpha = 0.5f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        // Widget contents based on currently ordered HUD config list
                        when (widget) {
                            HudWidget.SYSTEM_METRICS -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (!isCustomizeMode) {
                                        Text(
                                            text = "> SISTEM VERI METRIKLERI",
                                            color = colors.secondary,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(
                                                    colors.surfaceVariant.copy(alpha = 0.3f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("API LIMIT", fontSize = 8.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
                                                Text("$apiCallsRpm / 15 RPM", fontSize = 11.sp, color = colors.primary, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(
                                                    colors.surfaceVariant.copy(alpha = 0.3f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Text("SOZBEYAN", fontSize = 8.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
                                                Text("$totalMessages ADET", fontSize = 11.sp, color = colors.accent, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            HudWidget.SENTIMENT_ANALYSIS -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (!isCustomizeMode) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "> HISSI ANALIZ RADARI",
                                                color = colors.secondary,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = activeSentiment.emoji + " " + activeSentiment.type.name,
                                                color = colors.accent,
                                                fontSize = 9.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                    } else {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "VALANS VE RADAR HİSSİYATI",
                                                color = Color.Gray,
                                                fontSize = 8.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                            Text(
                                                text = activeSentiment.emoji + " " + activeSentiment.type.name,
                                                color = colors.accent,
                                                fontSize = 9.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                    
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .background(colors.surface.copy(alpha = 0.5f), shape = RoundedCornerShape(3.dp))
                                    ) {
                                        val progressRatio = ((activeSentiment.score + 1.0f) / 2f).coerceIn(0f, 1f)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(progressRatio)
                                                .fillMaxHeight()
                                                .background(
                                                    brush = Brush.horizontalGradient(
                                                        colors = listOf(Color(0xFFEF4444), colors.primary, Color(0xFF10B981))
                                                    ),
                                                    shape = RoundedCornerShape(3.dp)
                                                )
                                        )
                                    }
                                    Text(
                                        text = "VALANS: ${((activeSentiment.score + 1.0f) * 50).toInt()}% // BILGI: ${activeSentiment.description}",
                                        fontSize = 8.sp,
                                        color = Color.LightGray,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }

                            HudWidget.DIAGNOSTIC_LOGS -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (!isCustomizeMode) {
                                        Text(
                                            text = "> SISTEM DIAGNOSTIK & SES LOGU",
                                            color = colors.secondary,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                    
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(90.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                            .border(0.5.dp, colors.primary.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                            .padding(6.dp)
                                    ) {
                                        val listState = rememberLazyListState()
                                        LaunchedEffect(systemLogs.size) {
                                            if (systemLogs.isNotEmpty()) {
                                                listState.animateScrollToItem(systemLogs.size - 1)
                                            }
                                        }
                                        
                                        LazyColumn(
                                            state = listState,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            items(systemLogs) { log ->
                                                val logColor = when (log.level) {
                                                    "SUCCESS" -> Color(0xFF10B981)
                                                    "ERROR" -> Color(0xFFEF4444)
                                                    "WARN" -> Color(0xFFEAB308)
                                                    "SEARCH" -> Color(0xFF38BDF8)
                                                    else -> Color.LightGray
                                                }
                                                Text(
                                                    text = "[${log.timestamp}] [${log.tag}] ${log.message}",
                                                    color = logColor,
                                                    fontSize = 8.sp,
                                                    fontFamily = FontFamily.Monospace,
                                                    lineHeight = 11.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            HudWidget.QUICK_ACTIONS -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.addLog("VOICE", "Ses Algılandı: 'Sistemi tetikle'", "SUCCESS")
                                            viewModel.sendMessage("Jarvis, sistem durumunu, API limitini ve mazi akışını özetle")
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary),
                                        border = BorderStroke(0.5.dp, colors.primary.copy(alpha = 0.4f)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(1.dp)
                                    ) {
                                        Text(
                                            text = "SES KOMUTU TETIKLE",
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.addLog("SYS", "Sistem taraması başlatıldı. Donanım stabilize.", "INFO")
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.secondary),
                                        border = BorderStroke(0.5.dp, colors.secondary.copy(alpha = 0.4f)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(1.dp)
                                    ) {
                                        Text(
                                            text = "TEHŞIS ÇALIŞTIR",
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


