package com.example.ui

import java.util.Locale

enum class SentimentType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}

data class SentimentAnalysis(
    val score: Float, // -1.0f to 1.0f
    val type: SentimentType,
    val description: String,
    val emoji: String,
    val confidence: Int // 0 to 100%
)

object SentimentAnalyzer {

    private val positiveWords = setOf(
        "güzel", "teşekkür", "harika", "iyi", "başarılı", "süper", "muhteşem", 
        "mükemmel", "sevindim", "mutlu", "kolay", "seviyorum", "sağol", "sağolasın", 
        "ellerine sağlık", "en iyi", "beğendim", "harikulade", "bravo",
        "great", "awesome", "good", "nice", "thanks", "thank you", "excellent", 
        "perfect", "happy", "love", "easy", "successful", "wonderful", "amazing", "glad"
    )

    private val negativeWords = setOf(
        "kötü", "hata", "bozuk", "çalışmadı", "sorun", "başarısız", "uyarı", 
        "yanlış", "yavaş", "zor", "berbat", "üzüldüm", "nefret", "gıcık", "sinir",
        "çalışmıyor", "problem", "hatalı", "eksik", "kırıntı", "yetersiz",
        "bad", "error", "problem", "wrong", "fail", "slow", "hard", "difficult", 
        "dislike", "hate", "unhappy", "broken", "failed", "bug", "annoying", "poor"
    )

    fun analyze(text: String): SentimentAnalysis {
        val cleanText = text.lowercase(Locale.getDefault())
            .replace(Regex("[^a-zçğıöşuü\\s]"), " ")
        
        val tokens = cleanText.split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (tokens.isEmpty()) {
            return SentimentAnalysis(0f, SentimentType.NEUTRAL, "STABLE / CALM", "😐", 100)
        }

        var positiveCount = 0
        var negativeCount = 0

        // Use custom token matching & phrase matching
        for (token in tokens) {
            if (positiveWords.contains(token)) {
                positiveCount++
            } else if (negativeWords.contains(token)) {
                negativeCount++
            }
        }

        // Context checks (e.g. "değil" or "no/not" might flip the score, keeping it simple but smart!)
        val textLower = text.lowercase(Locale.getDefault())
        if (textLower.contains("değil") || textLower.contains("not ") || textLower.contains("don't") || textLower.contains("hiç")) {
            // Adjust confidence or balance if negation is spotted
            if (positiveCount > 0) {
                positiveCount--
                negativeCount++
            }
        }

        val totalMatches = positiveCount + negativeCount
        val score = if (totalMatches == 0) {
            0f
        } else {
            (positiveCount - negativeCount).toFloat() / totalMatches.toFloat()
        }

        val type = when {
            score > 0.15f -> SentimentType.POSITIVE
            score < -0.15f -> SentimentType.NEGATIVE
            else -> SentimentType.NEUTRAL
        }

        val description = when (type) {
            SentimentType.POSITIVE -> {
                if (score > 0.6f) "ENTHUSIASTIC & OPTIMAL" else "SERENE & COOPERATIVE"
            }
            SentimentType.NEGATIVE -> {
                if (score < -0.6f) "CRITICAL / DISRUPTIVE" else "STRESSED / APPREHENSIVE"
            }
            SentimentType.NEUTRAL -> "ANALYTICAL & COMPOSING"
        }

        val emoji = when (type) {
            SentimentType.POSITIVE -> "😊"
            SentimentType.NEGATIVE -> "😡"
            SentimentType.NEUTRAL -> "😐"
        }

        // Confidence starts high and adjusts by vocabulary density
        val vocabMatchDensity = (totalMatches.toFloat() / tokens.size.toFloat()).coerceIn(0f, 1f)
        val confidence = (50 + (vocabMatchDensity * 50)).toInt()

        return SentimentAnalysis(score, type, description, emoji, confidence)
    }
}
