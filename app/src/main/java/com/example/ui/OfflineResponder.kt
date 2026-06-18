package com.example.ui

import java.text.SimpleDateFormat
import java.util.*

object OfflineResponder {
    fun respondOffline(input: String, lang: String): String {
        val query = input.lowercase(Locale.getDefault()).trim()
        val isEn = lang == "en"

        // Keyword: Time
        if (query.contains("saat") || query.contains("time")) {
            val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            return if (isEn) {
                "⏱️ **LOCAL CHRONOMETER ENGINE:**\nCurrent system time is **$timeStr**. All timers synchronized with local clock."
            } else {
                "⏱️ **LOKAL KRONOMETRE ÇEKİRDEĞİ:**\nMevcut sistem saati **$timeStr**. Tüm zamanlayıcılar yerel donanım saatiyle eşitlendi."
            }
        }

        // Keyword: Date
        if (query.contains("tarih") || query.contains("date") || query.contains("gün")) {
            val dateStr = SimpleDateFormat("EEEE, d MMMM yyyy", Locale(lang)).format(Date())
            return if (isEn) {
                "📅 **LOCAL CALENDAR:**\nToday's date is **$dateStr**. No network drift detected."
            } else {
                "📅 **YEREL TAKVİM:**\nBugün **$dateStr**. Ağ gecikmesi veya sapma tespit edilmedi."
            }
        }

        // Keyword: System / Diagnostics
        if (query.contains("sistem") || query.contains("system") || query.contains("durum") || query.contains("diag") || query.contains("teşhis")) {
            val freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024)
            val maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024)
            return if (isEn) {
                """
                📟 **J.A.R.V.I.S. SYSTEM TELEMETRY (OFFLINE):**
                - **Sistem Durumu:** STABLE / STABILIZED
                - **Bellek Dağılımı:** ${maxMem - freeMem}MB kullanılmaktadır (Maks: ${maxMem}MB)
                - **Mikroişlemci Modu:** Yerel Güvenli Emergency Core
                - **Ağ Durumu:** ÇEVRİMDIŞI (Offline)
                - **Bilişsel Matris:** CPU-tabanlı Sola Tarama aktif
                """.trimIndent()
            } else {
                """
                📟 **J.A.R.V.I.S. SİSTEM TELEMETRİSİ (ÇEVRİMDIŞI):**
                - **Sistem Durumu:** KARARLI / STABİLİZE
                - **Bellek Tahsisi:** ${maxMem - freeMem}MB aktif kullanımda (Maks: ${maxMem}MB)
                - **İşlemci Modu:** Yerel Güvenli Acil Durum Çekirdeği
                - **Ağ Durumu:** ÇEVRİMDIŞI (Offline Mod)
                - **Bilişsel Matris:** Donanım tabanlı Yerel Analiz aktif
                """.trimIndent()
            }
        }

        // Keyword: Location / Who am I
        if (query.contains("nerede") || query.contains("where") || query.contains("kim") || query.contains("who")) {
            return if (isEn) {
                "🤖 **LOCAL AUTHENTICATION:**\nI am the local backup core of **J.A.R.V.I.S.** running directly in your device's sandboxed environment. Due to offline conditions, some advanced neural pathways are hibernating."
            } else {
                "🤖 **YEREL VERİ DOĞRULAMA:**\nBen cihazınızın güvenli kum havuzunda çalışan **J.A.R.V.I.S.** yerel yedek çekirdeğiyim. Çevrimdışı koşullar nedeniyle gelişmiş bulut sinir yolları uyku modunda."
            }
        }

        // Keyword: Help
        if (query.contains("yardım") || query.contains("help") || query.contains("kılavuz") || query.contains("guide")) {
            return if (isEn) {
                """
                💡 **OFFLINE MODE ENGINE CAPABILITIES:**
                When running without internet, you can query J.A.R.V.I.S for:
                1. **system / state** - View offline hardware memory & CPU diagnostic
                2. **time / chronometer** - Retrieve instant system timestamp
                3. **date** - Standard calendar query
                4. **identity / location** - Information about this local sandbox
                """.trimIndent()
            } else {
                """
                💡 **ÇEVRİMDIŞI MOD YETENEKLERİ:**
                İnternet bağlantısı yokken J.A.R.V.I.S.'e şu sorguları sorabilirsiniz:
                1. **sistem / durum / teşhis** - Donanım bellek ve CPU durumunu göster
                2. **saat / zaman** - Anlık hassas sistem saati
                3. **tarih** - Standart takvim sorgusu
                4. **kimlik / neredesin** - Bu yerel kum havuzu hakkında bilgi
                """.trimIndent()
            }
        }

        // Fallback responses
        val backupsTr = listOf(
            "📡 **UYARI: Ağ bağlantısı kurulamadı.**\nMesajınızı aldım ancak Gemini sunucuları çevrimdışı. Lütfen internetinizi kontrol edin veya çevrimdışı komutları ('teşhis', 'saat', 'yardım') deneyin.",
            "🛰️ **LOKAL GÜVENLİ ÇEKİRDEK RAPORU:**\nAğ geçidi aktif değil. Çevrimdışı modda '$input' sorgusu sınırlandırılmıştır.",
            "🛡️ **SİBER KALKAN AKTİF:**\nYerel J.A.R.V.I.S. motoru şu anda dış ağlardan yalıtılmıştır. Çevrimdışı asistan olarak hizmet vermekteyim."
        )

        val backupsEn = listOf(
            "📡 **WARNING: Network disconnected.**\nI received your transmission, but Gemini servers are unreachable. Please check your link or test local commands like 'system', 'time', or 'help'.",
            "🛰️ **LOCAL SAFE CORE REPORT:**\nGateway link is down. Under offline constraints, searching for '$input' is restricted.",
            "🛡️ **CYBER SHIELD ACTIVE:**\nThe local J.A.R.V.I.S. matrix is isolated. I am serving you as your secure, offline guardian."
        )

        val list = if (isEn) backupsEn else backupsTr
        val idx = Math.abs(query.hashCode()) % list.size
        return list[idx]
    }
}
