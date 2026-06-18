package com.example.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.api.Content
import com.example.api.GeminiRequest
import com.example.api.GenerationConfig
import com.example.api.Part
import com.example.api.RetrofitClient
import com.example.data.AppDatabase
import com.example.data.ChatMessage
import com.example.data.ChatRepository
import com.example.data.ChatSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SystemLog(
    val timestamp: String,
    val tag: String,
    val message: String,
    val level: String = "INFO"
)

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChatRepository
    private val sharedPrefs = application.getSharedPreferences("cosmic_chat_prefs", android.content.Context.MODE_PRIVATE)

    private val _hudWidgets = MutableStateFlow<List<HudWidget>>(emptyList())
    val hudWidgets: StateFlow<List<HudWidget>> = _hudWidgets.asStateFlow()

    private val _isHudCustomizeMode = MutableStateFlow(false)
    val isHudCustomizeMode: StateFlow<Boolean> = _isHudCustomizeMode.asStateFlow()

    private val apiCallTimestamps = mutableListOf<Long>()
    private val _apiCallsRpm = MutableStateFlow(0)
    val apiCallsRpm: StateFlow<Int> = _apiCallsRpm.asStateFlow()

    private val _systemLogs = MutableStateFlow<List<SystemLog>>(listOf(
        SystemLog(java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault()).format(java.util.Date()), "SYS", "J.A.R.V.I.S Sinir Ağları ve Teşhis Portu aktif.", "SUCCESS"),
        SystemLog(java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault()).format(java.util.Date()), "SYS", "Donanım Hızlandırma ve Neural Engine başlatıldı.", "INFO")
    ))
    val systemLogs: StateFlow<List<SystemLog>> = _systemLogs.asStateFlow()

    fun addLog(tag: String, message: String, level: String = "INFO") {
        val sdf = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())
        val timeStr = sdf.format(java.util.Date())
        val newLog = SystemLog(timeStr, tag, message, level)
        _systemLogs.update { current ->
            (listOf(newLog) + current).take(200)
        }
    }

    private val _appLanguage = MutableStateFlow(sharedPrefs.getString("app_language", "tr") ?: "tr")
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    private val _isOfflineMode = MutableStateFlow(sharedPrefs.getBoolean("is_offline_mode", false))
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    private val defaultShortcutsSet = setOf(
        "Bana Kotlin Coroutine anlatabilir misin?",
        "Compose ile dalgalı neon buton yapımı.",
        "Açıklamalı 3 günlük tatil planı.",
        "Yazılım mülakat sorusu sor."
    )
    private val _customShortcuts = MutableStateFlow(
        sharedPrefs.getStringSet("custom_shortcuts", defaultShortcutsSet)?.toList() ?: defaultShortcutsSet.toList()
    )
    val customShortcuts: StateFlow<List<String>> = _customShortcuts.asStateFlow()

    private val _isLockEnabled = MutableStateFlow(sharedPrefs.getBoolean("is_lock_enabled", false))
    val isLockEnabled: StateFlow<Boolean> = _isLockEnabled.asStateFlow()

    private val _appPin = MutableStateFlow(sharedPrefs.getString("app_pin", "1234") ?: "1234")
    val appPin: StateFlow<String> = _appPin.asStateFlow()

    private val _isAppLocked = MutableStateFlow(sharedPrefs.getBoolean("is_lock_enabled", false))
    val isAppLocked: StateFlow<Boolean> = _isAppLocked.asStateFlow()

    fun setAppLanguage(lang: String) {
        _appLanguage.value = lang
        sharedPrefs.edit().putString("app_language", lang).apply()
        addLog("SYS", if (lang == "en") "System interface language configured: ENGLISH" else "Sistem arayüz dili yapılandırıldı: TÜRKÇE", "SUCCESS")
    }

    fun setOfflineMode(enabled: Boolean) {
        _isOfflineMode.value = enabled
        sharedPrefs.edit().putBoolean("is_offline_mode", enabled).apply()
        addLog("SYS", if (enabled) "OFFLINE EMERGENCY CORE ACTIVATED" else "AĞ BAĞLANTISI GÜVENLİK MODELİ AKTİF", if (enabled) "WARN" else "SUCCESS")
    }

    fun addCustomShortcut(shortcut: String) {
        if (shortcut.trim().isEmpty()) return
        val currentSet = sharedPrefs.getStringSet("custom_shortcuts", defaultShortcutsSet) ?: defaultShortcutsSet
        val updatedSet = currentSet.toMutableSet()
        updatedSet.add(shortcut.trim())
        sharedPrefs.edit().putStringSet("custom_shortcuts", updatedSet).apply()
        _customShortcuts.value = updatedSet.toList()
        addLog("SHORTCUT", "Hızlı yönlendirme kısayolu eklendi: '${shortcut.take(20)}'", "SUCCESS")
    }

    fun removeCustomShortcut(shortcut: String) {
        val currentSet = sharedPrefs.getStringSet("custom_shortcuts", defaultShortcutsSet) ?: defaultShortcutsSet
        val updatedSet = currentSet.toMutableSet()
        updatedSet.remove(shortcut)
        sharedPrefs.edit().putStringSet("custom_shortcuts", updatedSet).apply()
        _customShortcuts.value = updatedSet.toList()
        addLog("SHORTCUT", "Hızlı yönlendirme kısayolu silindi: '${shortcut.take(20)}'", "WARN")
    }

    fun resetShortcuts() {
        sharedPrefs.edit().putStringSet("custom_shortcuts", defaultShortcutsSet).apply()
        _customShortcuts.value = defaultShortcutsSet.toList()
        addLog("SHORTCUT", "Kısayol listesi fabrika ayarlarına sıfırlandı.", "INFO")
    }

    fun setLockEnabled(enabled: Boolean) {
        _isLockEnabled.value = enabled
        sharedPrefs.edit().putBoolean("is_lock_enabled", enabled).apply()
        if (!enabled) {
            _isAppLocked.value = false
        }
        addLog("SECURITY", if (enabled) "Biyometrik / PIN koruma kilidi etkinleştirildi." else "Güvenlik kilidi devredışı bırakıldı.", if (enabled) "SUCCESS" else "WARN")
    }

    fun setAppPin(pin: String) {
        if (pin.length == 4 && pin.all { it.isDigit() }) {
            _appPin.value = pin
            sharedPrefs.edit().putString("app_pin", pin).apply()
            addLog("SECURITY", "Erişim şifresi (PIN) güncellendi.", "SUCCESS")
        }
    }

    fun lockApp() {
        if (_isLockEnabled.value) {
            _isAppLocked.value = true
            addLog("SECURITY", "Güvenlik protokolü tetiklendi: Uygulama koruma kalkanına alındı.", "WARN")
        }
    }

    fun unlockApp(pin: String): Boolean {
        return if (pin == _appPin.value) {
            _isAppLocked.value = false
            addLog("SECURITY", "Erişim Yetkilendirildi: J.A.R.V.I.S. sistemleri açıldı.", "SUCCESS")
            true
        } else {
            addLog("SECURITY", "YETKİSİZ ERİŞİM DENEMESİ! Şifre doğrulanamadı.", "ERROR")
            false
        }
    }

    private val _themePreset = MutableStateFlow(sharedPrefs.getString("theme_preset", "Cosmic Dark (Standart)") ?: "Cosmic Dark (Standart)")
    val themePreset: StateFlow<String> = _themePreset.asStateFlow()

    fun setThemePreset(preset: String) {
        _themePreset.value = preset
        sharedPrefs.edit().putString("theme_preset", preset).apply()
        if (preset == "Cosmic Light") {
            _isDarkMode.value = false
            sharedPrefs.edit().putBoolean("is_dark_mode", false).apply()
        } else {
            _isDarkMode.value = true
            sharedPrefs.edit().putBoolean("is_dark_mode", true).apply()
        }
        addLog("THEME", "Arayüz tema seti değiştirildi: $preset", "SUCCESS")
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ChatRepository(database.chatDao())

        // HUD widget order initialization
        val savedCsv = sharedPrefs.getString("hud_widgets_order_csv", null)
        val initialOrder = if (!savedCsv.isNullOrBlank()) {
            savedCsv.split(",").mapNotNull { id ->
                HudWidget.values().find { it.id == id }
            }
        } else {
            emptyList()
        }
        _hudWidgets.value = if (initialOrder.size == HudWidget.values().size) {
            initialOrder
        } else {
            HudWidget.values().toList()
        }

        viewModelScope.launch {
            while (true) {
                delay(1000)
                updateRpmCount()
            }
        }
    }

    private fun updateRpmCount() {
        val now = System.currentTimeMillis()
        val oneMinuteAgo = now - 60_000
        synchronized(apiCallTimestamps) {
            apiCallTimestamps.removeAll { it < oneMinuteAgo }
            _apiCallsRpm.value = apiCallTimestamps.size
        }
    }

    fun recordApiCall() {
        synchronized(apiCallTimestamps) {
            apiCallTimestamps.add(System.currentTimeMillis())
            updateRpmCount()
        }
    }

    // List of all chat sessions
    val sessions: StateFlow<List<ChatSession>> = repository.allSessions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _globalSearchQuery = MutableStateFlow("")
    val globalSearchQuery: StateFlow<String> = _globalSearchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val globalSearchResults: StateFlow<List<ChatMessage>> = _globalSearchQuery
        .flatMapLatest { query ->
            if (query.trim().length < 2) {
                flowOf(emptyList())
            } else {
                repository.searchAllMessages(query.trim())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setGlobalSearchQuery(query: String) {
        _globalSearchQuery.value = query
        if (query.isNotEmpty()) {
            addLog("SEARCH", "Küresel arama sorgusu: '$query'", "INFO")
        }
    }

    private val _isDashboardExpanded = MutableStateFlow(false)
    val isDashboardExpanded: StateFlow<Boolean> = _isDashboardExpanded.asStateFlow()

    fun setDashboardExpanded(expanded: Boolean) {
        _isDashboardExpanded.value = expanded
        addLog("DASHBOARD", "Sistem Gösterge Paneli (Dashboard): ${if (expanded) "GENİŞLETİLDİ" else "KAPATILDI"}", "INFO")
    }

    private val _isGridLayoutEnabled = MutableStateFlow(false)
    val isGridLayoutEnabled: StateFlow<Boolean> = _isGridLayoutEnabled.asStateFlow()

    fun toggleGridLayout() {
        val newValue = !_isGridLayoutEnabled.value
        _isGridLayoutEnabled.value = newValue
        addLog("LAYOUT", "Grid/Mozaik Görünümü: ${if (newValue) "AKTİF" else "PASİF"}", "SUCCESS")
    }

    private val _currentSessionId = MutableStateFlow<Int?>(null)
    val currentSessionId: StateFlow<Int?> = _currentSessionId.asStateFlow()

    // Current active session
    private val _currentSession = MutableStateFlow<ChatSession?>(null)
    val currentSession: StateFlow<ChatSession?> = _currentSession.asStateFlow()

    // Messages for active session
    @OptIn(ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<ChatMessage>> = _currentSessionId
        .flatMapLatest { id ->
            if (id != null) {
                repository.getMessagesForSession(id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(sharedPrefs.getBoolean("is_dark_mode", true))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isWakeWordEnabled = MutableStateFlow(sharedPrefs.getBoolean("is_wake_word_enabled", false))
    val isWakeWordEnabled: StateFlow<Boolean> = _isWakeWordEnabled.asStateFlow()

    private val _wakeWord = MutableStateFlow(sharedPrefs.getString("wake_word", "Okey Jarvis") ?: "Okey Jarvis")
    val wakeWord: StateFlow<String> = _wakeWord.asStateFlow()

    fun setWakeWordEnabled(enabled: Boolean) {
        _isWakeWordEnabled.value = enabled
        sharedPrefs.edit().putBoolean("is_wake_word_enabled", enabled).apply()
        addLog("WAKEWORD", "Ok Jarvis uyandırma motoru: ${if (enabled) "AKTİF" else "KAPALI"}", if (enabled) "SUCCESS" else "INFO")
    }

    fun setWakeWord(word: String) {
        _wakeWord.value = word
        sharedPrefs.edit().putString("wake_word", word).apply()
        addLog("WAKEWORD", "Sihirli uyandırma ifadesi ayarlandı: $word", "INFO")
    }

    private val _ttsLanguageCode = MutableStateFlow(sharedPrefs.getString("tts_language_code", "tr") ?: "tr")
    val ttsLanguageCode: StateFlow<String> = _ttsLanguageCode.asStateFlow()

    fun setTtsLanguageCode(code: String) {
        _ttsLanguageCode.value = code
        sharedPrefs.edit().putString("tts_language_code", code).apply()
        addLog("VOICE", "Konuşma sentezleyici (TTS) dili: $code", "INFO")
    }

    private val _isContextCachingEnabled = MutableStateFlow(sharedPrefs.getBoolean("is_context_caching", true))
    val isContextCachingEnabled: StateFlow<Boolean> = _isContextCachingEnabled.asStateFlow()

    fun setContextCachingEnabled(enabled: Boolean) {
        _isContextCachingEnabled.value = enabled
        sharedPrefs.edit().putBoolean("is_context_caching", enabled).apply()
        addLog("CACHE", "Smart Context Caching: ${if (enabled) "AÇIK" else "KAPALI"}", "INFO")
    }

    val totalMessageCount: StateFlow<Int> = repository.totalMessageCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val userMessageCount: StateFlow<Int> = repository.userMessageCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val modelMessageCount: StateFlow<Int> = repository.modelMessageCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val activeSessionSentiment: StateFlow<SentimentAnalysis> = messages
        .map { msgList ->
            val userTexts = msgList.filter { it.role == "user" }.map { it.text }
            if (userTexts.isEmpty()) {
                SentimentAnalysis(0f, SentimentType.NEUTRAL, "STABLE / CALM", "😐", 100)
            } else {
                val fullText = userTexts.joinToString(" ")
                SentimentAnalyzer.analyze(fullText)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SentimentAnalysis(0f, SentimentType.NEUTRAL, "STABLE / CALM", "😐", 100)
        )

    private val _voiceInputTriggerEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val voiceInputTriggerEvent = _voiceInputTriggerEvent.asSharedFlow()

    fun triggerVoiceInput() {
        viewModelScope.launch {
            _voiceInputTriggerEvent.emit(Unit)
            addLog("VOICE", "Sesli dinleme motoru tetiklendi", "INFO")
        }
    }

    fun toggleTheme() {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        sharedPrefs.edit().putBoolean("is_dark_mode", newValue).apply()
        val suffix = if (newValue) "Cosmic Dark (Standart)" else "Cosmic Light"
        _themePreset.value = suffix
        addLog("THEME", "Ekran görünüm modu değiştirildi. Aktif: $suffix", "SUCCESS")
    }

    // Helper state to check if API key is valid / exists
    val isApiKeyConfigured: Boolean
        get() = BuildConfig.GEMINI_API_KEY.isNotEmpty() && 
                BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"

    init {
        // Automatically select the most recent session or create one if empty
        viewModelScope.launch {
            sessions.collect { list ->
                if (_currentSessionId.value == null && list.isNotEmpty()) {
                    selectSession(list.first().id)
                }
            }
        }
    }

    fun selectSession(sessionId: Int) {
        _currentSessionId.value = sessionId
        viewModelScope.launch {
            val session = repository.getSessionById(sessionId)
            _currentSession.value = session
            if (session != null) {
                addLog("DB", "Oturum seçildi: ${session.title} (ID: $sessionId)", "INFO")
            }
        }
    }

    fun createNewSession(title: String = "Yeni Sohbet") {
        viewModelScope.launch {
            val defaultSystemDoc = sharedPrefs.getString(
                "selected_personality",
                "Sen kullanıcıya her konuda yardımcı olan, cana yakın ve çok zeki bir yapay zeka asistanısın."
            ) ?: "Sen kullanıcıya her konuda yardımcı olan, cana yakın ve çok zeki bir yapay zeka asistanısın."
            val newSession = ChatSession(
                title = title,
                model = "gemini-3.5-flash",
                temperature = 0.7f,
                systemInstruction = defaultSystemDoc
            )
            val newId = repository.insertSession(newSession)
            _currentSessionId.value = newId.toInt()
            _currentSession.value = newSession.copy(id = newId.toInt())
            _errorMessage.value = null
            addLog("DB", "Yeni sohbet oturumu oluşturuldu (ID: $newId)", "SUCCESS")
        }
    }

    fun updateSessionConfigs(model: String, temperature: Float, systemInstruction: String) {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            val updated = session.copy(
                model = model,
                temperature = temperature,
                systemInstruction = systemInstruction
            )
            repository.updateSession(updated)
            _currentSession.value = updated
            sharedPrefs.edit().putString("selected_personality", systemInstruction).apply()
            addLog("SYS", "Asistan yapılandırması güncellendi (Model: $model, Isı: $temperature)", "SUCCESS")
        }
    }

    fun renameSession(sessionId: Int, newTitle: String) {
        viewModelScope.launch {
            val session = repository.getSessionById(sessionId)
            if (session != null) {
                repository.updateSession(session.copy(title = newTitle))
                if (_currentSessionId.value == sessionId) {
                    _currentSession.value = session.copy(title = newTitle)
                }
                addLog("DB", "Oturum yeniden adlandırıldı: '$newTitle'", "INFO")
            }
        }
    }

    fun deleteSession(session: ChatSession) {
        viewModelScope.launch {
            repository.deleteSession(session)
            addLog("DB", "Oturum silindi: '${session.title}' (ID: ${session.id})", "WARN")
            if (_currentSessionId.value == session.id) {
                val remaining = sessions.value.filter { it.id != session.id }
                if (remaining.isNotEmpty()) {
                    selectSession(remaining.first().id)
                } else {
                    _currentSessionId.value = null
                    _currentSession.value = null
                }
            }
        }
    }

    fun clearActiveMessages() {
        val sessionId = _currentSessionId.value ?: return
        viewModelScope.launch {
            repository.deleteMessagesForSession(sessionId)
            addLog("DB", "Aktif oturumdaki tüm mesaj geçmişi temizlendi (ID: $sessionId)", "WARN")
        }
    }

    fun sendMessage(text: String) {
        val sessionId = _currentSessionId.value ?: return
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            addLog("DB", "Kullanıcı mesajı gönderildi: '${text.take(25)}...'", "INFO")

            // 1. Save user message to database
            val userMsg = ChatMessage(
                sessionId = sessionId,
                role = "user",
                text = text.trim()
            )
            repository.insertMessage(userMsg)

            // Auto rename title if it's the first message and template title was used
            val activeSession = _currentSession.value
            val activeMsgs = messages.value
            if (activeSession != null && (activeSession.title == "Yeni Sohbet" || activeSession.title.trim().isEmpty()) && activeMsgs.isEmpty()) {
                val shortTitle = if (text.length > 20) text.take(18) + "..." else text
                renameSession(sessionId, shortTitle)
            }

            // Offline Mode Interceptor
            if (_isOfflineMode.value) {
                kotlinx.coroutines.delay(800)
                val replyText = OfflineResponder.respondOffline(text.trim(), _appLanguage.value)
                val modelMsg = ChatMessage(
                    sessionId = sessionId,
                    role = "model",
                    text = replyText
                )
                repository.insertMessage(modelMsg)
                val notifTitle = if (_appLanguage.value == "en") "⚡ J.A.R.V.I.S. Offline Core" else "⚡ J.A.R.V.I.S. Yerel Motor"
                sendNotification(notifTitle, replyText.take(100) + if (replyText.length > 100) "..." else "")
                addLog("OFFLINE", "Çevrimdışı yerel motor yanıtı üretti. Karakter: ${replyText.length}", "SUCCESS")
                _isLoading.value = false
                return@launch
            }

            // 2. Prepare request data
            val activeMessages = repository.getMessagesForSession(sessionId).first()
            val currentSessionDetails = repository.getSessionById(sessionId) ?: activeSession ?: return@launch

            val rawContents = if (_isContextCachingEnabled.value && activeMessages.size > 10) {
                addLog("CACHE", "Context Caching Kararı: ${activeMessages.size} mesaj analiz edildi. Önbelleğe alınıyor...", "SUCCESS")
                val keptFirst = activeMessages.take(2)
                val keptLast = activeMessages.takeLast(6)
                val resultList = mutableListOf<ChatMessage>()
                resultList.addAll(keptFirst)
                resultList.add(ChatMessage(
                    id = -999,
                    sessionId = sessionId,
                    role = "model",
                    text = "[Sistem Bilgisi: Önceki sohbet geçmişi akıllı önbelleğe (smart context cache) alındı ve tokenize edildi. Lütfen bu bağlamın bütünlüğünü koruyarak devam et.]"
                ))
                resultList.addAll(keptLast)
                resultList
            } else {
                activeMessages
            }

            val contents = rawContents.map { msg ->
                Content(
                    role = if (msg.role == "user") "user" else "model",
                    parts = listOf(Part(text = msg.text))
                )
            }

            val systemDocText = currentSessionDetails.systemInstruction
            val contextBlock = """
                
                [SİSTEM_BAĞLAM_MOTORU - AKTİF]
                Aktif Tarih ve Saat: ${java.text.SimpleDateFormat("EEEE, d MMMM yyyy HH:mm", java.util.Locale("tr", "TR")).format(java.util.Date())}
                Aktif Tema: ${if (_isDarkMode.value) "Kozmik Karanlık (Animasyonlu, Uzay/Sci-Fi temalı)" else "Kozmik Aydınlık (Açık Tema)"}
                Donanım/Arayüz Yeteneği: Ses tanıma (mikrofon) ve ses sentezi (konuşma/hoparlör) modülleri kullanıcı arayüzünde mevcuttur.
                Görsel Kart Yetenekleri: Kullanıcı arayüzünde Gelişmiş Görsel Cevap Kartları (Visual Response Cards) entegre edilmiştir. Yanıtlarında kod blokları (```kod```), listeler (- madde veya 1. sıra şeklinde), önemli uyarılar (**UYARI:** / **HATA:** / **DİKKAT:**), bilgi başlıkları ([BİLGİ] / [İPUCU] şeklinde) veya alt alta anahtar-değer çiftleri (örneğin "Anahtar: Değer") şeklinde yapılar hazırlarsan, sistem bunları kullanıcının ekranında çok havalı, renkli, sınır çizgili, gölgeli ve kopyalanabilir fütüristik bilgi kartlarına dönüştürecektir. Lütfen yanıtlarında bu formatları proaktif olarak kullan!
            """.trimIndent()

            val finalSystemInstructionText = if (systemDocText.isNotEmpty()) {
                "$systemDocText\n\n$contextBlock"
            } else {
                contextBlock
            }

            val systemInstruction = Content(parts = listOf(Part(text = finalSystemInstructionText)))

            val request = GeminiRequest(
                contents = contents,
                generationConfig = GenerationConfig(
                    temperature = currentSessionDetails.temperature,
                    maxOutputTokens = 2048
                ),
                systemInstruction = systemInstruction
            )

            // 3. Make the API Call in background dispatcher
            recordApiCall()
            addLog("API", "Gemini AI çağrısı başlatılıyor [Model: ${currentSessionDetails.model}, Isı: ${currentSessionDetails.temperature}]", "INFO")
            withContext(Dispatchers.IO) {
                try {
                    val apiKey = BuildConfig.GEMINI_API_KEY
                    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                        throw IllegalStateException("API Anahtarı bulunamadı. Lütfen AI Studio Secrets panelinden 'GEMINI_API_KEY' değişkenini yapılandırın.")
                    }

                    val response = RetrofitClient.service.generateContent(
                        model = currentSessionDetails.model,
                        apiKey = apiKey,
                        request = request
                    )

                    val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (responseText != null) {
                        // Save model response to database
                        val modelMsg = ChatMessage(
                            sessionId = sessionId,
                            role = "model",
                            text = responseText
                        )
                        repository.insertMessage(modelMsg)
                        sendNotification("⚡ J.A.R.V.I.S. Yanıtı Hazır", responseText.take(100) + if (responseText.length > 100) "..." else "")
                        addLog("API", "Gemini API yanıtı başarıyla işlendi (${responseText.length} karakter)", "SUCCESS")
                    } else {
                        throw Exception(response.candidates?.firstOrNull()?.finishReason ?: "Boş yanıt alındı.")
                    }
                } catch (e: Exception) {
                    val errMsg = e.localizedMessage ?: "Bilinmeyen bir hata oluştu."
                    Log.e("ChatViewModel", "Gemini API Error", e)
                    _errorMessage.value = errMsg
                    addLog("API", "Gemini API bağlantı hatası: $errMsg", "ERROR")
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun sendNotification(title: String, messageText: String) {
        val context = getApplication<Application>()
        val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "jarvis_cyber_notifications"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "J.A.R.V.I.S Cyber Uplink",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "System notification channel for J.A.R.V.I.S updates"
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setContentTitle(title)
            .setContentText(messageText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationId = (System.currentTimeMillis() % 100000).toInt()
        notificationManager.notify(notificationId, builder.build())
    }

    fun saveSessionDraft(sessionId: Int, text: String) {
        sharedPrefs.edit().putString("draft_session_$sessionId", text).apply()
    }

    fun getSessionDraft(sessionId: Int): String {
        return sharedPrefs.getString("draft_session_$sessionId", "") ?: ""
    }

    fun setHudCustomizeMode(active: Boolean) {
        _isHudCustomizeMode.value = active
        addLog("HUD_SETUP", "HUD Özelleştirme Modu: ${if (active) "AKTİFLEŞTİRİLDİ" else "KAPATILDI"}", if (active) "WARN" else "SUCCESS")
    }

    fun updateHudWidgetOrder(newOrder: List<HudWidget>) {
        _hudWidgets.value = newOrder
        val csv = newOrder.joinToString(",") { it.id }
        sharedPrefs.edit().putString("hud_widgets_order_csv", csv).apply()
        addLog("HUD_SETUP", "HUD widget yerleşimi güncellendi.", "SUCCESS")
    }

    fun resetHudWidgetOrder() {
        val defaultOrder = HudWidget.values().toList()
        updateHudWidgetOrder(defaultOrder)
        addLog("HUD_SETUP", "HUD widget yerleşimi varsayılana sıfırlandı.", "INFO")
    }
}

enum class HudWidget(val id: String, val title: String) {
    SYSTEM_METRICS("system_metrics", "SİSTEM VERİ METRİKLERİ"),
    SENTIMENT_ANALYSIS("sentiment_analysis", "HİSSİ ANALİZ RADARI"),
    DIAGNOSTIC_LOGS("diagnostic_logs", "SİSTEM DIAGNOSTİK & SES LOGU"),
    QUICK_ACTIONS("quick_actions", "HIZLI AKSİYONLAR")
}
