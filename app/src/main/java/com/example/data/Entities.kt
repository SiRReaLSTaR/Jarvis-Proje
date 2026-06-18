package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chat_sessions")
data class ChatSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val model: String = "gemini-3.5-flash",
    val temperature: Float = 0.7f,
    val systemInstruction: String = "Sen kullanıcıya her konuda yardımcı olan, cana yakın ve çok zeki bir yapay zeka asistanısın.",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatSession::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sessionId"])]
)
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int,
    val role: String, // "user" veya "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
